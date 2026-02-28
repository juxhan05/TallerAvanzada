package Modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa un equipo participante en el juego del Balero. Cada equipo
 * pertenece a un proyecto curricular, tiene un nombre y está compuesto por
 * exactamente 3 jugadores. Implementa {@code Serializable} para permitir la
 * persistencia del estado.
 *
 * @author Julian - Andres - Miguel
 * @version 2.0
 */
public class Equipo implements Serializable {

    /**
     * Identificador de versión para la serialización.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Nombre del proyecto curricular al que pertenece el equipo.
     */
    private String nombreProyectoCurricular;

    /**
     * Nombre identificador del equipo.
     */
    private String nombreEquipo;

    /**
     * Lista de jugadores que conforman el equipo (máximo 3).
     */
    private List<Jugador> jugadores;

    /**
     * Construye un equipo con su proyecto curricular y nombre. Inicializa la
     * lista de jugadores vacía.
     *
     * @param nombreProyectoCurricular Nombre del proyecto curricular.
     * @param nombreEquipo Nombre del equipo.
     */
    public Equipo(String nombreProyectoCurricular, String nombreEquipo) {
        this.nombreProyectoCurricular = nombreProyectoCurricular;
        this.nombreEquipo = nombreEquipo;
        this.jugadores = new ArrayList<>();
    }

    /**
     * Retorna el nombre del proyecto curricular del equipo.
     *
     * @return Nombre del proyecto curricular.
     */
    public String getNombreProyectoCurricular() {
        return nombreProyectoCurricular;
    }

    /**
     * Establece el nombre del proyecto curricular del equipo.
     *
     * @param nombreProyectoCurricular Nuevo nombre del proyecto curricular.
     */
    public void setNombreProyectoCurricular(String nombreProyectoCurricular) {
        this.nombreProyectoCurricular = nombreProyectoCurricular;
    }

    /**
     * Retorna el nombre del equipo.
     *
     * @return Nombre del equipo.
     */
    public String getNombreEquipo() {
        return nombreEquipo;
    }

    /**
     * Establece el nombre del equipo.
     *
     * @param nombreEquipo Nuevo nombre del equipo.
     */
    public void setNombreEquipo(String nombreEquipo) {
        this.nombreEquipo = nombreEquipo;
    }

    /**
     * Retorna la lista de jugadores del equipo.
     *
     * @return Lista de jugadores.
     */
    public List<Jugador> getJugadores() {
        return jugadores;
    }

    /**
     * Establece la lista de jugadores del equipo.
     *
     * @param jugadores Nueva lista de jugadores.
     */
    public void setJugadores(List<Jugador> jugadores) {
        this.jugadores = jugadores;
    }
}
