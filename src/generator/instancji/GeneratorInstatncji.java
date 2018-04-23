package generator.instancji;

import dane.OdczytZapis;
import struktury.Instancja;
import struktury.task.Break;
import struktury.task.Task;
import struktury.task.Zadanie;

import java.util.*;

/**
 * Klasa generująca instancje o zadanych parametrach
 */
public class GeneratorInstatncji {
    public static void main(String[] args) {
        generujInstancje((short) 10);
    }

    /**
     * Generuje instancje o zadanej wielkości zadań - PARAMETRY DLUGOSCI ZADAŃ I PRZERW ORAZ ILOŚĆ PZRERW NALEŻY ZMIENIĆ BEZPOŚREDNIO W KODZIE
     * @param rozmiar ilość zadań do wygenerowania
     */
    public static void generujInstancje(short rozmiar) {
        for (int h = 0; h < 1; h++) {
            for (int x = 0; x < 1; x++) {

                Random random = new Random();
                short liczbaPrzestojow = (short) 15;//(random.nextInt((int) Math.ceil(rozmiar / 1.5 - rozmiar / 5.0)) + Math.ceil(rozmiar / 5));
                Instancja instancja = new Instancja(rozmiar, liczbaPrzestojow);
                int czas = 0;
                for (int i = 0; i < Math.ceil(rozmiar); i++) {
                    short czasWykonania = (short) 160;//(random.nextInt(41-h*2) + 1);
                    czas += czasWykonania;
                    instancja.getZadaniaMaszyna1()[i] = new Zadanie(0, czasWykonania, i, false);
                }
            /*for (int i = (int) Math.ceil(rozmiar/2); i< rozmiar; i++) {
                short czasWykonania = (short) (random.nextInt(200) + 1);
                czas += czasWykonania;
                instancja.getZadaniaMaszyna1()[i] = new Zadanie(0, czasWykonania, i, false);
            }*/
                System.out.println(czas);

                for (Zadanie zadanie : instancja.getZadaniaMaszyna1()) {
                    zadanie.setRozpoczecie(random.nextInt(czas / 2));
                }
                for (int i = 0; i < Math.ceil(rozmiar); i++) {
                    short czasWykonania = (short) 160;//(random.nextInt(41-h*2) + 1);
                    Zadanie zadaniePierwsze = instancja.getZadaniaMaszyna1()[i];
                    instancja.getZadaniaMaszyna2()[i] = new Zadanie((int) (zadaniePierwsze.getRozpoczecie() + zadaniePierwsze.getDlugosc()), czasWykonania, i, false);
                }
            /*for (int i = (int) Math.ceil(rozmiar/2); i < rozmiar; i++) {
                short czasWykonania = (short) (random.nextInt(200) + 1);
                Zadanie zadaniePierwsze = instancja.getZadaniaMaszyna1()[i];
                instancja.getZadaniaMaszyna2()[i] = new Zadanie((int) (zadaniePierwsze.getRozpoczecie() + zadaniePierwsze.getDlugosc()), czasWykonania, i, false);
            }*/
                for (int i = 0; i < liczbaPrzestojow; i++) {
                    szukaj:
                    while (true) {
                        int czasRozpoczecia = random.nextInt(czas);
                        int dlugosc = random.nextInt(20) + 1;
                        for (int j = 0; j < i; j++) {
                            Break przerwa = instancja.getPrzerwy()[j];
                            if ((przerwa.getRozpoczecie() + przerwa.getDlugosc()) > czasRozpoczecia + dlugosc && przerwa.getRozpoczecie() < czasRozpoczecia + dlugosc) {
                                continue szukaj;
                            }
                            if (przerwa.getRozpoczecie() < czasRozpoczecia && (przerwa.getRozpoczecie() + przerwa.getDlugosc()) > czasRozpoczecia) {
                                continue szukaj;
                            }
                            if (czasRozpoczecia < przerwa.getRozpoczecie() && czasRozpoczecia + dlugosc > przerwa.getRozpoczecie() + przerwa.getDlugosc()) {
                                continue szukaj;
                            }
                        }
                        czas += dlugosc;
                        instancja.getPrzerwy()[i] = new Break(czasRozpoczecia, dlugosc);
                        break;
                    }
                }
                System.out.println(czas);
                ArrayList<Break> listaPrzerw = new ArrayList<>();
                listaPrzerw.addAll(Arrays.asList(instancja.getPrzerwy()));
                listaPrzerw.sort(Comparator.comparing(Task::getRozpoczecie));
                for (int i = 0; i < instancja.getLiczbaPrzerw(); i++) {
                    instancja.getPrzerwy()[i] = listaPrzerw.get(i);
                }
                //OdczytZapis.zapiszDane(instancja);
                OdczytZapis.zapiszDaneTekstowe(instancja, "instancje/stalaSumaDlugosciZadan/", 10);
                // instancja=OdczytZapis.wczytajDane();
           /* int min = 999999990;
            int max = 0;
            long suma = 0;
            for (int i = 0; i < 500000; i++) {
                Rozwiazanie rozwiazanie = GeneratorUszeregowania.generujRozwiaznieLosowe(instancja);
                rozwiazanie.przeliczCzasy(instancja);
                //System.out.println(rozwiazanie.getCzas());
                if (rozwiazanie.getCzas() > max) {
                    max = rozwiazanie.getCzas();
                }
                if (rozwiazanie.getCzas() < min) {
                    min = rozwiazanie.getCzas();
                }
                suma += rozwiazanie.getCzas();
                //System.out.println("zadania 1");
            /*
            for (int zadanie: rozwiazanie.getMaszyna1().getZadania()
                 ) {
                System.out.println(zadanie);
            }
            System.out.println("czasy 1");
            for (int czasy:rozwiazanie.getMaszyna1().getCzasyZakonczeniaZadan()
                 ) {
                System.out.println(czasy);

            }
            System.out.println("zadania 2");
            for (int zadanie: rozwiazanie.getMaszyna2().getZadania()
                    ) {
                System.out.println(zadanie);
            }
            System.out.println("czasy 2");
            for (int czasy:rozwiazanie.getMaszyna2().getCzasyZakonczeniaZadan()
                    ) {
                System.out.println(czasy);
            }
*/

            }
           /* System.out.println("srednia " + suma / 500000.0);
            System.out.println("zrobiono");
            System.out.println("max" + max);
            System.out.println("min" + min);
            //OdczytZapis.wczytajDane(instancja);

            for (int i = 0; i < rozmiar; i++) {
                System.out.print(instancja.getZadaniaMaszyna1()[i]);
                System.out.println(instancja.getZadaniaMaszyna2()[i]);
            }
            System.out.println("  Break ");
            for (int i = 0; i < liczbaPrzestojow; i++) {
                System.out.println(instancja.getPrzerwy()[i]);
            }

        }*/
        }
    }
}
