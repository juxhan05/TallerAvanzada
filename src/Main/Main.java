package Main;

import controlador.ControlPrincipal;
import javax.swing.SwingUtilities;

/**
 * Clase principal de la aplicación del Balero.
 * Punto de entrada del programa. Lanza el controlador principal
 * dentro del hilo de eventos de Swing (EDT) para garantizar
 * la seguridad de la interfaz gráfica.
 *
 * @author Julian - Andres - Miguel
 * @version 2.0
 */
public class Main {

    /**
     * Método principal de la aplicación.
     *
     * @param args Argumentos de línea de comandos (no utilizados).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ControlPrincipal control = new ControlPrincipal();
            control.ejecutar();
        });
    }
}