package controlador;

import Modelo.Equipo;
import Modelo.Jugador;
import java.util.List;

/**
 * Controlador que gestiona la lógica de negocio relacionada con los equipos.
 * Calcula puntajes, resuelve empates y determina el equipo ganador. Sigue el
 * principio de Responsabilidad Única (SRP) de SOLID.
 *
 * @author Julian - Andres - Miguel
 * @version 2.0
 */
public class EquipoController {

    /**
     * Calcula el puntaje total de un equipo sumando los puntajes de sus
     * jugadores.
     *
     * @param equipo Equipo del que se calcula el puntaje.
     * @return Puntaje total del equipo.
     */
    public int calcularPuntajeEquipo(Equipo equipo) {
        int total = 0;
        for (Jugador jugador : equipo.getJugadores()) {
            total += jugador.getPuntaje();
        }
        return total;
    }

    /**
     * Calcula el total de intentos exitosos de un equipo sumando los intentos
     * exitosos de sus jugadores.
     *
     * @param equipo Equipo del que se calculan los intentos exitosos.
     * @return Total de intentos exitosos del equipo.
     */
    public int obtenerTotalIntentosExitosos(Equipo equipo) {
        int total = 0;
        for (Jugador jugador : equipo.getJugadores()) {
            total += jugador.getIntentosExitosos();
        }
        return total;
    }

    /**
     * Determina el equipo ganador de la lista dada. En caso de empate en
     * puntaje, se resuelve a favor del equipo con mayor cantidad de intentos
     * exitosos.
     *
     * @param equipos Lista de equipos participantes.
     * @return Equipo con el mayor puntaje (o mayor intentos exitosos en caso de
     * empate).
     */
    public Equipo obtenerEquipoGanador(List<Equipo> equipos) {
        Equipo ganador = equipos.get(0);
        for (Equipo equipo : equipos) {
            if (calcularPuntajeEquipo(equipo) > calcularPuntajeEquipo(ganador)) {
                ganador = equipo;
            } else if (calcularPuntajeEquipo(equipo) == calcularPuntajeEquipo(ganador)) {
                ganador = resolverEmpate(ganador, equipo);
            }
        }
        return ganador;
    }

    /**
     * Resuelve un empate entre dos equipos comparando sus intentos exitosos.
     * Gana el equipo con mayor cantidad de intentos exitosos.
     *
     * @param equipo1 Primer equipo empatado.
     * @param equipo2 Segundo equipo empatado.
     * @return Equipo ganador del desempate.
     */
    private Equipo resolverEmpate(Equipo equipo1, Equipo equipo2) {
        int exitos1 = obtenerTotalIntentosExitosos(equipo1);
        int exitos2 = obtenerTotalIntentosExitosos(equipo2);
        if (exitos2 > exitos1) {
            return equipo2;
        }
        return equipo1;
    }
}
