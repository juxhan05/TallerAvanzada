package Modelo;

import java.io.Serializable;

/**
 * Representa un jugador participante en el juego del Balero. Almacena la
 * información personal del jugador y sus estadísticas acumuladas durante la
 * partida (puntaje, intentos e intentos exitosos). Implementa
 * {@code Serializable} para permitir la persistencia del estado.
 *
 * @author Julian - Andres - Miguel
 * @version 2.0
 */
public class Jugador implements Serializable {

    /**
     * Identificador de versión para la serialización.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Código único del estudiante.
     */
    private String codigo;

    /**
     * Nombre completo del jugador.
     */
    private String nombre;

    /**
     * Puntaje acumulado por el jugador en la partida.
     */
    private int puntaje;

    /**
     * Número total de intentos realizados por el jugador.
     */
    private int intentos;

    /**
     * Número de intentos que resultaron en una embocada exitosa.
     */
    private int intentosExitosos;

    /**
     * Construye un jugador con su código y nombre. Inicializa el puntaje,
     * intentos e intentos exitosos en cero.
     *
     * @param codigo Código único del estudiante.
     * @param nombre Nombre completo del jugador.
     */
    public Jugador(String codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.puntaje = 0;
        this.intentos = 0;
        this.intentosExitosos = 0;
    }

    /**
     * Retorna el código del estudiante.
     *
     * @return Código del jugador.
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * Establece el código del estudiante.
     *
     * @param codigo Nuevo código del jugador.
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    /**
     * Retorna el nombre del jugador.
     *
     * @return Nombre del jugador.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del jugador.
     *
     * @param nombre Nuevo nombre del jugador.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Retorna el puntaje acumulado del jugador.
     *
     * @return Puntaje del jugador.
     */
    public int getPuntaje() {
        return puntaje;
    }

    /**
     * Establece el puntaje del jugador.
     *
     * @param puntaje Nuevo puntaje del jugador.
     */
    public void setPuntaje(int puntaje) {
        this.puntaje = puntaje;
    }

    /**
     * Retorna el número total de intentos realizados.
     *
     * @return Número de intentos.
     */
    public int getIntentos() {
        return intentos;
    }

    /**
     * Establece el número total de intentos realizados.
     *
     * @param intentos Nuevo número de intentos.
     */
    public void setIntentos(int intentos) {
        this.intentos = intentos;
    }

    /**
     * Retorna el número de intentos exitosos (embocadas logradas).
     *
     * @return Número de intentos exitosos.
     */
    public int getIntentosExitosos() {
        return intentosExitosos;
    }

    /**
     * Establece el número de intentos exitosos.
     *
     * @param intentosExitosos Nuevo número de intentos exitosos.
     */
    public void setIntentosExitosos(int intentosExitosos) {
        this.intentosExitosos = intentosExitosos;
    }
}
