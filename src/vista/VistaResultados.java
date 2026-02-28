package vista;

import Modelo.Equipo;
import Modelo.Jugador;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.List;

/**
 * Vista de resultados finales del juego del Balero. Muestra el equipo ganador,
 * sus jugadores, puntaje y victorias históricas. Incluye botones para jugar de
 * nuevo o cerrar la aplicación. Sigue el patrón MVC: solo se encarga de la
 * presentación.
 *
 * @author Julian - Andres - Miguel
 * @version 2.0
 */
public class VistaResultados extends JDialog {

    // ── Colores y fuentes ──────────────────────────────────────────────────────
    private static final Color COLOR_FONDO = new Color(15, 17, 26);
    private static final Color COLOR_PANEL = new Color(26, 30, 46);
    private static final Color COLOR_ACENTO = new Color(255, 180, 0);
    private static final Color COLOR_ACENTO2 = new Color(0, 210, 170);
    private static final Color COLOR_TEXTO = new Color(230, 235, 255);
    private static final Color COLOR_SUBTEXTO = new Color(130, 140, 180);
    private static final Color COLOR_BORDE = new Color(50, 58, 90);
    private static final Font FUENTE_TROFEO = new Font("Georgia", Font.BOLD, 48);
    private static final Font FUENTE_TITULO = new Font("Georgia", Font.BOLD, 22);
    private static final Font FUENTE_EQUIPO = new Font("Georgia", Font.BOLD, 18);
    private static final Font FUENTE_JUGADOR = new Font("Monospaced", Font.PLAIN, 13);
    private static final Font FUENTE_BOTON = new Font("Georgia", Font.BOLD, 14);
    private static final Font FUENTE_VICTORIAS = new Font("Georgia", Font.ITALIC, 14);

    // ── Callbacks ──────────────────────────────────────────────────────────────
    private Runnable onCerrar;
    private Runnable onJugarDeNuevo;

    // ──────────────────────────────────────────────────────────────────────────
    //  Constructor
    // ──────────────────────────────────────────────────────────────────────────
    /**
     * Construye y muestra la ventana de resultados.
     *
     * @param parent Ventana padre.
     * @param ganador Equipo ganador.
     * @param todosLosEquipos Lista completa de equipos con sus estadísticas.
     * @param totalVictorias Número total de victorias del equipo ganador.
     */
    public VistaResultados(JFrame parent, Equipo ganador,
            List<Equipo> todosLosEquipos, int totalVictorias) {
        super(parent, "Resultados Finales", true);
        configurarVentana();
        construirUI(ganador, todosLosEquipos, totalVictorias);
        setVisible(true);
    }

    // ──────────────────────────────────────────────────────────────────────────
    //  Configuración
    // ──────────────────────────────────────────────────────────────────────────
    /**
     * Configura las propiedades básicas del diálogo.
     */
    private void configurarVentana() {
        setSize(640, 720);
        setLocationRelativeTo(getOwner());
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(COLOR_FONDO);
        setLayout(new BorderLayout(0, 0));
    }

    // ──────────────────────────────────────────────────────────────────────────
    //  Construcción de UI
    // ──────────────────────────────────────────────────────────────────────────
    /**
     * Construye todos los paneles de la ventana.
     *
     * @param ganador Equipo ganador.
     * @param todosLosEquipos Lista de todos los equipos.
     * @param totalVictorias Victorias históricas del ganador.
     */
    private void construirUI(Equipo ganador, List<Equipo> todosLosEquipos,
            int totalVictorias) {
        add(construirEncabezado(), BorderLayout.NORTH);
        add(construirCentro(ganador, todosLosEquipos, totalVictorias), BorderLayout.CENTER);
        add(construirBotones(), BorderLayout.SOUTH);
    }

