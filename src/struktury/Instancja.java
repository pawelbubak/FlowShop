package struktury;

import struktury.task.Break;
import struktury.task.Zadanie;

import java.io.Serializable;

/**
 * Klasa reprezentująca przykładową instancję zadań do uszeregowania na 2 maszynach
 */
public class Instancja implements Serializable{
    private  short liczbaZadan;
    private short liczbaPrzerw;
    private Zadanie[] zadaniaMaszyna1;
    private Zadanie[] zadaniaMaszyna2;
    private Break[] przerwy;

    /**
     * Konstruktor instancji
     * @param liczbaZadan Liczba zadań
     * @param liczbaPrzerw Liczba przerw
     */
    public Instancja(short liczbaZadan, short liczbaPrzerw) {
        this.liczbaZadan = liczbaZadan;
        this.liczbaPrzerw = liczbaPrzerw;
        this.zadaniaMaszyna1 = new Zadanie[liczbaZadan];
        this.zadaniaMaszyna2 = new Zadanie[liczbaZadan];
        this.przerwy = new Break[liczbaPrzerw];
    }

    public Instancja() {}

    public short getLiczbaZadan() {
        return liczbaZadan;
    }

    public void setLiczbaZadan(short liczbaZadan) {
        this.liczbaZadan = liczbaZadan;
    }

    public Zadanie[] getZadaniaMaszyna1() {
        return zadaniaMaszyna1;
    }

    public void setZadaniaMaszyna1(Zadanie[] zadaniaMaszyna1) {
        this.zadaniaMaszyna1 = zadaniaMaszyna1;
    }

    public Zadanie[] getZadaniaMaszyna2() {
        return zadaniaMaszyna2;
    }

    public void setZadaniaMaszyna2(Zadanie[] zadaniaMaszyna2) {
        this.zadaniaMaszyna2 = zadaniaMaszyna2;
    }

    public Break[] getPrzerwy() {
        return przerwy;
    }

    public void setPrzerwy(Break[] przerwy) {
        this.przerwy = przerwy;
    }

    public short getLiczbaPrzerw() {
        return liczbaPrzerw;
    }

    public void setLiczbaPrzerw(short liczbaPrzerw) {
        this.liczbaPrzerw = liczbaPrzerw;
    }
}
