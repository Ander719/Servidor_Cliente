package cliente;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

/**
 * Gestor de colores para los usuarios en el chat.
 * Asigna un color único a cada usuario automáticamente.
 * Los colores se eligen de una paleta predefinida.
 */
public class ColorManager {
    
    // Paleta de colores disponibles
    private static final Color[] COLORES_DISPONIBLES = {
        new Color(255, 0, 0),       // Rojo
        new Color(0, 0, 255),       // Azul
        new Color(0, 128, 0),       // Verde oscuro
        new Color(255, 165, 0),     // Naranja
        new Color(128, 0, 128),     // Púrpura
        new Color(0, 128, 128),     // Teal
        new Color(255, 192, 203),   // Rosa
        new Color(165, 42, 42),     // Marrón
        new Color(34, 139, 34),     // Verde bosque
        new Color(220, 20, 60),     // Carmesí
        new Color(100, 149, 237),   // Azul cornflower
        new Color(184, 134, 11),    // Amarillo oscuro
    };
    
    // Mapa: usuario → color
    private static Map<String, Color> mapeoUsuariosColores = new HashMap<>();
    
    // Contador para asignar colores secuencialmente
    private static int indiceColor = 0;
    
    /**
     * Obtiene el color asignado a un usuario.
     * Si el usuario no tiene color asignado, se le asigna uno automáticamente.
     * 
     * @param nombreUsuario el nombre del usuario
     * @return el color asignado al usuario
     */
    public static Color obtenerColorUsuario(String nombreUsuario) {
        // Si ya tiene color, devolverlo
        if (mapeoUsuariosColores.containsKey(nombreUsuario)) {
            return mapeoUsuariosColores.get(nombreUsuario);
        }
        
        // Asignar nuevo color
        Color color = COLORES_DISPONIBLES[indiceColor % COLORES_DISPONIBLES.length];
        mapeoUsuariosColores.put(nombreUsuario, color);
        indiceColor++;
        
        return color;
    }
    
    /**
     * Obtiene colores especiales para diferentes tipos de mensaje
     */
    public static Color obtenerColorTipo(String tipo) {
        switch (tipo) {
            case "SISTEMA":    // Mensajes del sistema (usuario entró/salió)
                return new Color(128, 128, 128);  // Gris
                
            case "ERROR":      // Mensajes de error
                return new Color(255, 0, 0);      // Rojo
                
            case "INFO":       // Mensajes informativos
                return new Color(0, 102, 204);    // Azul oscuro
                
            case "PRIVADO":    // Prefijo de mensaje privado
                return new Color(128, 0, 255);    // Púrpura
                
            default:
                return Color.BLACK;
        }
    }
    
    /**
     * Limpia el mapeo de colores (útil para una nueva conexión)
     */
    public static void limpiar() {
        mapeoUsuariosColores.clear();
        indiceColor = 0;
    }
}