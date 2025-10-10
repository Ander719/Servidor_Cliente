package cliente;

import compartido.Mensaje;
import java.io.ObjectInputStream;

public class HiloReceptor implements Runnable {
    private ObjectInputStream entrada;
    private VentanaCliente ventana;

    public HiloReceptor(ObjectInputStream entrada, VentanaCliente ventana) {
        this.entrada = entrada;
        this.ventana = ventana;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Mensaje mensaje = (Mensaje) entrada.readObject();
                ventana.mostrarMensaje(mensaje.formatearParaChat());
            }
        } catch (Exception e) {
            ventana.mostrarMensaje("⚠️ Conexión cerrada.");
        }
    }
}
