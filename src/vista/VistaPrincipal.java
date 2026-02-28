package vista;

import Modelo.Equipo;
import Modelo.Jugador;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Vista principal del juego del Balero. Muestra la grilla de equipos, sus
 * jugadores y controla el inicio del juego. Sigue el patrón MVC: solo se
 * encarga de la presentación y delega acciones al controlador.
 *
 * @author Julian - Andres - Miguel
 * @version 2.0
 */
public class VistaPrincipal extends JFrame {

    // ── Colores y fuentes ──────────────────────────────────────────────────────
    private static final Color COLOR_FONDO = new Color(15, 17, 26);
    private static final Color COLOR_PANEL_EQUIPO = new Color(26, 30, 46);
    private static final Color COLOR_ACENTO = new Color(255, 180, 0);
    private static final Color COLOR_ACENTO2 = new Color(0, 210, 170);
    private static final Color COLOR_TEXTO = new Color(230, 235, 255);
    private static final Color COLOR_SUBTEXTO = new Color(130, 140, 180);
    private static final Color COLOR_BORDE = new Color(50, 58, 90);
    private static final Font FUENTE_TITULO = new Font("Georgia", Font.BOLD, 28);
    private static final Font FUENTE_EQUIPO = new Font("Georgia", Font.BOLD, 16);
    private static final Font FUENTE_JUGADOR = new Font("Monospaced", Font.PLAIN, 12);
    private static final Font FUENTE_BOTON = new Font("Georgia", Font.BOLD, 14);
    private static final Font FUENTE_SUBTITULO = new Font("Georgia", Font.ITALIC, 13);

    // ── Componentes principales ────────────────────────────────────────────────
    private JPanel panelGrilla;
    private JButton btnCargar;
    private JButton btnIniciar;
    private JButton btnSalir;
    private JLabel lblTiempo;
    private JSpinner spinnerTiempo;
    private JLabel lblEstado;

    /**
     * Paneles de cada equipo para poder modificar su opacidad
     */
    private List<PanelEquipo> panelesEquipo = new ArrayList<>();

    /**
     * Índice del equipo que está jugando actualmente (-1 = nadie)
     */
    private int equipoActual = -1;

    // ── Listeners (callbacks hacia el controlador) ─────────────────────────────
    private Runnable onCargar;
    private Runnable onIniciar;
    private Runnable onSalir;

    // ──────────────────────────────────────────────────────────────────────────
    //  Constructor
    // ──────────────────────────────────────────────────────────────────────────
    /**
     * Construye y muestra la ventana principal.
     */
    public VistaPrincipal() {
        configurarVentana();
        construirUI();
        setVisible(true);
    }

