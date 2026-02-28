package controlador;

import Modelo.Resultado;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación de {@link ResultadoDAO} que persiste resultados usando un
 * archivo de acceso aleatorio ({@link RandomAccessFile}). Cada registro tiene
 * un tamaño fijo de {@value #TAMANIO_REGISTRO} bytes, lo que permite acceso
 * directo a cualquier posición del archivo. Sigue el principio de
 * Responsabilidad Única (SRP) de SOLID.
 *
 * @author Julian - Andres - Miguel
 * @version 2.0
 */
public class ResultadoRandomAccessDAO implements ResultadoDAO {

    /**
     * Número de caracteres fijos por campo String en el archivo.
     */
    private static final int TAMANIO_STRING = 30;

    /**
     * Tamaño en bytes de cada registro: 4 (clave) + 4 Strings * 30 chars * 2
     * bytes + 4 (puntaje) = 248 bytes.
     */
    private static final int TAMANIO_REGISTRO = 248;

    /**
     * Ruta del archivo de acceso aleatorio.
     */
    private final String rutaArchivo;

    /**
     * Construye el DAO con la ruta del archivo de acceso aleatorio.
     *
     * @param rutaArchivo Ruta absoluta o relativa del archivo .dat.
     */
    public ResultadoRandomAccessDAO(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    /**
     * Guarda un resultado al final del archivo de acceso aleatorio. Asigna
     * automáticamente la clave según la posición en el archivo.
     *
     * @param resultado Resultado a guardar.
     */
    @Override
    public void guardarResultado(Resultado resultado) {
        try (RandomAccessFile archivo = new RandomAccessFile(rutaArchivo, "rw")) {
            long longitud = archivo.length();
            int clave = (int) (longitud / TAMANIO_REGISTRO) + 1;
            archivo.seek(longitud);
            archivo.writeInt(clave);
            escribirStringFijo(archivo, resultado.getNombreEquipo());
            escribirStringFijo(archivo, resultado.getJugador1());
            escribirStringFijo(archivo, resultado.getJugador2());
            escribirStringFijo(archivo, resultado.getJugador3());
            archivo.writeInt(resultado.getPuntajeTotal());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lee y retorna todos los resultados almacenados en el archivo.
     *
     * @return Lista de todos los resultados históricos.
     */
    @Override
    public List<Resultado> leerResultados() {
        List<Resultado> resultados = new ArrayList<>();
        try (RandomAccessFile archivo = new RandomAccessFile(rutaArchivo, "r")) {
            while (archivo.getFilePointer() < archivo.length()) {
                int clave = archivo.readInt();
                String nombreEquipo = leerStringFijo(archivo);
                String jugador1 = leerStringFijo(archivo);
                String jugador2 = leerStringFijo(archivo);
                String jugador3 = leerStringFijo(archivo);
                int puntaje = archivo.readInt();
                resultados.add(new Resultado(
                        clave,
                        nombreEquipo.trim(),
                        jugador1.trim(),
                        jugador2.trim(),
                        jugador3.trim(),
                        puntaje
                ));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultados;
    }

    /**
     * Cuenta cuántas veces ha ganado un equipo consultando el archivo completo.
     *
     * @param nombreEquipo Nombre del equipo a consultar.
     * @return Número de victorias del equipo.
     */
    @Override
    public int contarVictorias(String nombreEquipo) {
        int contador = 0;
        for (Resultado r : leerResultados()) {
            if (r.getNombreEquipo().equals(nombreEquipo)) {
                contador++;
            }
        }
        return contador;
    }

    /**
     * Escribe un String con longitud fija en el archivo. Si el texto es más
     * corto, se rellena con espacios. Si es más largo, se trunca.
     *
     * @param archivo Archivo donde se escribe.
     * @param texto Texto a escribir.
     * @throws IOException Si ocurre un error de escritura.
     */
    private void escribirStringFijo(RandomAccessFile archivo, String texto)
            throws IOException {
        StringBuilder sb = new StringBuilder(texto);
        if (sb.length() > TAMANIO_STRING) {
            sb.setLength(TAMANIO_STRING);
        } else {
            while (sb.length() < TAMANIO_STRING) {
                sb.append(" ");
            }
        }
        archivo.writeChars(sb.toString());
    }

    /**
     * Lee un String con longitud fija desde el archivo.
     *
     * @param archivo Archivo desde donde se lee.
     * @return String leído con posibles espacios de relleno.
     * @throws IOException Si ocurre un error de lectura.
     */
    private String leerStringFijo(RandomAccessFile archivo) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < TAMANIO_STRING; i++) {
            sb.append(archivo.readChar());
        }
        return sb.toString();
    }
}
