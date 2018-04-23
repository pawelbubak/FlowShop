package struktury.task;

import java.io.Serializable;

/**
 * Klasa której roszszerzeniami są Zadanie i Break
 */
public class Task implements Serializable{
    private int rozpoczecie;
    private int dlugosc;

    public int getRozpoczecie() {
        return rozpoczecie;
    }

    public void setRozpoczecie(int rozpoczecie) {
        this.rozpoczecie = rozpoczecie;
    }

    public int getDlugosc() {
        return dlugosc;
    }

    public void setDlugosc(int dlugosc) {
        this.dlugosc = dlugosc;
    }

    public Task(int rozpoczecie, int dlugosc) {
        this.rozpoczecie = rozpoczecie;
        this.dlugosc = dlugosc;
    }

    public Task() {
    }

    @Override
    public String toString() {
        return "[" + rozpoczecie + ", " + dlugosc + "] ";
    }
}

