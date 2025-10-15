package cliente;

import compartido.Mensaje;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import java.io.ObjectInputStream;

/**
 * Hilo que recibe mensajes del servidor continuamente. Se ejecuta en paralelo
 * al hilo principal de la GUI. Usa SwingUtilities.invokeLater() para actualizar
 * la GUI de forma segura.
 */
public class HiloReceptor implements Runnable {

	private ObjectInputStream entrada;
	private VentanaCliente ventana;
	private boolean ejecutando;

	/**
	 * Constructor
	 * 
	 * @param entrada stream de entrada del servidor
	 * @param ventana referencia a la ventana principal
	 */
	public HiloReceptor(ObjectInputStream entrada, VentanaCliente ventana) {
		this.entrada = entrada;
		this.ventana = ventana;
		this.ejecutando = true;
	}

	/**
	 * Método principal del hilo Bucle infinito que recibe y procesa mensajes del
	 * servidor
	 */
	@Override
	public void run() {
		System.out.println("HiloReceptor iniciado");

		while (ejecutando) {
			try {
				// PASO 1: Recibir mensaje del servidor (bloqueante)
				Mensaje mensaje = (Mensaje) entrada.readObject();

				if (mensaje == null) {
					System.out.println("Mensaje nulo recibido, finalizando...");
					break;
				}

				// PASO 2: Procesar el mensaje
				procesarMensaje(mensaje);

			} catch (java.io.EOFException e) {
				// Conexión cerrada por el servidor
				System.out.println("Conexión cerrada por el servidor");
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						ventana.mostrarMensaje("❌ Conexión perdida con el servidor");
						ventana.deshabilitarEnvio();
						ventana.actualizarEstado("Desconectado");
					}
				});
				break;

			} catch (java.io.IOException e) {
				// Error de comunicación
				if (ejecutando) {
					System.err.println("Error de comunicación: " + e.getMessage());

					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							ventana.mostrarMensaje("❌ Error de comunicación con el servidor");
							ventana.deshabilitarEnvio();
							ventana.actualizarEstado("Desconectado");
						}
					});
				}
				break;

			} catch (ClassNotFoundException e) {
				System.err.println("Error al deserializar: " + e.getMessage());
				e.printStackTrace();
				break;
			}
		}

		System.out.println("HiloReceptor finalizado");
	}

	/**
	 * Procesa un mensaje recibido según su tipo Actualiza la GUI de forma segura
	 * usando SwingUtilities.invokeLater()
	 * 
	 * @param mensaje el mensaje recibido del servidor
	 */
	private void procesarMensaje(Mensaje mensaje) {
		String tipo = mensaje.getTipo();

		// PASO 3: Determinar tipo de mensaje y actuar
		if (tipo.equals(Mensaje.TIPO_PUBLICO)) {
			// Mensaje público
			mostrarMensajeEnGUI(mensaje);

		} else if (tipo.equals(Mensaje.TIPO_PRIVADO)) {
			// Mensaje privado
			mostrarMensajeEnGUI(mensaje);

		} else if (tipo.equals(Mensaje.TIPO_ACEPTADO)) {
			// Conexión aceptada (ya fue procesada en Cliente.conectar())
			System.out.println("Confirmación de aceptación recibida");

		} else if (tipo.equals(Mensaje.TIPO_LISTA_USUARIOS)) {
			// Lista actualizada de usuarios
			String usuarios = mensaje.getContenido();
			actualizarListaUsuarios(usuarios);

		} else if (tipo.equals(Mensaje.TIPO_USUARIO_ENTRO)) {
			// Notificación: nuevo usuario
			mostrarMensajeEnGUI(mensaje);
			
			ventana.solicitarListaUsuarios(); // <-- pedir al servidor la lista actualizada
			// También actualizar lista (si es necesario)
			// ventana.actualizarComboDestinatario(...);

		} else if (tipo.equals(Mensaje.TIPO_USUARIO_SALIO)) {
			// Notificación: usuario salió
			mostrarMensajeEnGUI(mensaje);

		} else if (tipo.equals(Mensaje.TIPO_ERROR)) {
			// Mensaje de error del servidor
			mostrarMensajeEnGUI(mensaje);

		} else if (tipo.equals(Mensaje.TIPO_INFO)) {
			// Mensaje informativo
			mostrarMensajeEnGUI(mensaje);

		} else {
			System.err.println("Tipo de mensaje desconocido: " + tipo);
		}
	}

	/**
	 * Muestra un mensaje en la GUI de forma segura Usa SwingUtilities.invokeLater()
	 * para ejecutar en el EDT
	 * 
	 * @param mensaje el mensaje a mostrar
	 */
	private void mostrarMensajeEnGUI(Mensaje mensaje) {
		// PASO 4: Encolar tarea de actualización de GUI en el EDT
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// PASO 5: Esto se ejecuta en el EDT (hilo de la GUI)
				String textoFormateado = mensaje.formatearParaChat();
				ventana.mostrarMensaje(textoFormateado);
			}
		});
	}

	/**
	 * Actualiza la lista de usuarios disponibles
	 * 
	 * @param usuarios string con los nombres de usuarios separados por comas
	 */
	private void actualizarListaUsuarios(String usuarios) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				if (usuarios != null && !usuarios.isEmpty()) {
					String[] listaUsuarios = usuarios.split(",");
					ventana.actualizarComboDestinatario(listaUsuarios);
				}
			}
		});
	}

	/**
	 * Detiene la ejecución del hilo
	 */
	public void detener() {
		ejecutando = false;
	}
}