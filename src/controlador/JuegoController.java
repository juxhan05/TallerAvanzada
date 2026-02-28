package controlador;

import Modelo.Equipo;
import Modelo.Jugador;
import Modelo.Resultado;
import Modelo.Embocada;
import Modelo.PuntajeEmbocada;
import java.util.List;
import java.util.Random;

/**
 * Controlador principal de la lógica del juego del Balero. Coordina los turnos
 * de los equipos y jugadores, y gestiona el resultado final.
 *
 * @author Julian - Andres - Miguel
 * @version 2.0
 */
public class JuegoController {

    private EquipoController equipoController;
    private JugadorController jugadorController;
    private EquipoDAO equipoDAO;
    private ResultadoDAO resultadoDAO;
    private List<Equipo> equipos;
    private int tiempoPorEquipo;
    private Random random;

    /**
     * Constructor que recibe todos los colaboradores por inyección de
     * dependencias.
     *
     * @param equipoController Controlador de equipos.
     * @param jugadorController Controlador de jugadores.
     * @param equipoDAO DAO para carga de equipos.
     * @param resultadoDAO DAO para persistencia de resultados.
     */
    public JuegoController(EquipoController equipoController,
            JugadorController jugadorController,
            EquipoDAO equipoDAO,
            ResultadoDAO resultadoDAO) {
        this.equipoController = equipoController;
        this.jugadorController = jugadorController;
        this.equipoDAO = equipoDAO;
        this.resultadoDAO = resultadoDAO;
        this.random = new Random();
    }

    /**
     * Carga los equipos desde el DAO configurado.
     */
    public void cargarEquipos() {
        this.equipos = equipoDAO.cargarEquipos();
    }

    /**
     * Establece la lista de equipos directamente (sin pasar por el DAO).
     *
     * @param equipos Lista de equipos a usar en el juego.
     */
    public void setEquipos(List<Equipo> equipos) {
        this.equipos = equipos;
    }

    /**
     * Devuelve la lista de equipos cargados.
     *
     * @return Lista de equipos.
     */
    public List<Equipo> getEquipos() {
        return equipos;
    }

    /**
     * Establece el tiempo de juego por equipo en segundos.
     *
     * @param tiempoPorEquipo Tiempo en segundos.
     */
    public void setTiempoPorEquipo(int tiempoPorEquipo) {
        this.tiempoPorEquipo = tiempoPorEquipo;
    }

    /**
     * Inicia el juego ejecutando los turnos de todos los equipos y guardando el
     * resultado final.
     *
     * @return Equipo ganador.
     */
    public Equipo iniciarJuego() {
        reiniciarEstadisticas();
        for (Equipo equipo : equipos) {
            ejecutarTurnoEquipo(equipo);
        }
        Equipo ganador = equipoController.obtenerEquipoGanador(equipos);
        int puntajeTotal = equipoController.calcularPuntajeEquipo(ganador);

        Resultado resultado = new Resultado(
                0,
                ganador.getNombreEquipo(),
                ganador.getJugadores().get(0).getNombre(),
                ganador.getJugadores().get(1).getNombre(),
                ganador.getJugadores().get(2).getNombre(),
                puntajeTotal
        );
        resultadoDAO.guardarResultado(resultado);
        return ganador;
    }

    /**
     * Ejecuta el turno de un equipo repartiendo el tiempo entre sus jugadores.
     *
     * @param equipo Equipo en turno.
     */
    private void ejecutarTurnoEquipo(Equipo equipo) {
        for (Jugador jugador : equipo.getJugadores()) {
            ejecutarTurnoJugador(jugador, tiempoPorEquipo);
        }
    }

    /**
     * Ejecuta el turno de un jugador simulando intentos de embocada.
     *
     * @param jugador Jugador en turno.
     * @param tiempo Número de intentos disponibles.
     */
    private void ejecutarTurnoJugador(Jugador jugador, int tiempo) {
        for (int i = 0; i < tiempo; i++) {
            jugadorController.incrementarIntento(jugador);
            Embocada embocada = generarEmbocadaAleatoria();
            if (PuntajeEmbocada.esExitosa(embocada)) {
                jugadorController.incrementarIntentoExitoso(jugador);
            }
            jugadorController.sumarPuntaje(jugador, PuntajeEmbocada.getPuntaje(embocada));
        }
    }

    /**
     * Genera una embocada aleatoria incluyendo la posibilidad de FALLA.
     *
     * @return Embocada aleatoria.
     */
    public Embocada generarEmbocadaAleatoria() {
        Embocada[] valores = Embocada.values();
        int indice = random.nextInt(valores.length);
        return valores[indice];
    }

    /**
     * Reinicia las estadísticas de todos los jugadores de todos los equipos.
     */
    private void reiniciarEstadisticas() {
        for (Equipo equipo : equipos) {
            for (Jugador jugador : equipo.getJugadores()) {
                jugadorController.reiniciarEstadisticas(jugador);
            }
        }
    }
}
