package main;

import dane.OdczytZapis;
import generator.Ruletka;
import generator.uszeregowania.GeneratorUszeregowania;
import struktury.Instancja;
import struktury.Rozwiazanie;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Główna klasa rozruchowa
 */
public class Genetyczny {

    private static long start, stop;
    private static long czas;

    public static double wspolczynnikMutacji;
    public static ArrayList<Rozwiazanie> populacja;
    private static double wspolczynnikKrzyzowania;
    public static Instancja instancja;
    public static int najlepszyCzas;
    public static int najgorszyCzas;

// ########## PARAMETRY ###############
    public static final int rozmiarPopulacji = 40;
    public static final int LICZBA_POKOLEN = 3000;
// ###################################

    public static int suma;
    public static Rozwiazanie najlepszy;
    public static double wsplDestabilizacji;

    public static void main(String[] args) {
        Random random = new Random();
        BufferedWriter writer = null;
        for (int z = 0; z < 1; z++) {
            try {
                writer = new BufferedWriter(new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream("rozwiazania/porownanieZLosowym/"+"czasy2.txt"))));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            for (int x = 0; x < 6; x++) {
                populacja = new ArrayList<>();
                //for (int y = 0; y < 4; y++) {111
                //instancja = OdczytZapis.wczytajDane(); //wczytanie instancji serializacją
                instancja = OdczytZapis.wczytajDaneTekstowe("instancje/tuning", x*4+3); // wczytywanie instancji z pliku
                najlepszyCzas = 999999999;
                najgorszyCzas = 0;

                //######### PARAMETRY ###################
                wspolczynnikMutacji = 0.05;
                wspolczynnikKrzyzowania = 0.3;
                wsplDestabilizacji = 0.4;
                //#######################################

                //Generowanie poczatkowej populacji
                Rozwiazanie rozwiazanie;
                int pamiec = 0;
                int licznik = 0;
                start();
                for (int i = 0; i < rozmiarPopulacji; i++) {


                    rozwiazanie = GeneratorUszeregowania.generujRozwiaznieLosowe(instancja);
                    rozwiazanie.przeliczCzasy(instancja);
                    populacja.add(rozwiazanie);
                    if (rozwiazanie.getCzas() > najgorszyCzas) {
                        najgorszyCzas = rozwiazanie.getCzas();
                    }
                    if (rozwiazanie.getCzas() < najlepszyCzas) {
                        najlepszyCzas = rozwiazanie.getCzas();
                    }
                }
                najlepszy = populacja.get(0);
                System.out.println("Początowo: max" + najlepszyCzas + " min " + najgorszyCzas);
                System.out.println("#######################################");

                //Optymalizacja rozwiazania za pomocą algorytmu

                for (int i = 0; i < LICZBA_POKOLEN; i++) { //Warunek stopu- liczba pokoleń
                    czas = 0;
                   // while (czas < 3000) { // warunek stopu czas
                    wspolczynnikMutacji = 1 - i / LICZBA_POKOLEN; //liniowy wspolczynnik mutacji
                    if (i % 100 == 0) {

                        if (pamiec == najlepszyCzas) {
                            licznik++;
                        }

                        if (licznik == LICZBA_POKOLEN * 0.01 * wsplDestabilizacji) {
                             wspolczynnikMutacji = 0.5; //mechanizm destabilizacji
                            licznik = 0;
                        }
                        pamiec = najlepszyCzas;

                        /*try {
                            writer.write(String.valueOf(najlepszyCzas));
                            writer.newLine();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }*/
                    }

                    /**
                     * Operacje wykonywanie w jednym pokoleniu- krzyżowanie, mutowanie, selekcja .
                     */
                    for (int j = 0; j < rozmiarPopulacji; j++) {
                        //Cześć populacji zgodnie z współczynnikiem krzyżowania poddawana jest krzyżowaniu
                        if (random.nextDouble() < wspolczynnikKrzyzowania / 2.0) {
                            //wybieramy partnera do krzyżownaia za pomocą turnieju pomiedzy dwoma losowymi rozwiązaniami z populacji
                            int element2 = random.nextInt(rozmiarPopulacji);
                            int element3 = random.nextInt(rozmiarPopulacji);
                            int best;
                            if (populacja.get(element2).getCzas() < populacja.get(element3).getCzas()) {
                                best = element2;
                            } else {
                                best = element3;
                            }
                            GeneratorUszeregowania.crossOver(populacja.get(j), populacja.get(best));
                        }

                        // część rozwiązań z populacji jest mutowana
                        if (random.nextDouble() < wspolczynnikMutacji) {
                            GeneratorUszeregowania.mutuj(populacja.get(j), 1);
                        }
                    }
                    //Wyszukujemy najlepsze i najgorsze rozwiazanie(potrzebne do okreslenia prawdopodobieństwa w ruletce)
                    najlepszyCzas = 999999999;
                    najgorszyCzas = 0;
                    suma = 0;
                    for (Rozwiazanie roz : populacja
                            ) {
                        if (roz.getCzas() > najgorszyCzas) {
                            najgorszyCzas = roz.getCzas();
                        }
                        if (roz.getCzas() < najlepszyCzas) {
                            najlepszyCzas = roz.getCzas();
                        }
                        suma += roz.getCzas();
                    }

                    //zmniejszamy populacje do wybranego rozmiaru za pomocą ruletki
                    Ruletka.ruletka(rozmiarPopulacji);
                    najlepszyCzas = 999999999;
                    najgorszyCzas = 0;
                    for (Rozwiazanie roz : populacja
                            ) {
                        if (roz.getCzas() > najgorszyCzas) {
                            najgorszyCzas = roz.getCzas();
                        }
                        if (roz.getCzas() < najlepszyCzas) {
                            najlepszyCzas = roz.getCzas();
                        }
                        if (roz.getCzas() < najlepszy.getCzas()) {
                            najlepszy = roz;
                        }
                    }
                    stop();
                    czas = Time();
                    // System.out.println(czas);
                }


                // Wypisywanie rozwiazania
                najlepszyCzas = 999999999;
                najgorszyCzas = 0;
                suma = 0;
                for (Rozwiazanie roz : populacja
                        ) {
                    if (roz.getCzas() > najgorszyCzas) {
                        najgorszyCzas = roz.getCzas();
                    }
                    if (roz.getCzas() < najlepszyCzas) {
                        najlepszyCzas = roz.getCzas();
                    }
                    if (roz.getCzas() < najlepszy.getCzas()) {
                        najlepszy = roz;
                    }
                    suma += roz.getCzas();
                }

                 /*int min = 999999990;
            int max = 0;
            Rozwiazanie optymalneLosowe = null;
            long suma = 0;
            for (int i = 0; i < 50000; i++) {
                Rozwiazanie rozwiazanielos = GeneratorUszeregowania.generujRozwiaznieLosowe(instancja);
                rozwiazanielos.przeliczCzasy(instancja);
                //System.out.println(rozwiazanie.getCzas());
                if (rozwiazanielos.getCzas() > max) {
                    max = rozwiazanielos.getCzas();
                }
                if (rozwiazanielos.getCzas() < min) {
                    min = rozwiazanielos.getCzas();
                    optymalneLosowe = rozwiazanielos;
                }
                suma += rozwiazanielos.getCzas();
                //System.out.println("zadania 1");
            }*/


                try {
                    writer.write(String.valueOf(najlepszy.getCzas()));// + ";"+ String.valueOf(optymalneLosowe.getCzas())+ ";" + suma/50000);
                    writer.newLine();
                    //writer.close();
                    OdczytZapis.zapiszRozwiazanie(instancja, najlepszy, x, "rozwiazania/porownanieZLosowym/");
                    //OdczytZapis.zapiszRozwiazanie(instancja, optymalneLosowe, x+20, "rozwiazania/porownanieZLosowym");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                //writer.write(String.valueOf(najlepszy.getCzas()));
                //writer.newLine();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        //try {
        //   writer.close();
        //} catch (IOException e) {
        //  e.printStackTrace();
        //}
    }


    public static void start() {
        start = System.currentTimeMillis(); // start timing
    }

    public static void stop() {
        stop = System.currentTimeMillis(); // stop timing
    }

    public static long Time() {
        return stop - start;
    }
}
