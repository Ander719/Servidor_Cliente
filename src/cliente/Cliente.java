package cliente;

import compartido.Mensaje;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Clase Cliente que gestiona la conexión con el servidor de chat.
 * Se encarga de:
 * - Conectar con el servidor
 * - Autenticarse (enviar nombre de usuario)
 * - Enviar mensajes públicos y privados
 * - Desconectarse del servidor
 */
public class Cliente {
    
    private Socket socket;
    private ObjectOutputStream salida;
    private ObjectInputStream entrada;
    private String nombreUsuario;
    private boolean conectado;
    private VentanaCliente ventana;
    private HiloReceptor hiloReceptor;
    
    /**
     * Constructor
     * 
     * @param ventana referencia a la ventana principal para actualizar GUI
     */
    public Cliente(VentanaCliente ventana) {
        this.ventana = ventana;
        this.conectado = false;
        this.nombreUsuario = "";
    }
    
    /**
     * Intenta conectar con el servidor
     * 
     * @param ip dirección IP del servidor
     * @param puerto puerto del servidor
     * @param nombre nombre de usuario
     * @return true si la conexión fue exitosa, false si no
     */
    public boolean conectar(String ip, int puerto, String nombre) {
        try {
            // Validar parámetros
            if (ip == null || ip.trim().isEmpty()) {
                mostrarError("La IP no puede estar vacía");
                return false;
            }
            
            if (nombre == null || nombre.trim().isEmpty()) {
                mostrarError("El nombre de usuario no puede estar vacío");
                return false;
            }
            
            this.nombreUsuario = nombre.trim();
            
            // Crear conexión con el servidor
            System.out.println("Intentando conectar con " + ip + ":" + puerto);
            socket = new Socket(ip, puerto);
            
            // Crear streams (IMPORTANTE: salida primero, luego entrada)
            salida = new ObjectOutputStream(socket.getOutputStream());
            salida.flush(); // ¡CRÍTICO para evitar deadlock!
            
            entrada = new ObjectInputStream(socket.getInputStream());
            
            System.out.println("✅ Conexión establecida con el servidor");
            
            // Enviar nombre de usuario para autenticación
            Mensaje mensajeNombre = new Mensaje(Mensaje.TIPO_NOMBRE, nombreUsuario);
            salida.writeObject(mensajeNombre);
            salida.flush();
            
            System.out.println("Nombre enviado al servidor: " + nombreUsuario);
            
            socket.setSoTimeout(5000); // 5 segundos máximo
            // Esperar respuesta del servidor
            Mensaje respuesta = (Mensaje) entrada.readObject();
            socket.setSoTimeout(0);
            
            if (respuesta == null) {
                mostrarError("Servidor respondió con mensaje nulo");
                cerrarConexion();
                return false;
            }
            
            String tipo = respuesta.getTipo();
            if (Mensaje.TIPO_ACEPTADO.equals(tipo)) {
                conectado = true;
                System.out.println("✅ Autenticación exitosa");
                iniciarHiloReceptor();
                return true;
            } else if (Mensaje.TIPO_RECHAZADO.equals(tipo)) {
                mostrarError(respuesta.getContenido());
                cerrarConexion();
                return false;
            } else {
                mostrarError("Respuesta inesperada del servidor: " + tipo);
                cerrarConexion();
                return false;
            }
            
        } catch (java.net.ConnectException e) {
            mostrarError("No se pudo conectar al servidor.\nVerifica que esté ejecutándose.");
            return false;
            
        } catch (java.net.SocketTimeoutException e) {
            mostrarError("Timeout: El servidor tardó demasiado en responder.");
            return false;
            
        } catch (IOException e) {
            mostrarError("Error de conexión: " + e.getMessage());
            return false;
            
        } catch (ClassNotFoundException e) {
            mostrarError("Error al recibir mensaje del servidor: " + e.getMessage());
            return false;
            
        } catch (Exception e) {
            mostrarError("Error inesperado: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Inicia el hilo que recibe mensajes del servidor
     */
    private void iniciarHiloReceptor() {
        hiloReceptor = new HiloReceptor(entrada, ventana);
        Thread thread = new Thread(hiloReceptor);
        thread.setDaemon(true);
        thread.start();
        System.out.println("Hilo receptor iniciado");
    }
    
    /**
     * Envía un mensaje público a todos los usuarios
     * 
     * @param contenido el contenido del mensaje
     */
    public void enviarMensajePublico(String contenido) {
        if (!conectado) {
            mostrarError("No estás conectado al servidor");
            return;
        }
        
        if (contenido == null || contenido.trim().isEmpty()) {
            return; // No enviar mensajes vacíos
        }
        
        try {
            Mensaje mensaje = new Mensaje(
                Mensaje.TIPO_PUBLICO,
                nombreUsuario,
                contenido.trim()
            );
            
            salida.writeObject(mensaje);
            salida.flush();
            
            System.out.println("[ENVIADO] Mensaje público: " + contenido);
            
        } catch (IOException e) {
            mostrarError("Error al enviar mensaje: " + e.getMessage());
            desconectar();
        }
    }
    
    /**
     * Envía un mensaje privado a un usuario específico
     * 
     * @param destinatario nombre del usuario destinatario
     * @param contenido el contenido del mensaje
     */
    public void enviarMensajePrivado(String destinatario, String contenido) {
        if (!conectado) {
            mostrarError("No estás conectado al servidor");
            return;
        }
        
        if (destinatario == null || destinatario.trim().isEmpty()) {
            mostrarError("Debes especificar un destinatario");
            return;
        }
        
        if (contenido == null || contenido.trim().isEmpty()) {
            return; // No enviar mensajes vacíos
        }
        
        try {
            Mensaje mensaje = new Mensaje(
                Mensaje.TIPO_PRIVADO,
                nombreUsuario,
                destinatario.trim(),
                contenido.trim()
            );
            
            salida.writeObject(mensaje);
            salida.flush();
            
            System.out.println("[ENVIADO] Mensaje privado a " + destinatario + ": " + contenido);
            
        } catch (IOException e) {
            mostrarError("Error al enviar mensaje privado: " + e.getMessage());
            desconectar();
        }
    }
    
    /**
     * Desconecta del servidor
     */
    public void desconectar() {
        if (!conectado) {
            return; // Ya desconectado
        }
        
        try {
            // Enviar comando de desconexión
            Mensaje desconexion = new Mensaje(
                Mensaje.TIPO_DESCONECTAR,
                nombreUsuario,
                "Desconectando..."
            );
            
            salida.writeObject(desconexion);
            salida.flush();
            
            System.out.println("Comando de desconexión enviado");
            
        } catch (IOException e) {
            System.err.println("Error al enviar desconexión: " + e.getMessage());
        } finally {
            cerrarConexion();
        }
    }
    
    /**
     * Cierra la conexión y los recursos
     */
    private void cerrarConexion() {
        conectado = false;
        
        try {
            if (entrada != null) {
                entrada.close();
            }
            if (salida != null) {
                salida.close();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            System.err.println("Error al cerrar conexión: " + e.getMessage());
        }
        
        System.out.println("❌ Desconectado del servidor");
    }
    
    /**
     * Verifica si el cliente está conectado
     * 
     * @return true si está conectado, false si no
     */
    public boolean isConectado() {
        return conectado;
    }
    
    /**
     * Obtiene el nombre de usuario
     * 
     * @return el nombre de usuario
     */
    public String getNombreUsuario() {
        return nombreUsuario;
    }
    
    /**
     * Muestra un mensaje de error en la ventana
     * 
     * @param mensaje el mensaje de error
     */
    private void mostrarError(String mensaje) {
        System.err.println("❌ ERROR: " + mensaje);
        if (ventana != null) {
            ventana.mostrarErrorDialog(mensaje);
        }
    }

	public void enviarMensaje(Mensaje pedirLista) {
		   try {
		        if (salida != null && conectado) {
		            salida.writeObject(pedirLista);
		            salida.flush();
		        }
		    } catch (IOException e) {
		        ventana.mostrarErrorDialog("Error enviando mensaje: " + e.getMessage());
		    }
		
	}
}