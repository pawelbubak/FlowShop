package generator.uszeregowania;

import main.Genetyczny;
import struktury.Instancja;
import struktury.Maszyna;
import struktury.Rozwiazanie;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Klasa generująca nowe rozwiązania zalęznie od wybranej opcji
 */
public class GeneratorUszeregowania {
    /**
     * Generuje rozwiązanie losowe dla zadanej instancji
     * @param instancja Instancja
     * @return wygenerowane rozwiązanie
     */
    public static Rozwiazanie generujRozwiaznieLosowe(Instancja instancja) {
        int liczbaZadan = instancja.getLiczbaZadan();
        int[] zadaniaMaszyna1 = new int[liczbaZadan];
        List<Integer> list = IntStream.range(0, liczbaZadan).boxed().collect(Collectors.toList()); //Tworzy liste koljenych liczb
        Collections.shuffle(list);
        for (int i = 0; i < liczbaZadan; i++) {
            zadaniaMaszyna1[i] = list.get(i);
        }
        int[] zadaniaMaszyna2 = new int[liczbaZadan];
        Collections.shuffle(list);
        for (int i = 0; i < liczbaZadan; i++) {
            zadaniaMaszyna2[i] = list.get(i);
        }
        Maszyna maszyna1 = new Maszyna(zadaniaMaszyna1, liczbaZadan);
        Maszyna maszyna2 = new Maszyna(zadaniaMaszyna2, liczbaZadan);
        return new Rozwiazanie(maszyna1, maszyna2);
    }

    /**
     * Krzyżownaie dwóch wczęsniej wybranych rozwiązań
     * @param rozwiazanie1 Rozwiązanie pierwsze
     * @param rozwiazanie2 Rozwiązanie drugie
     */
    public static void crossOver(Rozwiazanie rozwiazanie1, Rozwiazanie rozwiazanie2) {
        int rozmiar = rozwiazanie1.getMaszyna1().getLiczbaZadan();
        Random random = new Random();
        int miejsce = random.nextInt(rozmiar - 4) + 2;
        Rozwiazanie nowe1 = new Rozwiazanie(new Maszyna(new int[rozmiar], rozmiar), new Maszyna(new int[rozmiar], rozmiar));
        Rozwiazanie nowe2 = new Rozwiazanie(new Maszyna(new int[rozmiar], rozmiar), new Maszyna(new int[rozmiar], rozmiar));


        for (int i = 0; i < miejsce; i++) {
            nowe1.getMaszyna1().getZadania()[i] = rozwiazanie2.getMaszyna1().getZadania()[i];
            nowe1.getMaszyna2().getZadania()[i] = rozwiazanie2.getMaszyna2().getZadania()[i];
            nowe2.getMaszyna1().getZadania()[i] = rozwiazanie1.getMaszyna1().getZadania()[i];
            nowe2.getMaszyna2().getZadania()[i] = rozwiazanie1.getMaszyna2().getZadania()[i];
        }

        krzyzowanie(rozwiazanie1.getMaszyna1(), rozmiar, miejsce, nowe1.getMaszyna1());
        krzyzowanie(rozwiazanie1.getMaszyna2(), rozmiar, miejsce, nowe1.getMaszyna2());
        krzyzowanie(rozwiazanie2.getMaszyna1(), rozmiar, miejsce, nowe2.getMaszyna1());
        krzyzowanie(rozwiazanie2.getMaszyna2(), rozmiar, miejsce, nowe2.getMaszyna2());


        nowe1.przeliczCzasy(Genetyczny.instancja);
        nowe2.przeliczCzasy(Genetyczny.instancja);
        Genetyczny.populacja.add(nowe1);
        Genetyczny.populacja.add(nowe2);
    }

    /**
     * Metoda pomocnicza do crossOver - uzupełnia drugą część genomu
     * @param stara Stara maszyna
     * @param rozmiar Ilość zadań do ułożenia
     * @param miejsce miejsce rozpoczęcia układania zadań
     * @param nowa Nowa maszyna
     */
    private static void krzyzowanie(Maszyna stara, int rozmiar, int miejsce, Maszyna nowa) {
       // int j = 0;
        int temp ;
        for (int i = miejsce; i < rozmiar; i++) {
            nastepne:
            for (int j=0; j < rozmiar; j++) {
                temp = stara.getZadania()[j];
                //System.out.println(temp);
                for (int k = 0; k < i; k++) {
                    if (nowa.getZadania()[k] == temp) {
                        continue nastepne;
                    }
                }
                nowa.getZadania()[i] = temp;
            }
        }
    }

    /**
     * Mutowanie wybranych rozwiązań
     * @param rozwiazanie Rzowiązanie numer 1
     * @param powtorzenia Parametr opisujący ile par ma się wymieniać
     */
    public static void mutuj(Rozwiazanie rozwiazanie, int powtorzenia) {
        Random random = new Random();
        int rozmiar = rozwiazanie.getMaszyna1().getLiczbaZadan();
        Rozwiazanie nowe1 = new Rozwiazanie(new Maszyna(kopiuj(rozwiazanie.getMaszyna1().getZadania()), rozmiar), new Maszyna(kopiuj(rozwiazanie.getMaszyna2().getZadania()), rozmiar));
        for (int j = 0; j < 2; j++) {
            for (int i = 0; i < powtorzenia; i++) {
                int przesuniecie = random.nextInt((int) Math.ceil(( rozmiar*(Genetyczny.wspolczynnikMutacji))));
                int element = random.nextInt(rozmiar);
                int element2;
                if (random.nextBoolean()) {
                    if (element - przesuniecie >= 0) {
                        element2 = element - przesuniecie;
                    } else {
                        element2 = element + przesuniecie;
                    }
                } else if (element + przesuniecie < rozmiar) {
                    element2 = element + przesuniecie;
                } else {
                    element2 = element - przesuniecie;
                }
                element2 = Math.abs(element2) % rozmiar;

                int temp;
                if (j == 0) {
                    temp = nowe1.getMaszyna1().getZadania()[element];
                    nowe1.getMaszyna1().getZadania()[element] = nowe1.getMaszyna1().getZadania()[element2];
                    nowe1.getMaszyna1().getZadania()[element2] = temp;
                } else {
                    if (random.nextDouble() < 0.45) {
                        temp = nowe1.getMaszyna2().getZadania()[element];
                        nowe1.getMaszyna2().getZadania()[element] = nowe1.getMaszyna2().getZadania()[element2];
                        nowe1.getMaszyna2().getZadania()[element2] = temp;
                    }
                }
            }
            nowe1.przeliczCzasy(Genetyczny.instancja);
            Genetyczny.populacja.add(nowe1);
        }
    }


    /**
     * Pomocnicza metoda - pozwalajaca na kopiowanie tablic
     * @param tablica Tablica danych
     * @return Skopiowana tablica
     */
    private static int[] kopiuj(int[] tablica) {
        int skopiowana[] = new int[tablica.length];
        System.arraycopy(tablica, 0, skopiowana, 0, tablica.length);
        return skopiowana;
    }
}

