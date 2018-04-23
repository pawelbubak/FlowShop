package generator;

import main.Genetyczny;

import java.util.Random;

/**
 * Klasa mająca za zadanie wybierać rozwiązania do nowej populacji
 */
public class Ruletka {
    /**
     * Wybór rozwiązań ze starej populacji do usunięcia i stworzenie w ten sposób nowej (usuwając zmniejszam rozmiar populacji)
     * @param rozmiarDocelowy Docelowa wielkość populacji
     */
    public  static void  ruletka(int rozmiarDocelowy){
        int rozmiar= Genetyczny.populacja.size();
        Random random= new Random();
        int roznica=Genetyczny.najgorszyCzas-Genetyczny.najlepszyCzas;
        int wybor;
        for (int i = 0; i <rozmiar-rozmiarDocelowy ; i++) {
            while (true){
                wybor=random.nextInt(Genetyczny.populacja.size());
                // Prwdopodobieństwo usunięcia elementów zależne jest od różnicy funkcji celu rozwiązania i funkcji celu najlepszego rozwiązania w populacji
                // Gorsze rozwiązania usuwane są z większym prawdopodobieństwem
                if(random.nextInt(roznica+1)<=(Genetyczny.populacja.get(wybor).getCzas()-Genetyczny.najlepszyCzas)){
                   Genetyczny.populacja.remove(wybor);
                   break;
                }
            }
        }
    }
}
