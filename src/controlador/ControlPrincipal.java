package controlador;

import Modelo.Embocada;
import Modelo.Equipo;
import Modelo.Jugador;
import Modelo.PuntajeEmbocada;
import Modelo.Resultado;
import vista.VistaPrincipal;
import vista.VistaResultados;

import javax.swing.*;
import java.io.File;
import java.util.List;

/**
 * Controlador principal de la aplicación del Balero. Coordina la vista, los
 * DAOs y los controladores de lógica. Sigue el patrón MVC: es el único punto
 * que conoce tanto la vista como el modelo.
 *
 * @author Julian - Andres - Miguel
 * @version 2.0
 */
public class ControlPrincipal {

    // ── Rutas base ─────────────────────────────────────────────────────────────
    private static final String CARPETA_DATA = "Specs/data/";
    private static final String RUTA_SERIALIZADO = CARPETA_DATA + "equipos.dat";
    private static final String RUTA_RESULTADOS = CARPETA_DATA + "resultados.dat";

    // ── Componentes MVC ────────────────────────────────────────────────────────
    private VistaPrincipal vista;
    private JuegoController juegoController;
    private EquipoController equipoController;
    private JugadorController jugadorController;
    private ResultadoDAO resultadoDAO;
    private EquipoSerializadoDAO serializadoDAO;

    /**
     * Lista de equipos cargados en memoria.
     */
    private List<Equipo> equipos;

    // ──────────────────────────────────────────────────────────────────────────
    //  Punto de entrada
    // ──────────────────────────────────────────────────────────────────────────
    /**
     * Inicializa y muestra la aplicación. Debe llamarse desde el hilo de
     * eventos de Swing (EDT).
     */
    public void ejecutar() {
        new File(CARPETA_DATA).mkdirs();

        serializadoDAO = new EquipoSerializadoDAO(RUTA_SERIALIZADO);
        resultadoDAO = new ResultadoRandomAccessDAO(RUTA_RESULTADOS);
        equipoController = new EquipoController();
        jugadorController = new JugadorController();

        juegoController = new JuegoController(
                equipoController,
                jugadorController,
                serializadoDAO,
                resultadoDAO
        );

        SwingUtilities.invokeLater(() -> {
            vista = new VistaPrincipal();
            configurarListeners();
        });
    }

    // ──────────────────────────────────────────────────────────────────────────
    //  Listeners
    // ──────────────────────────────────────────────────────────────────────────
    /**
     * Conecta los botones de la vista con la lógica del controlador.
     */
    private void configurarListeners() {
        vista.setOnCargar(this::accionCargar);
        vista.setOnIniciar(this::accionIniciar);
        vista.setOnSalir(this::accionSalir);
    }

