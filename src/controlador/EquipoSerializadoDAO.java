package controlador;

import Modelo.Equipo;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación de {@link EquipoDAO} que persiste equipos mediante
 * serialización Java. Guarda y carga la lista completa de equipos en un archivo
 * binario (.dat). Sigue el principio de Responsabilidad Única (SRP) y el
 * principio de Inversión de Dependencias (DIP) de SOLID.
 *
 * @author Julian - Andres - Miguel
 * @version 2.0
 */
public class EquipoSerializadoDAO implements EquipoDAO {

    /**
     * Ruta del archivo serializado.
     */
    private final String rutaArchivo;

    /**
     * Construye el DAO con la ruta del archivo serializado indicado.
     *
     * @param rutaArchivo Ruta absoluta o relativa del archivo .dat.
     */
    public EquipoSerializadoDAO(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    /**
     * Carga los equipos desde el archivo serializado. Si el archivo no existe,
     * retorna una lista vacía.
     *
     * @return Lista de equipos deserializados, o lista vacía si no existe el
     * archivo.
     */
    @Override
    public List<Equipo> cargarEquipos() {
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois
                = new ObjectInputStream(new FileInputStream(archivo))) {
            return (List<Equipo>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * Guarda la lista de equipos en el archivo serializado. Sobreescribe el
     * archivo si ya existe.
     *
     * @param equipos Lista de equipos a serializar.
     */
    @Override
    public void guardarEquipos(List<Equipo> equipos) {
        try (ObjectOutputStream oos
                = new ObjectOutputStream(new FileOutputStream(rutaArchivo))) {
            oos.writeObject(equipos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
