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
import java.awt.Color;
import java.awt.Font;
import javax.swing.JScrollPane;

public class VentanaCliente extends JFrame {

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
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 779, 540);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblIp = new JLabel("Ip:");
		lblIp.setBounds(10, 16, 45, 13);
		contentPane.add(lblIp);
		
		textIp = new JTextField();
		textIp.setBounds(51, 13, 96, 19);
		contentPane.add(textIp);
		textIp.setColumns(10);
		
		textPuerto = new JTextField();
		textPuerto.setColumns(10);
		textPuerto.setBounds(232, 13, 96, 19);
		contentPane.add(textPuerto);
		
		JLabel lblPuerto = new JLabel("Puerto:");
		lblPuerto.setBounds(179, 19, 45, 13);
		contentPane.add(lblPuerto);
		
		JButton btnConectar = new JButton("Conectar");
		btnConectar.setFont(new Font("Tahoma", Font.BOLD, 10));
		btnConectar.setForeground(new Color(0, 0, 0));
		btnConectar.setBackground(new Color(128, 255, 128));
		btnConectar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnConectar.setBounds(517, 6, 107, 21);
		contentPane.add(btnConectar);
		
		JButton btnDesconetar = new JButton("Desconetar");
		btnDesconetar.setFont(new Font("Tahoma", Font.BOLD, 10));
		btnDesconetar.setForeground(new Color(0, 0, 0));
		btnDesconetar.setBackground(new Color(255, 0, 0));
		btnDesconetar.setEnabled(false);
		btnDesconetar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnDesconetar.setBounds(634, 6, 121, 21);
		contentPane.add(btnDesconetar);
		
		JLabel lblEstado = new JLabel("");
		lblEstado.setBounds(517, 37, 238, 13);
		contentPane.add(lblEstado);
		
		JCheckBox chckbxPrivado = new JCheckBox("Privado");
		chckbxPrivado.setBounds(6, 471, 96, 13);
		contentPane.add(chckbxPrivado);
		
		textPara = new JTextField();
		textPara.setBounds(129, 468, 96, 19);
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
		btnEnviar.setFont(new Font("Tahoma", Font.BOLD, 10));
		btnEnviar.setForeground(new Color(0, 0, 0));
		btnEnviar.setBackground(new Color(0, 128, 255));
		btnEnviar.setBounds(648, 467, 107, 21);
		contentPane.add(btnEnviar);
		
		JLabel lblNombre = new JLabel("Nombre:");
		lblNombre.setBounds(338, 16, 63, 13);
		contentPane.add(lblNombre);
		
		JTextField textNombre = new JTextField();
		textNombre.setColumns(10);
		textNombre.setBounds(411, 13, 96, 19);
		contentPane.add(textNombre);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 69, 745, 366);
		contentPane.add(scrollPane);
		
		JTextArea textArea = new JTextArea();
		textArea.setBounds(12, 71, 711, 364);
		contentPane.add(textArea);
	}
}
