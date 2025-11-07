import java.util.Comparator;

/**
 * Comparador para {@link Charger}.
 * Compara dos cargadores basándose en su ID (String) en orden CRECIENTE.
 * Se utiliza como último criterio de desempate en ordenaciones
 * complejas, como la utilizada en {@link ChargingStation}.
 *
 * @author Pablo Carrasco Caballero
 * @version 2025
 */
public class ComparatorChargersId {
    /**
     * Compara dos objetos Charger para establecer un orden.
     * La comparación se basa en el orden alfabético de sus IDs.
     *
     * @param c1 El primer cargador a comparar.
     * @param c2 El segundo cargador a comparar.
     * @return Un entero negativo, cero, o un entero positivo si el ID del primer
     * cargador es, respectivamente, menor, igual o mayor que el ID del
     * segundo cargador.
     */
    public int compare(Charger c1, Charger c2) {
        Charger cargador1 = c1;
        Charger cargador2 = c2;

        // Utilizaremos CompareTo() para comparar dos cadenas alfabéticamente
        // en orden ascendente.
        // Ejemplos:
        // "CC01_01".compareTo("CC01_02") --> devolvería un -1.
        // "CC01_02".compareTo("CC01_01") --> devolvería un 1.
        // "CC01_01".compareTo("CC01_01") --> devolvería un 0.
        return cargador1.getId().compareTo(cargador2.getId());
    }
}