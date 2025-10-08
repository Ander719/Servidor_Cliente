package cliente;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.JCheckBox;

public class Login_Chat extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textIp;
	private JTextField textPuerto;
	private JTextField textPara;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login_Chat frame = new Login_Chat();
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
	public Login_Chat() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 779, 540);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblIp = new JLabel("Ip:");
		lblIp.setBounds(10, 10, 45, 13);
		contentPane.add(lblIp);
		
		textIp = new JTextField();
		textIp.setBounds(31, 7, 96, 19);
		contentPane.add(textIp);
		textIp.setColumns(10);
		
		textPuerto = new JTextField();
		textPuerto.setColumns(10);
		textPuerto.setBounds(208, 7, 96, 19);
		contentPane.add(textPuerto);
		
		JLabel lblPuerto = new JLabel("Puerto:");
		lblPuerto.setBounds(142, 13, 45, 13);
		contentPane.add(lblPuerto);
		
		JButton btnConectar = new JButton("Conectar");
		btnConectar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnConectar.setBounds(517, 6, 107, 21);
		contentPane.add(btnConectar);
		
		JButton btnDesconetar = new JButton("Desconetar");
		btnDesconetar.setEnabled(false);
		btnDesconetar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnDesconetar.setBounds(634, 6, 121, 21);
		contentPane.add(btnDesconetar);
		
		JLabel lblEstado = new JLabel("");
		lblEstado.setBounds(462, 37, 238, 13);
		contentPane.add(lblEstado);
		
		JTextArea textAreaChat = new JTextArea();
		textAreaChat.setBounds(10, 59, 745, 374);
		contentPane.add(textAreaChat);
		
		JCheckBox chckbxPrivado = new JCheckBox("Privado");
		chckbxPrivado.setBounds(6, 471, 96, 13);
		contentPane.add(chckbxPrivado);
		
		textPara = new JTextField();
		textPara.setBounds(130, 468, 96, 19);
		contentPane.add(textPara);
		textPara.setColumns(10);
		
		JLabel lblPara = new JLabel("Para:");
		lblPara.setBounds(102, 471, 45, 13);
		contentPane.add(lblPara);
		
		textField = new JTextField();
		textField.setBounds(265, 468, 373, 19);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JButton btnEnviar = new JButton("Enviar");
		btnEnviar.setBounds(648, 467, 107, 21);
		contentPane.add(btnEnviar);
		
		JLabel lblNombre = new JLabel("Nombre:");
		lblNombre.setBounds(338, 10, 63, 13);
		contentPane.add(lblNombre);
		
		JTextField textNombre = new JTextField();
		textNombre.setColumns(10);
		textNombre.setBounds(411, 7, 96, 19);
		contentPane.add(textNombre);
	}
}
