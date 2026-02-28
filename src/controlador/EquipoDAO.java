package controlador;

import Modelo.Equipo;
import java.util.List;

/**
 * Interfaz que define el contrato para el acceso a datos de equipos. Permite
 * cargar y guardar equipos desde diferentes fuentes (archivo de propiedades,
 * serialización, etc.). Sigue el principio de Inversión de Dependencias (DIP)
 * de SOLID.
 *
 * @author Julian - Andres - Miguel
 * @version 2.0
 */
public interface EquipoDAO {

    /**
     * Carga y retorna la lista de equipos desde la fuente de datos.
     *
     * @return Lista de equipos cargados.
     */
    List<Equipo> cargarEquipos();

    /**
     * Guarda la lista de equipos en la fuente de datos.
     *
     * @param equipos Lista de equipos a guardar.
     */
    void guardarEquipos(List<Equipo> equipos);
}
