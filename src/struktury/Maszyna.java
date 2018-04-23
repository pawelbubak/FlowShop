package struktury;

/**
 * Klasa reprezentująca maszynę na której wykonywane jest dane zadanie - zawiera liczbe zadań do niej przypisanych, kolejność wykonywanych zadań i czasy ich zakończenia
 */
public class Maszyna {
    private int liczbaZadan;
    private int[] zadania;
    private int[] czasyZakonczeniaZadan;

    public int getLiczbaZadan() {
        return liczbaZadan;
    }

    public void setLiczbaZadan(int liczbaZadan) {
        this.liczbaZadan = liczbaZadan;
    }

    public int[] getZadania() {
        return zadania;
    }

    public void setZadania(int[] zadania) {
        this.zadania = zadania;
    }

    public int[] getCzasyZakonczeniaZadan() {
        return czasyZakonczeniaZadan;
    }

    public void setCzasyZakonczeniaZadan(int[] czasyZakonczeniaZadan) {
        this.czasyZakonczeniaZadan = czasyZakonczeniaZadan;
    }

    public Maszyna(int[] zadania,int liczbaZadan) {
        this.zadania = zadania;
        this.liczbaZadan=liczbaZadan;
        this.czasyZakonczeniaZadan=new int[liczbaZadan];
    }

}
