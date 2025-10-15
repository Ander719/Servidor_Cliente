package cliente;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import compartido.Mensaje;
import java.awt.Toolkit;

/**
 * Ventana principal del cliente de chat.
 * Interfaz gráfica para conectar al servidor y enviar/recibir mensajes.
 */
public class VentanaCliente extends JFrame {

	private static final long serialVersionUID = 1L;
	
	// Panel principal
	private JPanel contentPane;
	
	// Componentes de conexión
	private JTextField textIp;
	private JTextField textPuerto;
	private JTextField textNombre;
	private JButton btnConectar;
	private JButton btnDesconectar;
	private JLabel lblEstado;
	
	// Componentes de chat
	private JTextPane textPaneChat;
	private JScrollPane scrollPane;
	
	// Componentes de envío
	private JCheckBox chckbxPrivado;
	private JTextField textDestinatario;
	private JTextField textMensaje;
	private JButton btnEnviar;
	private JComboBox<String> comboDestinatario;
	
	// Lógica del cliente
	private Cliente cliente;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaCliente frame = new VentanaCliente();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public VentanaCliente() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(VentanaCliente.class.getResource("/imagen/Captura de pantalla 2025-10-11 012323.png")));
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 779, 540);
		setTitle("Chat-DAM\r\n");
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		// ==================== PANEL DE CONEXIÓN ====================
		
		JLabel lblIp = new JLabel("IP:");
		lblIp.setBounds(10, 16, 45, 13);
		contentPane.add(lblIp);
		
		textIp = new JTextField();
		textIp.setBounds(51, 13, 96, 19);
		textIp.setText("127.0.0.1"); // Valor por defecto
		contentPane.add(textIp);
		textIp.setColumns(10);
		
		JLabel lblPuerto = new JLabel("Puerto:");
		lblPuerto.setBounds(179, 19, 45, 13);
		contentPane.add(lblPuerto);
		
		textPuerto = new JTextField();
		textPuerto.setColumns(10);
		textPuerto.setBounds(232, 13, 96, 19);
		textPuerto.setText("5000"); // Valor por defecto
		contentPane.add(textPuerto);
		
		JLabel lblNombre = new JLabel("Nombre:");
		lblNombre.setBounds(338, 16, 63, 13);
		contentPane.add(lblNombre);
		
		textNombre = new JTextField();
		textNombre.setColumns(10);
		textNombre.setBounds(411, 13, 96, 19);
		contentPane.add(textNombre);
		
		// Botón Conectar
		btnConectar = new JButton("Conectar");
		btnConectar.setFont(new Font("Tahoma", Font.BOLD, 10));
		btnConectar.setForeground(new Color(0, 0, 0));
		btnConectar.setBackground(new Color(128, 255, 128));
		btnConectar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnConectar_Click();
			}
		});
		btnConectar.setBounds(517, 6, 107, 21);
		contentPane.add(btnConectar);
		
		// Botón Desconectar
		btnDesconectar = new JButton("Desconectar");
		btnDesconectar.setFont(new Font("Tahoma", Font.BOLD, 10));
		btnDesconectar.setForeground(new Color(0, 0, 0));
		btnDesconectar.setBackground(new Color(255, 0, 0));
		btnDesconectar.setEnabled(false);
		btnDesconectar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnDesconectar_Click();
			}
		});
		btnDesconectar.setBounds(634, 6, 121, 21);
		contentPane.add(btnDesconectar);
		
		// Label Estado
		lblEstado = new JLabel("Desconectado");
		lblEstado.setBounds(517, 37, 238, 13);
		contentPane.add(lblEstado);
		
		// ==================== PANEL DE CHAT ====================
		
		// JScrollPane con JTextArea dentro
		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 69, 745, 366);
		contentPane.add(scrollPane);
		
		textPaneChat = new JTextPane();
		textPaneChat.setEditable(false); // No editable, solo lectura
		textPaneChat.setFont(new Font("Monospaced", Font.PLAIN, 11));
		scrollPane.setViewportView(textPaneChat); // ¡IMPORTANTE!
		
		/*textAreaChat = new JTextArea();
		textAreaChat.setEditable(false); // No editable, solo lectura
		textAreaChat.setLineWrap(true);
		textAreaChat.setWrapStyleWord(true);
		textAreaChat.setFont(new Font("Monospaced", Font.PLAIN, 11));
		scrollPane.setViewportView(textAreaChat); // ¡IMPORTANTE!*/
		
		// ==================== PANEL DE ENVÍO ====================
		
		JLabel lblPrivado = new JLabel("Privado:");
		lblPrivado.setBounds(6, 450, 63, 13);
		contentPane.add(lblPrivado);
		
		chckbxPrivado = new JCheckBox("");
		chckbxPrivado.setBounds(73, 450, 20, 13);
		chckbxPrivado.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				actualizarEstadoPrivado();
			}
		});
		contentPane.add(chckbxPrivado);
		
		JLabel lblDestinatario = new JLabel("Destinatario:");
		lblDestinatario.setBounds(102, 450, 80, 13);
		contentPane.add(lblDestinatario);
		
		comboDestinatario = new JComboBox<String>();
		comboDestinatario.setBounds(190, 447, 150, 21);
		comboDestinatario.addItem("Todos"); // Opción por defecto
		comboDestinatario.setEnabled(false);
		contentPane.add(comboDestinatario);
		
		textDestinatario = new JTextField();
		textDestinatario.setBounds(350, 447, 150, 19);
		textDestinatario.setVisible(false);
		contentPane.add(textDestinatario);
		textDestinatario.setColumns(10);
		
		JLabel lblMensaje = new JLabel("Mensaje:");
		lblMensaje.setBounds(10, 480, 63, 13);
		contentPane.add(lblMensaje);
		
		textMensaje = new JTextField();
		textMensaje.setBounds(73, 477, 568, 19);
		textMensaje.setEnabled(false);
		textMensaje.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					btnEnviar_Click();
				}
			}
			
			@Override
			public void keyReleased(KeyEvent e) {}
			
			@Override
			public void keyTyped(KeyEvent e) {}
		});
		contentPane.add(textMensaje);
		textMensaje.setColumns(10);
		
		btnEnviar = new JButton("Enviar");
		btnEnviar.setFont(new Font("Tahoma", Font.BOLD, 10));
		btnEnviar.setForeground(new Color(0, 0, 0));
		btnEnviar.setBackground(new Color(0, 128, 255));
		btnEnviar.setEnabled(false);
		btnEnviar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnEnviar_Click();
			}
		});
		btnEnviar.setBounds(648, 477, 107, 21);
		contentPane.add(btnEnviar);
		
		// Inicializar cliente
		cliente = new Cliente(this);
	}
	
	// ==================== MÉTODOS DE EVENTOS ====================
	
	/**
	 * Evento del botón Conectar
	 */
	private void btnConectar_Click() {
		// Validar campos
		String ip = textIp.getText().trim();
		String puertoStr = textPuerto.getText().trim();
		String nombre = textNombre.getText().trim();
		
		if (ip.isEmpty() || puertoStr.isEmpty() || nombre.isEmpty()) {
			JOptionPane.showMessageDialog(this,
				"Por favor, rellena todos los campos",
				"Campos incompletos",
				JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		// Validar puerto
		int puerto;
		try {
			puerto = Integer.parseInt(puertoStr);
			if (puerto < 1 || puerto > 65535) {
				throw new NumberFormatException();
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this,
				"El puerto debe ser un número entre 1 y 65535",
				"Puerto inválido",
				JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		// Validar nombre
		if (nombre.length() < 3 || nombre.length() > 20) {
			JOptionPane.showMessageDialog(this,
				"El nombre debe tener entre 3 y 20 caracteres",
				"Nombre inválido",
				JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if (!nombre.matches("[a-zA-Z0-9_]+")) {
			JOptionPane.showMessageDialog(this,
				"El nombre solo puede contener letras, números y guión bajo",
				"Nombre inválido",
				JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		// Intentar conectar
		boolean conectado = cliente.conectar(ip, puerto, nombre);
		
		if (conectado) {
			// Conexión exitosa
			habilitarChat();
			lblEstado.setText("Conectado como: " + nombre);
			mostrarMensajeConColor("ℹ️ Bienvenido al chat, " + nombre + "!", ColorManager.obtenerColorTipo("INFO"));
		}
		// Si no conecta, Cliente.java mostrará el error en un dialog
	}
	
	/**
	 * Evento del botón Desconectar
	 */
	private void btnDesconectar_Click() {
		cliente.desconectar();
		deshabilitarChat();
		lblEstado.setText("Desconectado");
		mostrarMensajeConColor("ℹ️ Te has desconectado del servidor", ColorManager.obtenerColorTipo("INFO"));
	}
	
	/**
	 * Evento del botón Enviar
	 */
	private void btnEnviar_Click() {
		String mensaje = textMensaje.getText().trim();
		
		if (mensaje.isEmpty()) {
			return; // No enviar mensajes vacíos
		}
		
		if (chckbxPrivado.isSelected()) {
			// Mensaje privado
			String destinatario = (String) comboDestinatario.getSelectedItem();
			
			if (destinatario == null || destinatario.equals("Todos")) {
				JOptionPane.showMessageDialog(this,
					"Debes seleccionar un usuario para enviar un mensaje privado",
					"Destinatario no seleccionado",
					JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			cliente.enviarMensajePrivado(destinatario, mensaje);
			
			// Mostrar en mi propio chat que envié el mensaje
			mostrarMensajeConColor("[Privado a " + destinatario + "]: " + mensaje + "\n", ColorManager.obtenerColorTipo("INFO"));
			
			
		} else {
			// Mensaje público
			cliente.enviarMensajePublico(mensaje);
		}
		
		// Limpiar campo
		textMensaje.setText("");
	}
	
	/**
	 * Actualiza el estado del checkbox de privado
	 */
	private void actualizarEstadoPrivado() {
		if (chckbxPrivado.isSelected()) {
			comboDestinatario.setEnabled(true);
		} else {
			comboDestinatario.setEnabled(false);
			comboDestinatario.setSelectedIndex(0); // "Todos"
		}
	}
	
	// ==================== MÉTODOS LLAMADOS DESDE CLIENTE Y HILO RECEPTOR ====================
	
	/**
	 * Muestra un mensaje en el área de chat
	 * 
	 * @param mensaje el mensaje a mostrar
	 */
	/*public void mostrarMensaje(String mensaje) {
		textAreaChat.append(mensaje + "\n");
		// Auto-scroll al final
		textAreaChat.setCaretPosition(textAreaChat.getDocument().getLength());
	}*/
	/**
	 * Muestra un mensaje en el área de chat con color automático según el usuario
	 * 
	 * @param mensaje el mensaje a mostrar
	 */
	public void mostrarMensaje(String mensaje) {
		// Extraer el nombre del usuario del mensaje
		String nombreUsuario = extraerNombreUsuario(mensaje);
		java.awt.Color color = ColorManager.obtenerColorUsuario(nombreUsuario);
		mostrarMensajeConColor(mensaje, color);
	}
	
	/**
	 * Muestra un mensaje en el área de chat con un color específico
	 * 
	 * @param mensaje el mensaje a mostrar
	 * @param color el color del texto
	 */
	public void mostrarMensajeConColor(String mensaje, java.awt.Color color) {
		StyledDocument doc = textPaneChat.getStyledDocument();
		SimpleAttributeSet atributos = new SimpleAttributeSet();
		StyleConstants.setForeground(atributos, color);
		
		try {
			doc.insertString(doc.getLength(), mensaje + "\n", atributos);
		} catch (javax.swing.text.BadLocationException e) {
			System.err.println("Error al insertar texto: " + e.getMessage());
		}
		
		// Auto-scroll al final
		textPaneChat.setCaretPosition(doc.getLength());
	}
	
	/**
	 * Extrae el nombre del usuario de un mensaje
	 * Ej: "marcos: Hola" → "marcos"
	 * Ej: "(Privado de Juan): Hola" → "Juan"
	 * 
	 * @param mensaje el mensaje
	 * @return el nombre del usuario
	 */
	private String extraerNombreUsuario(String mensaje) {
		// Si contiene ": " (mensaje público o privado)
		if (mensaje.contains(": ")) {
			int pos = mensaje.indexOf(": ");
			String prefijo = mensaje.substring(0, pos);
			
			// Si es privado: "(Privado de Juan)"
			if (prefijo.contains("Privado de ")) {
				return prefijo.replace("(Privado de ", "").replace(")", "");
			}
			
			// Si es "Privado a Juan"
			if (prefijo.contains("Privado a ")) {
				return prefijo.replace("(Privado a ", "").replace(")", "");
			}
			
			// Si es público: "marcos"
			// Quitar prefijo de timestamp si existe
			if (prefijo.contains("] ")) {
				prefijo = prefijo.substring(prefijo.lastIndexOf("] ") + 2);
			}
			
			return prefijo.trim();
		}
		
		// Si no tiene nombre, es un mensaje del sistema
		return "SISTEMA";
	}
	/**
	 * Muestra un diálogo de error
	 * 
	 * @param mensaje el mensaje de error
	 */
	public void mostrarErrorDialog(String mensaje) {
		JOptionPane.showMessageDialog(this,
			mensaje,
			"Error",
			JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * Habilita los controles de chat después de conectar
	 */
	private void habilitarChat() {
		textIp.setEnabled(false);
		textPuerto.setEnabled(false);
		textNombre.setEnabled(false);
		btnConectar.setEnabled(false);
		
		btnDesconectar.setEnabled(true);
		textMensaje.setEnabled(true);
		btnEnviar.setEnabled(true);
		chckbxPrivado.setEnabled(true);
		comboDestinatario.setEnabled(true);
	}
	
	/**
	 * Deshabilita los controles de chat después de desconectar
	 */
	private void deshabilitarChat() {
		textIp.setEnabled(true);
		textPuerto.setEnabled(true);
		textNombre.setEnabled(true);
		btnConectar.setEnabled(true);
		
		btnDesconectar.setEnabled(false);
		textMensaje.setEnabled(false);
		btnEnviar.setEnabled(false);
		chckbxPrivado.setEnabled(false);
		chckbxPrivado.setSelected(false);
		comboDestinatario.setEnabled(false);
		
		textPaneChat.setText(""); // Limpiar área de chat
		ColorManager.limpiar(); // Limpiar colores
	}
	
	/**
	 * Deshabilita el envío de mensajes (cuando se pierde conexión)
	 */
	public void deshabilitarEnvio() {
		textMensaje.setEnabled(false);
		btnEnviar.setEnabled(false);
		chckbxPrivado.setEnabled(false);
		comboDestinatario.setEnabled(false);
	}
	
	/**
	 * Actualiza el estado en la etiqueta
	 * 
	 * @param estado el nuevo estado a mostrar
	 */
	public void actualizarEstado(String estado) {
		lblEstado.setText(estado);
	}
	
	/**
	 * Actualiza la lista de usuarios disponibles en el combo
	 * 
	 * @param usuarios array con los nombres de usuarios
	 */
	public void actualizarComboDestinatario(String[] usuarios) {
		comboDestinatario.removeAllItems();
		comboDestinatario.addItem("Todos");
		
		for (String usuario : usuarios) {
			if (!usuario.trim().isEmpty()) {
				comboDestinatario.addItem(usuario.trim());
			}
		}
	}

	public void solicitarListaUsuarios() {
		 try {
			 Mensaje pedirLista = new Mensaje("TIPO_LISTA_USUARIOS", ""); // tipo inventado
			    cliente.enviarMensaje(pedirLista);
		    } catch (Exception e) {
		        System.err.println("Error solicitando lista de usuarios: " + e.getMessage());
		    }
		
	}
}