    // ──────────────────────────────────────────────────────────────────────────
    //  Configuración de ventana
    // ──────────────────────────────────────────────────────────────────────────
    /**
     * Configura las propiedades básicas del JFrame.
     */
    private void configurarVentana() {
        setTitle(" El Juego del Balero — Universidad Distrital");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setMinimumSize(new Dimension(960, 680));
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_FONDO);
        setLayout(new BorderLayout(0, 0));

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (onSalir != null) {
                    onSalir.run();
                }
            }
        });
    }

    // ──────────────────────────────────────────────────────────────────────────
    //  Construcción de la interfaz
    // ──────────────────────────────────────────────────────────────────────────
    /**
     * Construye todos los paneles y los añade al frame.
     */
    private void construirUI() {
        add(construirEncabezado(), BorderLayout.NORTH);
        add(construirCentro(), BorderLayout.CENTER);
        add(construirBarraInferior(), BorderLayout.SOUTH);
    }

    /**
     * Construye el encabezado con título y decoración.
     *
     * @return Panel del encabezado.
     */
    private JPanel construirEncabezado() {
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                // Degradado en el encabezado
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(30, 36, 60),
                        getWidth(), 0, new Color(15, 17, 26));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());

                // Línea dorada inferior
                g2.setColor(COLOR_ACENTO);
                g2.setStroke(new BasicStroke(2f));
                g2.drawLine(40, getHeight() - 1, getWidth() - 40, getHeight() - 1);
            }
        };
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(24, 40, 20, 40));

        // Título principal
        JLabel titulo = new JLabel("EL BALERO", SwingConstants.CENTER);
        titulo.setFont(FUENTE_TITULO);
        titulo.setForeground(COLOR_ACENTO);

        // Subtítulo
        JLabel sub = new JLabel(
                "Programación Avanzada · Universidad Distrital Francisco José de Caldas",
                SwingConstants.CENTER);
        sub.setFont(FUENTE_SUBTITULO);
        sub.setForeground(COLOR_SUBTEXTO);

        JPanel textos = new JPanel(new GridLayout(2, 1, 0, 4));
        textos.setOpaque(false);
        textos.add(titulo);
        textos.add(sub);
        panel.add(textos, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Construye el panel central con la grilla de equipos y los controles de
     * juego.
     *
     * @return Panel central.
     */
    private JPanel construirCentro() {
        JPanel centro = new JPanel(new BorderLayout(0, 16));
        centro.setOpaque(false);
        centro.setBorder(new EmptyBorder(24, 40, 8, 40));

        // Panel de configuración (tiempo)
        centro.add(construirPanelConfig(), BorderLayout.NORTH);

        // Grilla de equipos
        panelGrilla = new JPanel();
        panelGrilla.setLayout(new GridLayout(1, 2, 20, 0));
        panelGrilla.setOpaque(false);
        centro.add(panelGrilla, BorderLayout.CENTER);

        // Etiqueta de estado
        lblEstado = new JLabel("Carga los equipos para comenzar.", SwingConstants.CENTER);
        lblEstado.setFont(FUENTE_SUBTITULO);
        lblEstado.setForeground(COLOR_SUBTEXTO);
        lblEstado.setBorder(new EmptyBorder(8, 0, 0, 0));
        centro.add(lblEstado, BorderLayout.SOUTH);

        return centro;
    }

    /**
     * Construye el panel de configuración del tiempo de juego.
     *
     * @return Panel de configuración.
     */
    private JPanel construirPanelConfig() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 0));
        panel.setOpaque(false);

        lblTiempo = new JLabel("Tiempo por equipo (segundos):");
        lblTiempo.setFont(FUENTE_JUGADOR);
        lblTiempo.setForeground(COLOR_TEXTO);

        spinnerTiempo = new JSpinner(new SpinnerNumberModel(30, 10, 300, 5));
        spinnerTiempo.setPreferredSize(new Dimension(70, 30));
        spinnerTiempo.setFont(FUENTE_JUGADOR);
        ((JSpinner.DefaultEditor) spinnerTiempo.getEditor())
                .getTextField().setBackground(new Color(36, 42, 66));
        ((JSpinner.DefaultEditor) spinnerTiempo.getEditor())
                .getTextField().setForeground(COLOR_ACENTO);

        panel.add(lblTiempo);
        panel.add(spinnerTiempo);

        return panel;
    }

    /**
     * Construye la barra inferior con los botones de acción.
     *
     * @return Panel de la barra inferior.
     */
    private JPanel construirBarraInferior() {
        JPanel barra = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 16)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(COLOR_BORDE);
                g.drawLine(40, 0, getWidth() - 40, 0);
            }
        };
        barra.setOpaque(false);

        btnCargar = crearBoton("  Cargar Equipos", COLOR_ACENTO2);
        btnIniciar = crearBoton("  Iniciar Juego", COLOR_ACENTO);
        btnSalir = crearBoton(" Salir", new Color(200, 80, 80));

        btnIniciar.setEnabled(false);

        btnCargar.addActionListener(e -> {
            if (onCargar != null) {
                onCargar.run();
            }
        });
        btnIniciar.addActionListener(e -> {
            if (onIniciar != null) {
                onIniciar.run();
            }
        });
        btnSalir.addActionListener(e -> {
            if (onSalir != null) {
                onSalir.run();
            }
        });

        barra.add(btnCargar);
        barra.add(btnIniciar);
        barra.add(btnSalir);

        return barra;
    }

    /**
     * Crea un botón con estilo personalizado.
     *
     * @param texto Texto del botón.
     * @param color Color de acento del botón.
     * @return Botón configurado.
     */
    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2.setColor(color.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(color.brighter());
                } else {
                    g2.setColor(isEnabled() ? color : color.darker().darker());
                }
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                g2.setColor(isEnabled() ? Color.BLACK : new Color(80, 80, 80));
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

    // ──────────────────────────────────────────────────────────────────────────
    //  API pública — llamada desde el controlador
    // ──────────────────────────────────────────────────────────────────────────
    /**
     * Carga la lista de equipos en la grilla visual. Reemplaza cualquier
     * contenido anterior.
     *
     * @param equipos Lista de equipos a mostrar.
     */
    public void mostrarEquipos(List<Equipo> equipos) {
        panelGrilla.removeAll();
        panelesEquipo.clear();

        for (Equipo equipo : equipos) {
            PanelEquipo pe = new PanelEquipo(equipo);
            panelesEquipo.add(pe);
            panelGrilla.add(pe);
        }

        btnIniciar.setEnabled(true);
        setEstado("Equipos cargados. Listos para jugar");
        panelGrilla.revalidate();
        panelGrilla.repaint();
    }

    /**
     * Resalta el equipo que está jugando actualmente y pone los demás en marca
     * de agua.
     *
     * @param indiceEquipo Índice del equipo en turno (0-based).
     */
    public void resaltarEquipo(int indiceEquipo) {
        this.equipoActual = indiceEquipo;
        for (int i = 0; i < panelesEquipo.size(); i++) {
            panelesEquipo.get(i).setMarcaAgua(i != indiceEquipo);
        }
    }

    /**
     * Resalta el jugador en turno dentro del equipo activo. Los demás jugadores
     * del equipo se ven más claros.
     *
     * @param indiceJugador Índice del jugador en turno (0, 1 o 2).
     */
    public void resaltarJugador(int indiceJugador) {
        if (equipoActual >= 0 && equipoActual < panelesEquipo.size()) {
            panelesEquipo.get(equipoActual).resaltarJugador(indiceJugador);
        }
    }

    /**
     * Actualiza el texto de estado en la parte inferior de la grilla.
     *
     * @param mensaje Mensaje a mostrar.
     */
    public void setEstado(String mensaje) {
        lblEstado.setText(mensaje);
    }

    /**
     * Devuelve el tiempo configurado en el spinner.
     *
     * @return Tiempo en segundos.
     */
    public int getTiempoSeleccionado() {
        return (int) spinnerTiempo.getValue();
    }

    /**
     * Habilita o deshabilita el botón de iniciar juego.
     *
     * @param habilitado {@code true} para habilitar.
     */
    public void setBtnIniciarHabilitado(boolean habilitado) {
        btnIniciar.setEnabled(habilitado);
    }

    /**
     * Habilita o deshabilita el botón de cargar equipos.
     *
     * @param habilitado {@code true} para habilitar.
     */
    public void setBtnCargarHabilitado(boolean habilitado) {
        btnCargar.setEnabled(habilitado);
    }

    // ── Setters de listeners (inyección desde el controlador) ─────────────────
    /**
     * Asigna la acción para el botón "Cargar Equipos".
     *
     * @param onCargar Runnable que ejecuta el controlador.
     */
    public void setOnCargar(Runnable onCargar) {
        this.onCargar = onCargar;
    }

    /**
     * Asigna la acción para el botón "Iniciar Juego".
     *
     * @param onIniciar Runnable que ejecuta el controlador.
     */
    public void setOnIniciar(Runnable onIniciar) {
        this.onIniciar = onIniciar;
    }

    /**
     * Asigna la acción para el botón "Salir".
     *
     * @param onSalir Runnable que ejecuta el controlador.
     */
    public void setOnSalir(Runnable onSalir) {
        this.onSalir = onSalir;
    }

    /**
     * Panel visual que representa a un equipo con sus tres jugadores. Soporta
     * el efecto de marca de agua y resalte del jugador en turno.
     */
    private class PanelEquipo extends JPanel {

        private final Equipo equipo;
        private boolean marcaAgua = false;
        private int jugadorResaltado = -1;

        private final List<PanelJugador> panelesJugador = new ArrayList<>();

        /**
         * Construye el panel para el equipo dado.
         *
         * @param equipo Equipo a representar.
         */
        PanelEquipo(Equipo equipo) {
            this.equipo = equipo;
            setOpaque(false);
            setLayout(new BorderLayout(0, 12));
            setBorder(new EmptyBorder(16, 16, 16, 16));
            construir();
        }

        /**
         * Construye los subcomponentes del panel.
         */
        private void construir() {
            // Encabezado del equipo
            JPanel encabezado = new JPanel(new GridLayout(2, 1, 0, 2));
            encabezado.setOpaque(false);

            JLabel lblNombreEquipo = new JLabel(equipo.getNombreEquipo(), SwingConstants.CENTER);
            lblNombreEquipo.setFont(FUENTE_EQUIPO);
            lblNombreEquipo.setForeground(COLOR_ACENTO);

            JLabel lblProyecto = new JLabel(equipo.getNombreProyectoCurricular(), SwingConstants.CENTER);
            lblProyecto.setFont(FUENTE_SUBTITULO);
            lblProyecto.setForeground(COLOR_SUBTEXTO);

            encabezado.add(lblNombreEquipo);
            encabezado.add(lblProyecto);
            add(encabezado, BorderLayout.NORTH);

            // Jugadores
            JPanel panelJugadores = new JPanel(new GridLayout(1, 3, 10, 0));
            panelJugadores.setOpaque(false);

            for (Jugador jugador : equipo.getJugadores()) {
                PanelJugador pj = new PanelJugador(jugador);
                panelesJugador.add(pj);
                panelJugadores.add(pj);
            }
            add(panelJugadores, BorderLayout.CENTER);
        }

        /**
         * Activa o desactiva el efecto de marca de agua (translúcido).
         *
         * @param marcaAgua {@code true} para mostrar translúcido.
         */
        void setMarcaAgua(boolean marcaAgua) {
            this.marcaAgua = marcaAgua;
            repaint();
        }

        /**
         * Resalta el jugador indicado y opaca los demás.
         *
         * @param indice Índice del jugador a resaltar (0-2).
         */
        void resaltarJugador(int indice) {
            this.jugadorResaltado = indice;
            for (int i = 0; i < panelesJugador.size(); i++) {
                panelesJugador.get(i).setResaltado(i == indice);
            }
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            // Fondo del panel
            float alpha = marcaAgua ? 0.28f : 1.0f;
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

            g2.setColor(COLOR_PANEL_EQUIPO);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 16, 16));

            // Borde
            g2.setColor(marcaAgua ? COLOR_BORDE : COLOR_ACENTO.darker());
            g2.setStroke(new BasicStroke(1.5f));
            g2.draw(new RoundRectangle2D.Float(1, 1, getWidth() - 2, getHeight() - 2, 16, 16));

            g2.dispose();
            super.paintComponent(g);
        }
    }

    // ──────────────────────────────────────────────────────────────────────────
    //  Clase interna: Panel de un jugador
    // ──────────────────────────────────────────────────────────────────────────
    /**
     * Panel visual que representa a un jugador individual con su imagen y
     * datos.
     */
    private class PanelJugador extends JPanel {

        private final Jugador jugador;
        private boolean resaltado = false;
        private ImageIcon iconoJugador;

        /**
         * Construye el panel para el jugador dado.
         *
         * @param jugador Jugador a representar.
         */
        PanelJugador(Jugador jugador) {
            this.jugador = jugador;
            setOpaque(false);
            setLayout(new BorderLayout(0, 6));
            setBorder(new EmptyBorder(8, 8, 8, 8));
            cargarImagen();
            construir();
        }

        /**
         * Intenta cargar la imagen del jugador desde el classpath. Si no
         * existe, usa un ícono por defecto generado.
         */
        private void cargarImagen() {
            // Intenta cargar imagen por código de estudiante
            String ruta = "/imagenes/jugador_" + jugador.getCodigo() + ".png";
            java.net.URL url = getClass().getResource(ruta);
            if (url != null) {
                ImageIcon raw = new ImageIcon(url);
                Image img = raw.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                iconoJugador = new ImageIcon(img);
            } else {
                // Ícono genérico: círculo con inicial
                iconoJugador = null;
            }
        }

        /**
         * Construye los subcomponentes del panel del jugador.
         */
        private void construir() {
            // Imagen o avatar
            JPanel lblImagen = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);

                    int size = Math.min(getWidth(), getHeight()) - 8;
                    int x = (getWidth() - size) / 2;
                    int y = (getHeight() - size) / 2;

                    if (iconoJugador != null) {
                        // Clip circular
                        g2.setClip(new java.awt.geom.Ellipse2D.Float(x, y, size, size));
                        g2.drawImage(iconoJugador.getImage(), x, y, size, size, null);
                        g2.setClip(null);
                    } else {
                        // Avatar generado
                        Color avatarColor = resaltado ? COLOR_ACENTO : COLOR_ACENTO2;
                        g2.setColor(resaltado ? new Color(60, 50, 0) : new Color(0, 50, 45));
                        g2.fillOval(x, y, size, size);
                        g2.setColor(avatarColor);
                        g2.setStroke(new BasicStroke(2f));
                        g2.drawOval(x, y, size, size);

                        // Inicial del nombre
                        g2.setFont(new Font("Georgia", Font.BOLD, size / 2));
                        g2.setColor(avatarColor);
                        String inicial = jugador.getNombre().isEmpty() ? "?"
                                : String.valueOf(jugador.getNombre().charAt(0)).toUpperCase();
                        FontMetrics fm = g2.getFontMetrics();
                        g2.drawString(inicial,
                                x + (size - fm.stringWidth(inicial)) / 2,
                                y + (size + fm.getAscent() - fm.getDescent()) / 2);
                    }

                    // Halo si está resaltado
                    if (resaltado) {
                        g2.setColor(new Color(255, 180, 0, 60));
                        g2.setStroke(new BasicStroke(4f));
                        g2.drawOval(x - 2, y - 2, size + 4, size + 4);
                    }

                    g2.dispose();
                }
            };
            lblImagen.setOpaque(false);
            lblImagen.setPreferredSize(new Dimension(90, 90));

            // Nombre y código
            JLabel lblNombre = new JLabel(jugador.getNombre(), SwingConstants.CENTER);
            lblNombre.setFont(FUENTE_JUGADOR.deriveFont(Font.BOLD));
            lblNombre.setForeground(COLOR_TEXTO);

            JLabel lblCodigo = new JLabel(jugador.getCodigo(), SwingConstants.CENTER);
            lblCodigo.setFont(FUENTE_JUGADOR.deriveFont(10f));
            lblCodigo.setForeground(COLOR_SUBTEXTO);

            JPanel textos = new JPanel(new GridLayout(2, 1, 0, 2));
            textos.setOpaque(false);
            textos.add(lblNombre);
            textos.add(lblCodigo);

            add(lblImagen, BorderLayout.CENTER);
            add(textos, BorderLayout.SOUTH);
        }

        /**
         * Activa o desactiva el resalte visual del jugador en turno.
         *
         * @param resaltado {@code true} para resaltar.
         */
        void setResaltado(boolean resaltado) {
            this.resaltado = resaltado;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (resaltado) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(60, 50, 10, 120));
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                g2.dispose();
            }
            super.paintComponent(g);
        }
    }
}
