package compartido;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Clase para encapsular los mensajes entre cliente y servidor.
 * Implementa Serializable para poder enviarla por ObjectOutputStream/ObjectInputStream.
 * 
 * IMPORTANTE: Esta clase debe estar IDÉNTICA en ambos proyectos (Cliente y Servidor)
 */
public class Mensaje implements Serializable {
    
    // Serial Version UID - DEBE SER EL MISMO en cliente y servidor
    private static final long serialVersionUID = 1L;
    
    // Tipos de mensaje
    public static final String TIPO_NOMBRE = "NOMBRE";           // Cliente envía su nombre
    public static final String TIPO_PUBLICO = "PUBLICO";         // Mensaje para todos
    public static final String TIPO_PRIVADO = "PRIVADO";         // Mensaje para un usuario específico
    public static final String TIPO_DESCONECTAR = "DESCONECTAR"; // Cliente se desconecta
    public static final String TIPO_ACEPTADO = "ACEPTADO";       // Servidor acepta conexión
    public static final String TIPO_RECHAZADO = "RECHAZADO";     // Servidor rechaza conexión
    public static final String TIPO_LISTA_USUARIOS = "LISTA_USUARIOS"; // Lista de usuarios conectados
    public static final String TIPO_USUARIO_ENTRO = "USUARIO_ENTRO";   // Notificación: nuevo usuario
    public static final String TIPO_USUARIO_SALIO = "USUARIO_SALIO";   // Notificación: usuario salió
    public static final String TIPO_ERROR = "ERROR";             // Mensaje de error
    public static final String TIPO_INFO = "INFO";               // Mensaje informativo
    
    // Atributos del mensaje
    private String tipo;
    private String emisor;
    private String destinatario;  // null si es mensaje público
    private String contenido;
    private LocalDateTime timestamp;
    
    // Constructor completo
    public Mensaje(String tipo, String emisor, String destinatario, String contenido) {
        this.tipo = tipo;
        this.emisor = emisor;
        this.destinatario = destinatario;
        this.contenido = contenido;
        this.timestamp = LocalDateTime.now();
    }
    
    // Constructor para mensajes sin destinatario específico (públicos, comandos, etc.)
    public Mensaje(String tipo, String emisor, String contenido) {
        this(tipo, emisor, null, contenido);
    }
    
    // Constructor para mensajes del servidor (sin emisor específico)
    public Mensaje(String tipo, String contenido) {
        this(tipo, "SERVIDOR", null, contenido);
    }
    
    // Getters
    public String getTipo() {
        return tipo;
    }
    
    public String getEmisor() {
        return emisor;
    }
    
    public String getDestinatario() {
        return destinatario;
    }
    
    public String getContenido() {
        return contenido;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    // Setters (por si necesitas modificar después de crear)
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    public void setEmisor(String emisor) {
        this.emisor = emisor;
    }
    
    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }
    
    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
    
    // Métodos de utilidad
    
    /**
     * Verifica si el mensaje es público
     */
    public boolean esPublico() {
        return TIPO_PUBLICO.equals(tipo);
    }
    
    /**
     * Verifica si el mensaje es privado
     */
    public boolean esPrivado() {
        return TIPO_PRIVADO.equals(tipo);
    }
    
    /**
     * Formatea el timestamp del mensaje
     */
    public String getTimestampFormateado() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return timestamp.format(formatter);
    }
    
    /**
     * Representación en String del mensaje (útil para logs)
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(getTimestampFormateado()).append("] ");
        sb.append("Tipo: ").append(tipo);
        
        if (emisor != null && !emisor.equals("SERVIDOR")) {
            sb.append(" | Emisor: ").append(emisor);
        }
        
        if (destinatario != null) {
            sb.append(" | Destinatario: ").append(destinatario);
        }
        
        sb.append(" | Contenido: ").append(contenido);
        
        return sb.toString();
    }
    
    /**
     * Formatea el mensaje para mostrarlo en la interfaz del cliente
     */
    public String formatearParaChat() {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(getTimestampFormateado()).append("] ");
        
        switch (tipo) {
            case TIPO_PUBLICO:
                sb.append(emisor).append(": ").append(contenido);
                break;
                
            case TIPO_PRIVADO:
                if (emisor.equals("YO")) {
                    sb.append("(Privado a ").append(destinatario).append("): ").append(contenido);
                } else {
                    sb.append("(Privado de ").append(emisor).append("): ").append(contenido);
                }
                break;
                
            case TIPO_USUARIO_ENTRO:
                sb.append("*** ").append(contenido).append(" se ha unido al chat ***");
                break;
                
            case TIPO_USUARIO_SALIO:
                sb.append("*** ").append(contenido).append(" ha salido del chat ***");
                break;
                
            case TIPO_INFO:
                sb.append("ℹ️ ").append(contenido);
                break;
                
            case TIPO_ERROR:
                sb.append("❌ ERROR: ").append(contenido);
                break;
                
            default:
                sb.append(contenido);
                break;
        }
        
        return sb.toString();
    }
}