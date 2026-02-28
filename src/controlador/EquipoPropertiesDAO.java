package controlador;

import Modelo.Equipo;
import Modelo.Jugador;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Implementación de {@link EquipoDAO} que carga equipos desde un archivo de
 * propiedades. El formato esperado de cada entrada es:
 * <pre>
 * equipoN=ProyectoCurricular;NombreEquipo;codigo1;nombre1;codigo2;nombre2;codigo3;nombre3
 * </pre> Incluye validación del formato para informar errores claros al
 * usuario. Sigue el principio de Responsabilidad Única (SRP) de SOLID.
 *
 * @author Julian - Andres - Miguel
 * @version2 2.0
 */
public class EquipoPropertiesDAO implements EquipoDAO {

    /**
     * Número de campos esperados por línea del archivo de propiedades.
     */
    private static final int CAMPOS_ESPERADOS = 8;

    /**
     * Ruta del archivo de propiedades.
     */
    private final String rutaArchivo;

    /**
     * Lista de errores encontrados durante la carga.
     */
    private final List<String> errores = new ArrayList<>();

    /**
     * Construye el DAO con la ruta del archivo de propiedades indicado.
     *
     * @param rutaArchivo Ruta absoluta o relativa del archivo .properties.
     */
    public EquipoPropertiesDAO(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    /**
     * Carga los equipos desde el archivo de propiedades. Valida el formato de
     * cada línea e ignora las que tengan errores, acumulando los mensajes de
     * error para su consulta posterior.
     *
     * @return Lista de equipos válidos cargados desde el archivo.
     */
    @Override
    public List<Equipo> cargarEquipos() {
        errores.clear();
        List<Equipo> equipos = new ArrayList<>();
        Properties properties = new Properties();

        try (FileInputStream fis = new FileInputStream(rutaArchivo)) {
            properties.load(fis);

            if (properties.isEmpty()) {
                errores.add("El archivo de propiedades está vacío.");
                return equipos;
            }

            for (String key : properties.stringPropertyNames()) {
                String linea = properties.getProperty(key);

                if (linea == null || linea.trim().isEmpty()) {
                    errores.add("La línea '" + key + "' está vacía.");
                    continue;
                }

                String[] datos = linea.split(";");

                if (datos.length < CAMPOS_ESPERADOS) {
                    errores.add("La línea '" + key + "' tiene " + datos.length
                            + " campos, se esperaban " + CAMPOS_ESPERADOS + ".");
                    continue;
                }

                // Verificar que ningún campo esté vacío
                boolean camposValidos = true;
                for (int i = 0; i < CAMPOS_ESPERADOS; i++) {
                    if (datos[i].trim().isEmpty()) {
                        errores.add("Campo vacío en posición " + i
                                + " de la línea '" + key + "'.");
                        camposValidos = false;
                        break;
                    }
                }
                if (!camposValidos) {
                    continue;
                }

                String proyecto = datos[0].trim();
                String nombreEquipo = datos[1].trim();
                Equipo equipo = new Equipo(proyecto, nombreEquipo);

                Jugador j1 = new Jugador(datos[2].trim(), datos[3].trim());
                Jugador j2 = new Jugador(datos[4].trim(), datos[5].trim());
                Jugador j3 = new Jugador(datos[6].trim(), datos[7].trim());

                equipo.getJugadores().add(j1);
                equipo.getJugadores().add(j2);
                equipo.getJugadores().add(j3);
                equipos.add(equipo);
            }

        } catch (IOException e) {
            errores.add("No se pudo leer el archivo: " + e.getMessage());
        }

        return equipos;
    }

    /**
     * Operación no implementada para este DAO. El archivo de propiedades se usa
     * solo para carga inicial.
     *
     * @param equipos Lista de equipos (no utilizada).
     */
    @Override
    public void guardarEquipos(List<Equipo> equipos) {
        // No requerido para properties según el enunciado
    }

    /**
     * Retorna los errores encontrados durante la última carga. La lista estará
     * vacía si no hubo errores.
     *
     * @return Lista de mensajes de error.
     */
    public List<String> getErrores() {
        return new ArrayList<>(errores);
    }

    /**
     * Indica si hubo errores durante la última carga.
     *
     * @return {@code true} si hubo al menos un error.
     */
    public boolean tieneErrores() {
        return !errores.isEmpty();
    }
}
