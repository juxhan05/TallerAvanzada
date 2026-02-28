package Modelo;

/**
 * Enumeración que define los tipos de embocada posibles en el juego del Balero.
 * Solo contiene las constantes de tipo, sin lógica de puntaje. La lógica de
 * puntaje está delegada a {@link PuntajeEmbocada},
 *
 * @author Julian - Andres - Miguel
 * @version 2.0
 */
public enum Embocada {

    /**
     * Emboque básico y directo en el primer intento.
     */
    SIMPLE,
    /**
     * Dos giros o movimientos técnicos antes de encajar la pieza.
     */
    DOBLE,
    /**
     * Técnica estándar donde la pieza sube recta y se encaja.
     */
    VERTICAL,
    /**
     * Técnica con giro o balanceo particular.
     */
    MARIQUITA,
    /**
     * Emboque rápido y seco, directo y contundente.
     */
    PUÑALADA,
    /**
     * Variante técnica tradicional para atrapar la bola.
     */
    PURTIÑA,
    /**
     * Emboque con la mano en posición invertida o girando el palo al revés.
     */
    DOMINIO_REVES,
    /**
     * Intento fallido, sin embocada exitosa.
     */
    FALLA
}
