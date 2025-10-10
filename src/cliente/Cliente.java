package cliente;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import compartido.Mensaje;

public class Cliente {
    private String ip, nombre;
    private int puerto;
    private Socket cliente;
    private ObjectOutputStream salida;
    private ObjectInputStream entrada;
    private VentanaCliente ventana;

    public Cliente(String ip, int puerto, String nombre, VentanaCliente ventana) {
        this.ip = ip;
        this.puerto = puerto;
        this.nombre = nombre;
        this.ventana = ventana;
    }

    public boolean iniciar() {
        try {
            cliente = new Socket(ip, puerto);
            salida = new ObjectOutputStream(cliente.getOutputStream());
            salida.flush();
            entrada = new ObjectInputStream(cliente.getInputStream());

            // Enviar nombre al servidor
            Mensaje mensaje = new Mensaje(Mensaje.TIPO_NOMBRE, nombre, "", nombre);
            salida.writeObject(mensaje);
            salida.flush();

            // Hilo para recibir mensajes
            new Thread(new HiloReceptor(entrada, ventana)).start();
            
            return true;
        } catch (Exception e) {
            ventana.mostrarMensaje("❌ Error al conectar: " + e.getMessage());
            return false;
        }
    }

    public void enviarMensaje(String mensajeTexto, boolean privado, String destinatario) {
        try {
            Mensaje mensaje;
            if (privado && !destinatario.isEmpty()) {
                mensaje = new Mensaje(Mensaje.TIPO_PRIVADO, nombre, destinatario, mensajeTexto);
            } else {
                mensaje = new Mensaje(Mensaje.TIPO_PUBLICO, nombre, mensajeTexto);
            }
            salida.writeObject(mensaje);
            salida.flush();
        } catch (IOException e) {
            ventana.mostrarMensaje("❌ Error al enviar mensaje: " + e.getMessage());
        }
    }

    public void desconectar() {
        try {
        	Mensaje mensaje = new Mensaje(Mensaje.TIPO_DESCONECTAR, nombre, "");
            salida.writeObject(mensaje);
            salida.flush();

            if (entrada != null) entrada.close();
            if (salida != null) salida.close();
            if (cliente != null) cliente.close();

            ventana.mostrarMensaje("⚠️ Desconectado del servidor.");
        } catch (IOException e) {
            ventana.mostrarMensaje("❌ Error al desconectar: " + e.getMessage());
        }
    }
}