    /**
     * Construye el encabezado con trofeo y título.
     *
     * @return Panel del encabezado.
     */
    private JPanel construirEncabezado() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 0, 4)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(40, 35, 10),
                        0, getHeight(), COLOR_FONDO);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(24, 40, 16, 40));

        JLabel lblTrofeo = new JLabel("", SwingConstants.CENTER);
        lblTrofeo.setFont(FUENTE_TROFEO);

        JLabel lblTitulo = new JLabel("TENEMOS UN GANADOR", SwingConstants.CENTER);
        lblTitulo.setFont(FUENTE_TITULO);
        lblTitulo.setForeground(COLOR_ACENTO);

        JLabel lblSub = new JLabel("Resultados de la partida", SwingConstants.CENTER);
        lblSub.setFont(FUENTE_VICTORIAS);
        lblSub.setForeground(COLOR_SUBTEXTO);

        panel.add(lblTrofeo);
        panel.add(lblTitulo);
        panel.add(lblSub);
        return panel;
    }

    /**
     * Construye el panel central con tarjeta del ganador y tabla de resultados.
     *
     * @param ganador Equipo ganador.
     * @param todosLosEquipos Lista de todos los equipos.
     * @param totalVictorias Victorias históricas del ganador.
     * @return Panel central con scroll.
     */
    private JScrollPane construirCentro(Equipo ganador,
            List<Equipo> todosLosEquipos,
            int totalVictorias) {
        JPanel contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setOpaque(false);
        contenido.setBorder(new EmptyBorder(8, 32, 16, 32));

        contenido.add(construirTarjetaGanador(ganador, totalVictorias));
        contenido.add(Box.createVerticalStrut(20));
        contenido.add(construirTablaResultados(todosLosEquipos));

        JScrollPane scroll = new JScrollPane(contenido);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(12);
        return scroll;
    }

    /**
     * Construye la tarjeta visual del equipo ganador.
     *
     * @param ganador Equipo ganador.
     * @param totalVictorias Victorias históricas.
     * @return Panel tarjeta del ganador.
     */
    private JPanel construirTarjetaGanador(Equipo ganador, int totalVictorias) {
        JPanel tarjeta = new JPanel(new BorderLayout(0, 12)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(40, 36, 10));
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 16, 16));
                g2.setColor(COLOR_ACENTO);
                g2.setStroke(new BasicStroke(2f));
                g2.draw(new RoundRectangle2D.Float(1, 1, getWidth() - 2, getHeight() - 2, 16, 16));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        tarjeta.setOpaque(false);
        tarjeta.setBorder(new EmptyBorder(20, 24, 20, 24));
        tarjeta.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));

        JLabel lblNombre = new JLabel(ganador.getNombreEquipo(), SwingConstants.CENTER);
        lblNombre.setFont(FUENTE_EQUIPO);
        lblNombre.setForeground(COLOR_ACENTO);

        JLabel lblProyecto = new JLabel(ganador.getNombreProyectoCurricular(),
                SwingConstants.CENTER);
        lblProyecto.setFont(FUENTE_VICTORIAS);
        lblProyecto.setForeground(COLOR_SUBTEXTO);

        JPanel encabezado = new JPanel(new GridLayout(2, 1, 0, 4));
        encabezado.setOpaque(false);
        encabezado.add(lblNombre);
        encabezado.add(lblProyecto);

        JPanel panelJugadores = new JPanel(new GridLayout(1, 3, 12, 0));
        panelJugadores.setOpaque(false);
        for (Jugador j : ganador.getJugadores()) {
            panelJugadores.add(construirFichaJugador(j));
        }

        String textoVictorias = totalVictorias > 1
                ? " Este equipo ha ganado " + totalVictorias + " veces en total"
                : " ¡Primera victoria de este equipo!";
        JLabel lblVictorias = new JLabel(textoVictorias, SwingConstants.CENTER);
        lblVictorias.setFont(FUENTE_VICTORIAS);
        lblVictorias.setForeground(COLOR_ACENTO2);

        tarjeta.add(encabezado, BorderLayout.NORTH);
        tarjeta.add(panelJugadores, BorderLayout.CENTER);
        tarjeta.add(lblVictorias, BorderLayout.SOUTH);
        return tarjeta;
    }

    /**
     * Construye la tabla con los resultados de todos los equipos.
     *
     * @param equipos Lista de todos los equipos.
     * @return Panel con la tabla de resultados.
     */
    private JPanel construirTablaResultados(List<Equipo> equipos) {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400));

        JLabel lblTitulo = new JLabel("Resultados por equipo", SwingConstants.LEFT);
        lblTitulo.setFont(FUENTE_JUGADOR.deriveFont(Font.BOLD, 14f));
        lblTitulo.setForeground(COLOR_SUBTEXTO);
        lblTitulo.setBorder(new EmptyBorder(0, 0, 8, 0));
        panel.add(lblTitulo, BorderLayout.NORTH);

        String[] columnas = {"Equipo", "Jugador", "Intentos", "Exitosos", "Puntos"};
        JPanel tabla = new JPanel(new GridBagLayout());
        tabla.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(4, 8, 4, 8);

        for (int c = 0; c < columnas.length; c++) {
            gbc.gridx = c;
            gbc.gridy = 0;
            gbc.weightx = c == 0 || c == 1 ? 2.0 : 1.0;
            JLabel lbl = new JLabel(columnas[c], SwingConstants.CENTER);
            lbl.setFont(FUENTE_JUGADOR.deriveFont(Font.BOLD));
            lbl.setForeground(COLOR_ACENTO);
            tabla.add(lbl, gbc);
        }

        int fila = 1;
        for (Equipo equipo : equipos) {
            for (int j = 0; j < equipo.getJugadores().size(); j++) {
                Jugador jugador = equipo.getJugadores().get(j);
                String nombreEquipo = j == 0 ? equipo.getNombreEquipo() : "";
                Color colorFila = fila % 2 == 0
                        ? new Color(26, 30, 46) : new Color(22, 26, 40);
                String[] datos = {nombreEquipo, jugador.getNombre(),
                    String.valueOf(jugador.getIntentos()),
                    String.valueOf(jugador.getIntentosExitosos()),
                    String.valueOf(jugador.getPuntaje())};

                for (int c = 0; c < datos.length; c++) {
                    gbc.gridx = c;
                    gbc.gridy = fila;
                    gbc.weightx = c == 0 || c == 1 ? 2.0 : 1.0;
                    final Color cf = colorFila;
                    JLabel celda = new JLabel(datos[c], SwingConstants.CENTER) {
                        @Override
                        protected void paintComponent(Graphics g) {
                            g.setColor(cf);
                            g.fillRect(0, 0, getWidth(), getHeight());
                            super.paintComponent(g);
                        }
                    };
                    celda.setOpaque(false);
                    celda.setFont(FUENTE_JUGADOR);
                    celda.setForeground(COLOR_TEXTO);
                    tabla.add(celda, gbc);
                }
                fila++;
            }
        }
        panel.add(tabla, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Construye la ficha visual de un jugador para la tarjeta del ganador.
     *
     * @param jugador Jugador a mostrar.
     * @return Panel de la ficha del jugador.
     */
    private JPanel construirFichaJugador(Jugador jugador) {
        JPanel ficha = new JPanel(new BorderLayout(0, 6)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(30, 26, 8));
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        ficha.setOpaque(false);
        ficha.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel avatar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                int size = Math.min(getWidth(), getHeight()) - 8;
                int x = (getWidth() - size) / 2, y = (getHeight() - size) / 2;
                g2.setColor(new Color(60, 50, 0));
                g2.fillOval(x, y, size, size);
                g2.setColor(COLOR_ACENTO);
                g2.setStroke(new BasicStroke(2f));
                g2.drawOval(x, y, size, size);
                g2.setFont(new Font("Georgia", Font.BOLD, size / 2));
                g2.setColor(COLOR_ACENTO);
                String inicial = jugador.getNombre().isEmpty() ? "?"
                        : String.valueOf(jugador.getNombre().charAt(0)).toUpperCase();
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(inicial,
                        x + (size - fm.stringWidth(inicial)) / 2,
                        y + (size + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        avatar.setOpaque(false);
        avatar.setPreferredSize(new Dimension(60, 60));

        JLabel lblNombre = new JLabel(jugador.getNombre(), SwingConstants.CENTER);
        lblNombre.setFont(FUENTE_JUGADOR.deriveFont(Font.BOLD));
        lblNombre.setForeground(COLOR_TEXTO);

        JLabel lblPuntaje = new JLabel(jugador.getPuntaje() + " pts", SwingConstants.CENTER);
        lblPuntaje.setFont(FUENTE_JUGADOR);
        lblPuntaje.setForeground(COLOR_ACENTO);

        JLabel lblIntentos = new JLabel(
                jugador.getIntentosExitosos() + "/" + jugador.getIntentos(),
                SwingConstants.CENTER);
        lblIntentos.setFont(FUENTE_JUGADOR.deriveFont(11f));
        lblIntentos.setForeground(COLOR_SUBTEXTO);

        JPanel info = new JPanel(new GridLayout(3, 1, 0, 2));
        info.setOpaque(false);
        info.add(lblNombre);
        info.add(lblPuntaje);
        info.add(lblIntentos);

        ficha.add(avatar, BorderLayout.NORTH);
        ficha.add(info, BorderLayout.CENTER);
        return ficha;
    }

    /**
     * Construye el panel inferior con los botones de acción. Incluye "Jugar de
     * nuevo" y "Cerrar".
     *
     * @return Panel con los botones.
     */
    private JPanel construirBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 16));
        panel.setOpaque(false);

        JButton btnJugarDeNuevo = crearBoton("  Jugar de nuevo", COLOR_ACENTO2);
        JButton btnCerrar = crearBoton("  Cerrar", new Color(200, 80, 80));

        btnJugarDeNuevo.addActionListener(e -> {
            if (onJugarDeNuevo != null) {
                onJugarDeNuevo.run();
            }
            dispose();
        });

        btnCerrar.addActionListener(e -> {
            if (onCerrar != null) {
                onCerrar.run();
            }
            dispose();
        });

        panel.add(btnJugarDeNuevo);
        panel.add(btnCerrar);
        return panel;
    }

    /**
     * Crea un botón con estilo personalizado.
     *
     * @param texto Texto del botón.
     * @param color Color de acento.
     * @return Botón configurado.
     */
    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? color.brighter() : color);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                g2.setColor(Color.BLACK);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };
        btn.setFont(FUENTE_BOTON);
        btn.setPreferredSize(new Dimension(180, 42));
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    /**
     * Asigna la acción que se ejecuta al cerrar la ventana.
     *
     * @param onCerrar Runnable del controlador.
     */
    public void setOnCerrar(Runnable onCerrar) {
        this.onCerrar = onCerrar;
    }

    /**
     * Asigna la acción que se ejecuta al pulsar "Jugar de nuevo".
     *
     * @param onJugarDeNuevo Runnable del controlador.
     */
    public void setOnJugarDeNuevo(Runnable onJugarDeNuevo) {
        this.onJugarDeNuevo = onJugarDeNuevo;
    }
}
