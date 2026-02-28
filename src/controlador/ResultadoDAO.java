package controlador;

import Modelo.Resultado;
import java.util.List;

/**
 * Interfaz que define el contrato para el acceso a datos de resultados. Permite
 * guardar, leer y consultar resultados históricos de partidas. Sigue el
 * principio de Inversión de Dependencias (DIP) de SOLID.
 *
 * @author Julian - Andres - Miguel
 * @version 2.0
 */
public interface ResultadoDAO {

    /**
     * Guarda un resultado de partida en la fuente de datos.
     *
     * @param resultado Resultado a guardar.
     */
    void guardarResultado(Resultado resultado);

    /**
     * Lee y retorna todos los resultados almacenados.
     *
     * @return Lista de todos los resultados históricos.
     */
    List<Resultado> leerResultados();

    /**
     * Cuenta cuántas veces ha ganado un equipo en partidas anteriores.
     *
     * @param nombreEquipo Nombre del equipo a consultar.
     * @return Número de victorias del equipo.
     */
    int contarVictorias(String nombreEquipo);
}
