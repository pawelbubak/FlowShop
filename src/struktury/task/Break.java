package struktury.task;

import java.io.Serializable;

/**
 * (
 * Klasa reprezentująca przerwy serwisowe maszyny
 */
public class Break extends Task implements Serializable {
    /**
     * Konstruktor przerwy serwisowej maszyny
     * @param rozpoczecie Czas ropoczęcia przerwy
     * @param dlugosc Czas trwania przerwy
     */
    public Break(int rozpoczecie, int dlugosc) {
        super(rozpoczecie, dlugosc);
    }
}
