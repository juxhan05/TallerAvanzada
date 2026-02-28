package Modelo;

import java.util.EnumMap;
import java.util.Map;

/**
 * Clase responsable de gestionar la lógica de puntajes asociados
 * a cada tipo de {@link Embocada}.
 * Separa la lógica de negocio del enum, respetando el
 * Principio de Responsabilidad Única (SRP) de SOLID.
 * Al usar un {@code EnumMap} interno, también respeta el
 * Principio Abierto/Cerrado (OCP): agregar nuevos tipos de embocada
 * no requiere modificar esta clase, solo registrar el nuevo puntaje.
 *
 * @author Julian - Andres - Miguel
 * @version 2.0
 */
public class PuntajeEmbocada {

    /** Mapa que asocia cada tipo de embocada con su puntaje. */
    private static final Map<Embocada, Integer> PUNTAJES = new EnumMap<>(Embocada.class);

    static {
        PUNTAJES.put(Embocada.SIMPLE,        2);
        PUNTAJES.put(Embocada.DOBLE,        10);
        PUNTAJES.put(Embocada.VERTICAL,      3);
        PUNTAJES.put(Embocada.MARIQUITA,     4);
        PUNTAJES.put(Embocada.PUÑALADA,      5);
        PUNTAJES.put(Embocada.PURTIÑA,       6);
        PUNTAJES.put(Embocada.DOMINIO_REVES, 8);
        PUNTAJES.put(Embocada.FALLA,         0);
    }

    /**
     * Retorna el puntaje asociado al tipo de embocada indicado.
     *
     * @param embocada Tipo de embocada del que se quiere conocer el puntaje.
     * @return Puntaje correspondiente. Retorna 0 si la embocada no tiene puntaje definido.
     */
    public static int getPuntaje(Embocada embocada) {
        return PUNTAJES.getOrDefault(embocada, 0);
    }

    /**
     * Verifica si una embocada es exitosa (distinta de FALLA).
     *
     * @param embocada Tipo de embocada a verificar.
     * @return {@code true} si la embocada es exitosa, {@code false} si es FALLA.
     */
    public static boolean esExitosa(Embocada embocada) {
        return embocada != Embocada.FALLA;
    }
}