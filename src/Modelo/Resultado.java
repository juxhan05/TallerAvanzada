package Modelo;

/**
 * Representa el resultado de una partida del juego del Balero. Almacena los
 * datos del equipo ganador y su puntaje para ser persistidos en el archivo de
 * acceso aleatorio (RandomAccessFile).
 *
 * @author Julian - Andres - Miguel
 * @version 2.0
 */
public class Resultado {

    /**
     * Clave única del registro en el archivo de acceso aleatorio.
     */
    private int clave;

    /**
     * Nombre del equipo ganador.
     */
    private String nombreEquipo;

    /**
     * Nombre del primer jugador del equipo ganador.
     */
    private String jugador1;

    /**
     * Nombre del segundo jugador del equipo ganador.
     */
    private String jugador2;

    /**
     * Nombre del tercer jugador del equipo ganador.
     */
    private String jugador3;

    /**
     * Puntaje total obtenido por el equipo ganador.
     */
    private int puntajeTotal;

    /**
     * Construye un resultado con todos sus datos.
     *
     * @param clave Clave única del registro.
     * @param nombreEquipo Nombre del equipo ganador.
     * @param jugador1 Nombre del primer jugador.
     * @param jugador2 Nombre del segundo jugador.
     * @param jugador3 Nombre del tercer jugador.
     * @param puntajeTotal Puntaje total del equipo.
     */
    public Resultado(int clave, String nombreEquipo,
            String jugador1, String jugador2, String jugador3,
            int puntajeTotal) {
        this.clave = clave;
        this.nombreEquipo = nombreEquipo;
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
        this.jugador3 = jugador3;
        this.puntajeTotal = puntajeTotal;
    }

    /**
     * Retorna la clave única del registro.
     *
     * @return Clave del registro.
     */
    public int getClave() {
        return clave;
    }

    /**
     * Establece la clave única del registro.
     *
     * @param clave Nueva clave del registro.
     */
    public void setClave(int clave) {
        this.clave = clave;
    }

    /**
     * Retorna el nombre del equipo ganador.
     *
     * @return Nombre del equipo.
     */
    public String getNombreEquipo() {
        return nombreEquipo;
    }

    /**
     * Establece el nombre del equipo ganador.
     *
     * @param nombreEquipo Nuevo nombre del equipo.
     */
    public void setNombreEquipo(String nombreEquipo) {
        this.nombreEquipo = nombreEquipo;
    }

    /**
     * Retorna el nombre del primer jugador.
     *
     * @return Nombre del jugador 1.
     */
    public String getJugador1() {
        return jugador1;
    }

    /**
     * Establece el nombre del primer jugador.
     *
     * @param jugador1 Nuevo nombre del jugador 1.
     */
    public void setJugador1(String jugador1) {
        this.jugador1 = jugador1;
    }

    /**
     * Retorna el nombre del segundo jugador.
     *
     * @return Nombre del jugador 2.
     */
    public String getJugador2() {
        return jugador2;
    }

    /**
     * Establece el nombre del segundo jugador.
     *
     * @param jugador2 Nuevo nombre del jugador 2.
     */
    public void setJugador2(String jugador2) {
        this.jugador2 = jugador2;
    }

    /**
     * Retorna el nombre del tercer jugador.
     *
     * @return Nombre del jugador 3.
     */
    public String getJugador3() {
        return jugador3;
    }

    /**
     * Establece el nombre del tercer jugador.
     *
     * @param jugador3 Nuevo nombre del jugador 3.
     */
    public void setJugador3(String jugador3) {
        this.jugador3 = jugador3;
    }

    /**
     * Retorna el puntaje total del equipo ganador.
     *
     * @return Puntaje total.
     */
    public int getPuntajeTotal() {
        return puntajeTotal;
    }

    /**
     * Establece el puntaje total del equipo ganador.
     *
     * @param puntajeTotal Nuevo puntaje total.
     */
    public void setPuntajeTotal(int puntajeTotal) {
        this.puntajeTotal = puntajeTotal;
    }
}
