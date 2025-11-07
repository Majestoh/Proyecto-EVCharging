import java.util.Comparator;

/**
 * Comparador para {@link ChargingStation}.
 * Este comparador ordena las estaciones siguiendo dos criterios según los
 * requisitos establecidos.
 * 1. Criterio Principal: número de vehículos recargados, en orden DESCENDENTE.
 * 2. Criterio de Desempate: si el número de recargas es igual, se usa el ID de la estación
 * en orden CRECIENTE (alfabético).
 *
 * @author Pablo Carrasco Caballero
 * @version 2025
 */
public class ComparatorChargingStationNumberRecharged implements Comparator<ChargingStation> {
    /**
     * Comparamos dos objetos ChargingStation para establecer un orden.
     * Devolvemos -1 si el primer objeto debe ir ANTES que el segundo.
     * Devolvemos 1 si el primer objet odebe ir DESPUÉS que el segundo.
     * Devolvemos 0 si se consideran iguales.
     *
     * @param st1 La primera estación a comparar.
     * @param st2 La segunda estácion a comparar.
     * @return -1, 0 o 1, según el orden establecido.
     */
    public int compare(ChargingStation st1, ChargingStation st2) {
        ChargingStation estacion1 = st1;
        ChargingStation estacion2 = st2;

        int recargas1 = estacion1.getNumerEVRecharged();
        int recargas2 = estacion2.getNumerEVRecharged();

        // --- Criterio 1: Orden DESCENDENTE por número de recargas ---
        if (recargas1 > recargas2) {
            return -1;
        } else if (recargas1 < recargas2) {
            return 1;
        } else {
            // --- Criterio 2: Desempate por ID ---
            ComparatorChargingStationId comparadorPorId = new ComparatorChargingStationId();
            return comparadorPorId.compare(estacion1, estacion2);
        }
    }
}