    // ──────────────────────────────────────────────────────────────────────────
    //  Acciones
    // ──────────────────────────────────────────────────────────────────────────
    /**
     * Acción del botón "Cargar Equipos". Permite elegir entre serializado o
     * properties usando JFileChooser.
     */
    private void accionCargar() {
        String[] opciones = {"Archivo serializado (.dat)", "Archivo de propiedades (.properties)"};
        int eleccion = JOptionPane.showOptionDialog(
                vista,
                "¿Desde qué archivo deseas cargar los equipos?",
                "Cargar Equipos",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        if (eleccion == JOptionPane.CLOSED_OPTION) {
            return;
        }

        EquipoDAO dao;

        if (eleccion == 0) {
            File archivo = abrirJFileChooser("Seleccionar archivo serializado", "dat");
            if (archivo == null) {
                return;
            }
            dao = new EquipoSerializadoDAO(archivo.getAbsolutePath());
        } else {
            File archivo = abrirJFileChooser("Seleccionar archivo de propiedades", "properties");
            if (archivo == null) {
                return;
            }
            dao = new EquipoPropertiesDAO(archivo.getAbsolutePath());
        }

        equipos = dao.cargarEquipos();

        if (equipos == null || equipos.isEmpty()) {
            vista.setEstado(" No se encontraron equipos en el archivo seleccionado.");
            return;
        }

        juegoController.setEquipos(equipos);
        vista.mostrarEquipos(equipos);
        vista.setEstado(" " + equipos.size() + " equipo(s) cargados. ¡Listos para jugar!");
    }

    /**
     * Acción del botón "Iniciar Juego". Configura el tiempo y arranca el juego
     * en un hilo separado.
     */
    private void accionIniciar() {
        if (equipos == null || equipos.isEmpty()) {
            JOptionPane.showMessageDialog(vista,
                    "Primero debes cargar los equipos.",
                    "Sin equipos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int tiempo = vista.getTiempoSeleccionado();
        juegoController.setTiempoPorEquipo(tiempo);

        vista.setBtnCargarHabilitado(false);
        vista.setBtnIniciarHabilitado(false);

        new Thread(() -> ejecutarJuegoConUI(tiempo)).start();
    }

    /**
     * Acción del botón "Salir". Serializa los equipos y cierra la aplicación.
     */
    private void accionSalir() {
        int confirm = JOptionPane.showConfirmDialog(
                vista,
                "¿Deseas salir de la aplicación?",
                "Confirmar salida",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm == JOptionPane.YES_OPTION) {
            if (equipos != null && !equipos.isEmpty()) {
                serializadoDAO.guardarEquipos(equipos);
            }
            System.exit(0);
        }
    }

    // ──────────────────────────────────────────────────────────────────────────
    //  Lógica del juego con actualización de UI
    // ──────────────────────────────────────────────────────────────────────────
    /**
     * Ejecuta el juego turno a turno actualizando la interfaz gráfica. Corre en
     * un hilo separado para no bloquear el EDT.
     *
     * @param tiempoTotal Tiempo total por equipo en segundos.
     */
    private void ejecutarJuegoConUI(int tiempoTotal) {
        int tiempoPorJugador = tiempoTotal / 3;

        for (int e = 0; e < equipos.size(); e++) {
            final int indiceEquipo = e;
            Equipo equipo = equipos.get(e);

            // Reiniciar estadísticas del equipo
            for (Jugador j : equipo.getJugadores()) {
                jugadorController.reiniciarEstadisticas(j);
            }

            // Resaltar equipo en turno
            SwingUtilities.invokeLater(() -> {
                vista.resaltarEquipo(indiceEquipo);
                vista.setEstado(" Jugando: " + equipo.getNombreEquipo());
            });

            // Turno de cada jugador
            for (int j = 0; j < equipo.getJugadores().size(); j++) {
                final int indiceJugador = j;
                Jugador jugador = equipo.getJugadores().get(j);

                SwingUtilities.invokeLater(() -> {
                    vista.resaltarJugador(indiceJugador);
                    vista.setEstado(" " + equipo.getNombreEquipo()
                            + " — turno de: " + jugador.getNombre());
                });

                // Simular tiempo real del jugador
                long inicio = System.currentTimeMillis();
                long duracion = tiempoPorJugador * 1000L;

                while (System.currentTimeMillis() - inicio < duracion) {
                    jugadorController.incrementarIntento(jugador);
                    Embocada embocada = juegoController.generarEmbocadaAleatoria();

                    if (PuntajeEmbocada.esExitosa(embocada)) {
                        jugadorController.incrementarIntentoExitoso(jugador);
                    }
                    jugadorController.sumarPuntaje(jugador, PuntajeEmbocada.getPuntaje(embocada));
                    final String tipo = embocada.name();
                    final int puntaje = equipoController.calcularPuntajeEquipo(equipo);

                    SwingUtilities.invokeLater(()
                            -> vista.setEstado(" " + jugador.getNombre()
                                    + "  " + tipo
                                    + " | Puntaje equipo: " + puntaje)
                    );

                    try {
                        Thread.sleep(800);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                }
            }

            // Resetear resalte al terminar turno del equipo
            SwingUtilities.invokeLater(() -> {
                vista.resaltarJugador(-1);
                vista.resaltarEquipo(-1);
            });
        }

        // ── Fin del juego ──────────────────────────────────────────────────────
        Equipo ganador = equipoController.obtenerEquipoGanador(equipos);
        int puntajeFinal = equipoController.calcularPuntajeEquipo(ganador);

        // Guardar en RandomAccessFile
        Resultado resultado = new Resultado(
                0,
                ganador.getNombreEquipo(),
                ganador.getJugadores().get(0).getNombre(),
                ganador.getJugadores().get(1).getNombre(),
                ganador.getJugadores().get(2).getNombre(),
                puntajeFinal
        );
        resultadoDAO.guardarResultado(resultado);

        // Contar victorias históricas
        int victorias = resultadoDAO.contarVictorias(ganador.getNombreEquipo());

        // Serializar equipos al terminar
        serializadoDAO.guardarEquipos(equipos);

        final Equipo equipoGanador = ganador;
        final int totalVictorias = victorias;
        final int puntaje = puntajeFinal;

        SwingUtilities.invokeLater(()
                -> mostrarResultados(equipoGanador, puntaje, totalVictorias)
        );
    }

    // ──────────────────────────────────────────────────────────────────────────
    //  Resultados finales
    // ──────────────────────────────────────────────────────────────────────────
    /**
     * Abre la ventana de resultados con el equipo ganador.
     *
     * @param ganador Equipo ganador.
     * @param puntaje Puntaje total del ganador.
     * @param totalVictorias Victorias históricas del equipo ganador.
     */
    private void mostrarResultados(Equipo ganador, int puntaje, int totalVictorias) {
        VistaResultados vistaResultados = new VistaResultados(
                vista, ganador, equipos, totalVictorias);

        // Botón cerrar
        vistaResultados.setOnCerrar(() -> {
            vista.setBtnCargarHabilitado(true);
            vista.setBtnIniciarHabilitado(true);
            vista.setEstado("Juego terminado. Ganó: " + ganador.getNombreEquipo()
                    + " con " + puntaje + " pts.");
        });

        // Botón jugar de nuevo
        vistaResultados.setOnJugarDeNuevo(() -> {
            vista.setBtnCargarHabilitado(true);
            vista.setBtnIniciarHabilitado(true);
            vista.setEstado("¡Listos para otra partida!");
        });
    }

    // ──────────────────────────────────────────────────────────────────────────
    //  Utilidades
    // ──────────────────────────────────────────────────────────────────────────
    /**
     * Abre un JFileChooser para seleccionar un archivo con la extensión
     * indicada.
     *
     * @param titulo Título del diálogo.
     * @param extension Extensión del archivo a filtrar (sin punto).
     * @return Archivo seleccionado, o {@code null} si el usuario canceló.
     */
    private File abrirJFileChooser(String titulo, String extension) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle(titulo);
        chooser.setCurrentDirectory(new File(CARPETA_DATA));
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                extension.toUpperCase() + " files (*." + extension + ")", extension));
        int resultado = chooser.showOpenDialog(vista);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile();
        }
        return null;
    }
}
