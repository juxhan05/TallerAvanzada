package controlador;

import Modelo.Jugador;

/**
 * Controlador que gestiona la lógica de negocio relacionada con los jugadores.
 * Maneja el incremento de intentos, intentos exitosos, puntaje y el reinicio de
 * estadísticas de cada jugador. Sigue el principio de Responsabilidad Única
 * (SRP) de SOLID.
 *
 * @author Julian - Andres - Miguel
 * @version 2.0
 */
public class JugadorController {

    /**
     * Incrementa en uno el contador de intentos del jugador.
     *
     * @param jugador Jugador al que se le incrementa el intento.
     */
    public void incrementarIntento(Jugador jugador) {
        jugador.setIntentos(jugador.getIntentos() + 1);
    }

    /**
     * Incrementa en uno el contador de intentos exitosos del jugador.
     *
     * @param jugador Jugador al que se le incrementa el intento exitoso.
     */
    public void incrementarIntentoExitoso(Jugador jugador) {
        jugador.setIntentosExitosos(jugador.getIntentosExitosos() + 1);
    }

    /**
     * Suma los puntos indicados al puntaje acumulado del jugador.
     *
     * @param jugador Jugador al que se le suman los puntos.
     * @param puntos Cantidad de puntos a sumar.
     */
    public void sumarPuntaje(Jugador jugador, int puntos) {
        jugador.setPuntaje(jugador.getPuntaje() + puntos);
    }

    /**
     * Reinicia todas las estadísticas del jugador a cero. Se llama al inicio de
     * cada partida.
     *
     * @param jugador Jugador cuyas estadísticas se reinician.
     */
    public void reiniciarEstadisticas(Jugador jugador) {
        jugador.setPuntaje(0);
        jugador.setIntentos(0);
        jugador.setIntentosExitosos(0);
    }
}
