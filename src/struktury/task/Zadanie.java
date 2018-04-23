package struktury.task;

import java.io.Serializable;

/**
 * Klasa reprezentująca zadanie wykonywane na maszynie
 */
public class Zadanie extends Task implements Serializable {
    private int id;
    private boolean wykonane;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isWykonane() {
        return wykonane;
    }

    public void setWykonane(boolean wykonane) {
        this.wykonane = wykonane;
    }

    /**
     * Konstruktor zadania - dłuższy
     * @param czasRozpoczecia Czas możliwości ropoczęcia wykonywania zadania
     * @param dlugosc Czas wykonywania zadania
     * @param id Identyfikator zadania
     * @param wykonane Wartość reprezentująca czy zadanie zostało wykonane czy nie
     */
    public Zadanie(int czasRozpoczecia, int dlugosc, int id, boolean wykonane) {
        super(czasRozpoczecia, dlugosc);
        this.id = id;
        this.wykonane = wykonane;
    }

    /**
     * Konstruktor zadania
     * @param id Identyfikator zadania
     * @param wykonane Wartość reprezentująca czy zadanie zostało wykonane czy nie
     */
    public Zadanie(int id, boolean wykonane) {
        this.id = id;
        this.wykonane = wykonane;
    }
}
