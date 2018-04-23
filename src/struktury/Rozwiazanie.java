package struktury;

/**
 * Klasa reprezentująca rozwiązanie otrzymane poprzez losowe lub optymalizowane uporząkawanie
 */
public class Rozwiazanie {
    private Maszyna maszyna1;
    private Maszyna maszyna2;
    int czas;

    public Maszyna getMaszyna1() {
        return maszyna1;
    }

    public void setMaszyna1(Maszyna maszyna1) {
        this.maszyna1 = maszyna1;
    }

    public Maszyna getMaszyna2() {
        return maszyna2;
    }

    public void setMaszyna2(Maszyna maszyna2) {
        this.maszyna2 = maszyna2;
    }

    public Rozwiazanie() {
    }

    public Rozwiazanie(Maszyna maszyna1, Maszyna maszyna2) {
        this.maszyna1 = maszyna1;
        this.maszyna2 = maszyna2;
    }

    public int getCzas() {
        return czas;
    }

    /**
     * Metoda przeliczająca czasy zadanego uporząkowania
     * @param instancja Instancja zawierająca potrzebne informacje o zadaniach i przerwach do wyliczenia czasu
     */
    public void przeliczCzasy(Instancja instancja) {
        int ostatniCzas = 0;
        int aktualnaPrzerwa = 0;
        for (int i = 0; i < maszyna1.getLiczbaZadan(); i++) {
            int id = maszyna1.getZadania()[i];
            int dlugosc = instancja.getZadaniaMaszyna1()[id].getDlugosc();
            int rozpoczecie = instancja.getZadaniaMaszyna1()[id].getRozpoczecie();
            /**
             * PIERWSZA MASZYNA
             */
            if (ostatniCzas<rozpoczecie){
                ostatniCzas=rozpoczecie;
                for (int j = 0; j <instancja.getLiczbaPrzerw() ; j++) {
                    /**
                     * Sprawdza czy rozpoczęcie zadania nie wypada w trakcie przerwy
                     */
                    if(instancja.getPrzerwy()[j].getRozpoczecie()<rozpoczecie && instancja.getPrzerwy()[j].getRozpoczecie()+instancja.getPrzerwy()[j].getDlugosc()>rozpoczecie){
                        aktualnaPrzerwa=j+1;
                        ostatniCzas=instancja.getPrzerwy()[j].getRozpoczecie()+instancja.getPrzerwy()[j].getDlugosc();
                        break;
                    }else if (rozpoczecie>instancja.getPrzerwy()[j].getRozpoczecie()){
                        aktualnaPrzerwa=j+1;
                    }
                }
            }
            if(aktualnaPrzerwa<instancja.getLiczbaPrzerw()){
                /**
                 * Sprawdzamy czy na początku jest przerwa
                 */
            if (instancja.getPrzerwy()[aktualnaPrzerwa].getRozpoczecie() != ostatniCzas) { //czy na poczatku jest przerwa
                /**
                 * Sprawdzamy czy w trakcie zadania nie ma przerwy
                 */
                if (instancja.getPrzerwy()[aktualnaPrzerwa].getRozpoczecie() >= ostatniCzas + dlugosc) { //czy w tarkcie jest przerwa
                    ostatniCzas += dlugosc;
                    maszyna1.getCzasyZakonczeniaZadan()[id] = ostatniCzas;
                    continue ;
                    /**
                     * Sprawdzamy czy w trakcie zadania jest przerwa
                     */
                } else { //w trakcie jest przerwa
                    int pozostalyCzas = (int) (Math.ceil(dlugosc * 0.2) + ostatniCzas - instancja.getPrzerwy()[aktualnaPrzerwa].getRozpoczecie() + dlugosc);
                    ostatniCzas = instancja.getPrzerwy()[aktualnaPrzerwa].getRozpoczecie() + instancja.getPrzerwy()[aktualnaPrzerwa].getDlugosc(); //dodaje przerwe
                    aktualnaPrzerwa++;
                    while(pozostalyCzas>0){
                        if(aktualnaPrzerwa<instancja.getLiczbaPrzerw()) {// jesli są przerwy
                            if (instancja.getPrzerwy()[aktualnaPrzerwa].getRozpoczecie() >= ostatniCzas + pozostalyCzas) { // jesli pozostała czesc cala sie zmiesci
                                ostatniCzas += pozostalyCzas;
                                pozostalyCzas = 0;
                                maszyna1.getCzasyZakonczeniaZadan()[id] = ostatniCzas;
                                continue;
                            } else {
                                pozostalyCzas -= instancja.getPrzerwy()[aktualnaPrzerwa].getRozpoczecie() - ostatniCzas;
                                ostatniCzas = instancja.getPrzerwy()[aktualnaPrzerwa].getRozpoczecie() + instancja.getPrzerwy()[aktualnaPrzerwa].getDlugosc(); //dodaje przerwe
                                aktualnaPrzerwa++;
                            }
                        }else { //gdy skonczyly sie przerwy
                            ostatniCzas += pozostalyCzas;
                            pozostalyCzas = 0;
                            maszyna1.getCzasyZakonczeniaZadan()[id] = ostatniCzas;
                            continue;
                        }
                    }
                }
                /**
                 * Dodaje przerwę jeśli jest na początku
                 */
            }else{//gdy na początku jest przerwa
                ostatniCzas = instancja.getPrzerwy()[aktualnaPrzerwa].getRozpoczecie() + instancja.getPrzerwy()[aktualnaPrzerwa].getDlugosc(); //dodaje przerwe
                aktualnaPrzerwa++;
                i--;
            }
            }
            /**
             * Dodaje zadania gdy skończyły się przerwy
             */
            else{//gdy skonczyly sie przerwy
                ostatniCzas += dlugosc;
                maszyna1.getCzasyZakonczeniaZadan()[id] = ostatniCzas;
                continue ;
            }

        }
        /**
         * DRUGA MASZYNA
         */
        ostatniCzas=0;
        for (int i = 0; i < maszyna2.getLiczbaZadan(); i++) {
            int id = maszyna2.getZadania()[i];
            int dlugosc = instancja.getZadaniaMaszyna2()[id].getDlugosc();
           // int rozpoczecie = instancja.getZadaniaMaszyna2()[id].getRozpoczecie();
            if (ostatniCzas>maszyna1.getCzasyZakonczeniaZadan()[id]){
                ostatniCzas+=dlugosc;
                maszyna2.getCzasyZakonczeniaZadan()[id]=ostatniCzas;
            }else{
                ostatniCzas=maszyna1.getCzasyZakonczeniaZadan()[id]+dlugosc;
                maszyna2.getCzasyZakonczeniaZadan()[id]=ostatniCzas;
            }
        }
        czas=ostatniCzas;
    }
}
