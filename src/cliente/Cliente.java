package cliente;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Cliente {
	private final int PUERTO = 5000;
    private final String IP = "127.0.0.1";

    public void iniciar() {
        Socket cliente = null;
        ObjectOutputStream salida = null;
        ObjectInputStream entrada = null;
        try {
            cliente = new Socket(IP, PUERTO);//creacion de cliente con IP y PUERTO
            System.out.println("Conexion realizada con servidor");
            salida = new ObjectOutputStream(cliente.getOutputStream());
            entrada = new ObjectInputStream(cliente.getInputStream());
            String mensaje = (String) entrada.readObject();//lee el servidor
            System.out.println(mensaje);
            
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            try {
                if (entrada != null) {
                    entrada.close();
                }
                if (salida != null) {
                    salida.close();
                }
                if (cliente != null) {
                    cliente.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Fin cliente");
        }
    }
        
	public static void main(String[] args) {
		Cliente c = new Cliente();
        c.iniciar();


	}

}
