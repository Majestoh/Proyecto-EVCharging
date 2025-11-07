import java.util.Comparator;

/**
 * Comparador para {@link ChargingStation}.
 * Compara dos estaciones basándose en su ID en orden CRECIENTE.
 * Se utiliza para ordenar las estaciones en la información inicial, y como
 * criterio de desempate para la información final.
 *
 * @author Pablo Carrasco Caballero
 * @version 2025
 */
public class ComparatorChargingStationId {
    /**
     * Compara dos objetos ChargingStation para establecer un orden.
     * La comparación se basa en el orden alfabético de sus IDs.
     *
     * @param st1 La primera estación a comparar
     * @param st2 La segunda estación a comparar
     * @return Un entero negativo, cero, o un entero positivo si el ID de la primera
     * estación es, respectivamente, menor, igual o mayor que el ID de la segunda.
     */
    public int compare(ChargingStation st1, ChargingStation st2) {
        ChargingStation estacion1 = st1;
        ChargingStation estacion2 = st2;

        // Utilizamos compareTo() para comparar alfabéticamente en orden
        // ascendente.
        // Ejemplo:
        // "CC00".compareTo("CC01") --> devuelve un -1.
        return estacion1.getId().compareTo(estacion2.getId());
    }
}