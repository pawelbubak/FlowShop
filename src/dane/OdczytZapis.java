package dane;


import generator.uszeregowania.GeneratorUszeregowania;
import struktury.Instancja;
import struktury.Maszyna;
import struktury.Rozwiazanie;
import struktury.task.Break;
import struktury.task.Zadanie;

import java.io.*;
import java.util.ArrayList;

/**
 * Klasa pozwalająca serializować i deserializować, zapisywać i odczytywać przykładowe instancje problemu szeregowania zadań
 */
public class OdczytZapis {
    /**
     * Serializacja
     *
     * @param instancja instancja do zserializowania
     */
    public static void zapiszDane(Instancja instancja) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(
                    new BufferedOutputStream(
                            new FileOutputStream("instancja.bin")));
            out.writeObject(instancja);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Deserializacja
     *
     * @return wczytywana instancja
     */
    public static Instancja wczytajDane() {
        try {
            ObjectInputStream in = new ObjectInputStream(
                    new BufferedInputStream(
                            new FileInputStream("instancja.bin")));
            Instancja instancja = (Instancja) in.readObject();
            in.close();
            return instancja;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Zapis Instancji
     *
     * @param instancja      Instancja do zapisu
     * @param sciezka        sciezka do pliku
     * @param numerInstancji zapisywanej instancji
     */
    public static void zapiszDaneTekstowe(Instancja instancja, String sciezka, int numerInstancji) {
        String path = sciezka + "/instancja" + String.valueOf(numerInstancji) + ".txt";
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(path))));
            writer.write("**** " + numerInstancji + " ****\n");
            writer.write(instancja.getLiczbaZadan() + "\n");
            for (int i = 0; i < instancja.getLiczbaZadan(); i++) {
                writer.write(instancja.getZadaniaMaszyna1()[i].getDlugosc() + "; " + instancja.getZadaniaMaszyna2()[i].getDlugosc() + "; 1; 2; " + instancja.getZadaniaMaszyna1()[i].getRozpoczecie() + "\n");
            }
            for (int i = 0; i < instancja.getLiczbaPrzerw(); i++) {
                writer.write(i + 1 + "; 1; " + instancja.getPrzerwy()[i].getDlugosc() + "; " + instancja.getPrzerwy()[i].getRozpoczecie() + "\n");
            }
            writer.write("*** EOF ***");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Wczytywanie instancji z pliku
     *
     * @param sciezka        scieżka do pliku
     * @param numerInstancji numer wczytywanej instancji
     * @return Instancja
     */
    public static Instancja wczytajDaneTekstowe(String sciezka, int numerInstancji) {
        String path = sciezka + "/instancja" + numerInstancji + ".txt";
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path)));//OdczytZapis.class.getResourceAsStream(path)));
            Instancja instancja = new Instancja();
            reader.readLine();
            String linia = reader.readLine();
            instancja.setLiczbaZadan(Short.parseShort(linia));
            instancja.setZadaniaMaszyna1(new Zadanie[instancja.getLiczbaZadan()]);
            instancja.setZadaniaMaszyna2(new Zadanie[instancja.getLiczbaZadan()]);
            String[] temp;
            for (int i = 0; i < instancja.getLiczbaZadan(); i++) {
                linia = reader.readLine();
                temp = linia.split("; ");
                instancja.getZadaniaMaszyna1()[i] = new Zadanie(Integer.parseInt(temp[4]), Integer.parseInt(temp[0]), i, false);
                instancja.getZadaniaMaszyna2()[i] = new Zadanie(Integer.parseInt(temp[4]) + instancja.getZadaniaMaszyna1()[i].getDlugosc(), Integer.parseInt(temp[1]), i, false);
            }
            ArrayList<Break> listaPrzerw = new ArrayList<>();
            while (true) {
                linia = reader.readLine();
                if (linia.equals("*** EOF ***"))
                    break;
                temp = linia.split("; ");
                listaPrzerw.add(new Break(Integer.parseInt(temp[3]), Integer.parseInt(temp[2])));
            }
            instancja.setLiczbaPrzerw((short) listaPrzerw.size());
            instancja.setPrzerwy(new Break[instancja.getLiczbaPrzerw()]);
            for (int i = 0; i < instancja.getLiczbaPrzerw(); i++) {
                instancja.getPrzerwy()[i] = listaPrzerw.get(i);
            }
            reader.close();
            return instancja;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Zapis wygenerowanego roziązania
     *
     * @param instancja        Instanacja
     * @param rozwiazanie      Wygenerowane rozwiązania na podstwie przeslanej instancji
     * @param numerRozwiazania numer rozwiązania
     * @param sciezka          sciezka do pliku
     * @throws IOException Brak pliku
     */
    public static void zapiszRozwiazanie(Instancja instancja, Rozwiazanie rozwiazanie, int numerRozwiazania, String sciezka) throws IOException {
        // Na podstawie wygenerwanych wczęsnij czasów zakończenia zadań oraz informacji o zadaniach i przerwach zawartych w instacji generuje plik w zadanym formacie
        String path = sciezka + "/rozwiazanie" + String.valueOf(numerRozwiazania) + ".txt";
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(path))));
        Maszyna maszyna1 = rozwiazanie.getMaszyna1();
        Maszyna maszyna2 = rozwiazanie.getMaszyna2();

        writer.write("**** " + numerRozwiazania + " ****\n");
        Rozwiazanie losowe = GeneratorUszeregowania.generujRozwiaznieLosowe(instancja);
        losowe.przeliczCzasy(instancja);
        int losowyCzas = losowe.getCzas();
        writer.write(rozwiazanie.getCzas() + "," + losowyCzas);
        writer.newLine();
        writer.write("M1:");

        int ostatniCzas = 0;

        int nrAktualnejPrzerwy = 0;
        int idleLicznik1 = 1;
        int maintlicznik = 1;
        int sumaMaint = 0;
        int sumaIdle = 0;
        int rozpoczecie;
        int czasZakonczeniaZadania;
        int dlugoscZadania;
        for (int i = 0; i < maszyna1.getLiczbaZadan(); i++) {
            int id = maszyna1.getZadania()[i];
            dlugoscZadania = instancja.getZadaniaMaszyna1()[id].getDlugosc();
            czasZakonczeniaZadania = rozwiazanie.getMaszyna1().getCzasyZakonczeniaZadan()[id];

            if (nrAktualnejPrzerwy < instancja.getLiczbaPrzerw()) {
                if (ostatniCzas == instancja.getPrzerwy()[nrAktualnejPrzerwy].getRozpoczecie()) {
                    writer.write("maint" + maintlicznik + "_M1," + instancja.getPrzerwy()[nrAktualnejPrzerwy].getRozpoczecie() + "," + instancja.getPrzerwy()[nrAktualnejPrzerwy].getDlugosc() + ";");
                    sumaMaint += instancja.getPrzerwy()[nrAktualnejPrzerwy].getDlugosc();
                    ostatniCzas += instancja.getPrzerwy()[nrAktualnejPrzerwy].getDlugosc();
                    nrAktualnejPrzerwy++;
                    maintlicznik++;
                }
            }
            if (ostatniCzas == czasZakonczeniaZadania - dlugoscZadania) { //jesli zadanie nie ma idle i maint
                //TODO zapisz zadanie
                writer.write("op1_" + (id + 1) + "," + ostatniCzas + "," + dlugoscZadania + "," + dlugoscZadania + ";");
                ostatniCzas += dlugoscZadania;
            } else {
                if (instancja.getZadaniaMaszyna1()[id].getRozpoczecie() > ostatniCzas) { //jesli jest idle
                    if (nrAktualnejPrzerwy >= instancja.getLiczbaPrzerw()) {

                        //TODO dodaje idle
                        writer.write("idle" + idleLicznik1 + "_M1," + ostatniCzas + "," + (maszyna1.getCzasyZakonczeniaZadan()[id] - dlugoscZadania - ostatniCzas) + ";");
                        sumaIdle += (maszyna1.getCzasyZakonczeniaZadan()[id] - dlugoscZadania - ostatniCzas);
                        idleLicznik1++;
                        ostatniCzas += (maszyna1.getCzasyZakonczeniaZadan()[id] - dlugoscZadania - ostatniCzas);
                        if (instancja.getZadaniaMaszyna1()[id].getRozpoczecie() + dlugoscZadania == czasZakonczeniaZadania) {
                            //TODO dodaj zadanie

                            writer.write("op1_" + (id + 1) + "," + ostatniCzas + "," + dlugoscZadania + "," + dlugoscZadania + ";");
                            ostatniCzas += dlugoscZadania;
                        } else {
                            //TODO dodaj zadanie + 20% i break'i i idla
                            writer.write("op1_" + (id + 1) + "," + ostatniCzas + "," + dlugoscZadania + "," + (int) Math.ceil(dlugoscZadania * 1.2) + ";");
                            ostatniCzas += dlugoscZadania;
                            if(nrAktualnejPrzerwy<instancja.getLiczbaPrzerw()){
                            while (instancja.getPrzerwy()[nrAktualnejPrzerwy].getRozpoczecie() < czasZakonczeniaZadania) {
                                writer.write("maint" + maintlicznik + "_M1," + instancja.getPrzerwy()[nrAktualnejPrzerwy].getRozpoczecie() + "," + instancja.getPrzerwy()[nrAktualnejPrzerwy].getDlugosc() + ";");
                                sumaMaint += instancja.getPrzerwy()[nrAktualnejPrzerwy].getDlugosc();
                                maintlicznik++;
                                nrAktualnejPrzerwy++;
                                if (nrAktualnejPrzerwy == instancja.getPrzerwy().length) {
                                    break;
                                }
                            }
                            }
                            ostatniCzas = czasZakonczeniaZadania;
                        }
                    } else {
                        if (instancja.getPrzerwy()[nrAktualnejPrzerwy].getRozpoczecie() < instancja.getZadaniaMaszyna1()[id].getRozpoczecie() && ostatniCzas <= instancja.getPrzerwy()[nrAktualnejPrzerwy].getRozpoczecie()) {
                            //TODO dodaje brake'i i idle pomiedzy rozpoczecieam zadania a zakonczeniem ostatniego i sprawdzam czy miedzy ostatnim czasem a zakonczeniem zadania jest break
                            if (nrAktualnejPrzerwy < instancja.getLiczbaPrzerw()) { //###########################
                                while (instancja.getPrzerwy()[nrAktualnejPrzerwy].getRozpoczecie() < instancja.getZadaniaMaszyna1()[id].getRozpoczecie()) {
                                    writer.write("idle" + idleLicznik1 + "_M1," + ostatniCzas + "," + (instancja.getPrzerwy()[nrAktualnejPrzerwy].getRozpoczecie() - ostatniCzas) + ";");
                                    sumaIdle += (instancja.getPrzerwy()[nrAktualnejPrzerwy].getRozpoczecie() - ostatniCzas);
                                    idleLicznik1++;
                                    ostatniCzas = instancja.getPrzerwy()[nrAktualnejPrzerwy].getRozpoczecie() + instancja.getPrzerwy()[nrAktualnejPrzerwy].getDlugosc();
                                    writer.write("maint" + maintlicznik + "_M1," + instancja.getPrzerwy()[nrAktualnejPrzerwy].getRozpoczecie() + "," + instancja.getPrzerwy()[nrAktualnejPrzerwy].getDlugosc() + ";");
                                    sumaMaint += instancja.getPrzerwy()[nrAktualnejPrzerwy].getDlugosc();
                                    nrAktualnejPrzerwy++;
                                    maintlicznik++;
                                    if (nrAktualnejPrzerwy == instancja.getPrzerwy().length) {
                                        break;

                                    }

                                }

                            }
                            //ostatniCzas = Math.max((instancja.getPrzerwy()[nrAktualnejPrzerwy -1].getDlugosc() + instancja.getPrzerwy()[nrAktualnejPrzerwy-1].getDlugosc()), instancja.getZadaniaMaszyna1()[id].getRozpoczecie());
                            if(ostatniCzas<instancja.getZadaniaMaszyna1()[id].getRozpoczecie()){
                                writer.write("idle" + idleLicznik1 + "_M1," + ostatniCzas + "," + (instancja.getZadaniaMaszyna1()[id].getRozpoczecie()-ostatniCzas)+ ";");
                                sumaIdle += (instancja.getZadaniaMaszyna1()[id].getRozpoczecie()-ostatniCzas);
                                ostatniCzas=instancja.getZadaniaMaszyna1()[id].getRozpoczecie();
                            }

                            if (czasZakonczeniaZadania - ostatniCzas == dlugoscZadania) {
                                writer.write("op1_" + (id + 1) + "," + ostatniCzas + "," + dlugoscZadania + "," + dlugoscZadania + ";");
                                ostatniCzas = czasZakonczeniaZadania;
                            } else {
                                if (nrAktualnejPrzerwy < instancja.getLiczbaPrzerw()) { //przerwy na począttku przed zadnaiem
                                    while (instancja.getPrzerwy()[nrAktualnejPrzerwy].getRozpoczecie() == ostatniCzas) {
                                        writer.write("maint" + maintlicznik + "_M1," + instancja.getPrzerwy()[nrAktualnejPrzerwy].getRozpoczecie() + "," + instancja.getPrzerwy()[nrAktualnejPrzerwy].getDlugosc() + ";");
                                        sumaMaint += instancja.getPrzerwy()[nrAktualnejPrzerwy].getDlugosc();
                                        ostatniCzas = instancja.getPrzerwy()[nrAktualnejPrzerwy].getRozpoczecie() + instancja.getPrzerwy()[nrAktualnejPrzerwy].getDlugosc();
                                        maintlicznik++;
                                        nrAktualnejPrzerwy++;
                                        if (nrAktualnejPrzerwy == instancja.getPrzerwy().length) {
                                            break;
                                        }
                                    }
                                }
                                if (ostatniCzas == czasZakonczeniaZadania - dlugoscZadania) {
                                    writer.write("op1_" + (id + 1) + "," + ostatniCzas + "," + dlugoscZadania + "," + dlugoscZadania + ";");
                                    ostatniCzas += dlugoscZadania;
                                } else {


                                    writer.write("op1_" + (id + 1) + "," + ostatniCzas + "," + dlugoscZadania + "," +(int) Math.ceil(dlugoscZadania * 1.2) + ";");
                                   // ostatniCzas += dlugoscZadania;
                                    if (nrAktualnejPrzerwy < instancja.getLiczbaPrzerw()) {
                                        while (instancja.getPrzerwy()[nrAktualnejPrzerwy].getRozpoczecie() < czasZakonczeniaZadania) {
                                            writer.write("maint" + maintlicznik + "_M1," + instancja.getPrzerwy()[nrAktualnejPrzerwy].getRozpoczecie() + "," + instancja.getPrzerwy()[nrAktualnejPrzerwy].getDlugosc() + ";");
                                            sumaMaint += instancja.getPrzerwy()[nrAktualnejPrzerwy].getDlugosc();
                                            nrAktualnejPrzerwy++;
                                            maintlicznik++;
                                            if (nrAktualnejPrzerwy == instancja.getPrzerwy().length) {
                                                break;
                                            }
                                        }
                                    }
                                }
                                ostatniCzas = czasZakonczeniaZadania;

                            }

                        } else {
                            //TODO dodaje idle
                            writer.write("idle" + idleLicznik1 + "_M1," + ostatniCzas + "," + (maszyna1.getCzasyZakonczeniaZadan()[id] - dlugoscZadania - ostatniCzas) + ";");
                            sumaIdle += (maszyna1.getCzasyZakonczeniaZadan()[id] - dlugoscZadania - ostatniCzas);
                            idleLicznik1++;
                            ostatniCzas += (maszyna1.getCzasyZakonczeniaZadan()[id] - dlugoscZadania - ostatniCzas);
                            if (instancja.getZadaniaMaszyna1()[id].getRozpoczecie() + dlugoscZadania == czasZakonczeniaZadania) {
                                //TODO dodaj zadanie

                                writer.write("op1_" + (id + 1) + "," + ostatniCzas + "," + dlugoscZadania + "," + dlugoscZadania + ";");
                                ostatniCzas += dlugoscZadania;
                            } else {
                                //TODO dodaj zadanie + 20% i break'i i idla
                                writer.write("op1_" + (id + 1) + "," + ostatniCzas + "," + dlugoscZadania + "," +(int) Math.ceil(dlugoscZadania * 1.2) + ";");
                                ostatniCzas += dlugoscZadania;
                                if(nrAktualnejPrzerwy<instancja.getLiczbaPrzerw()) {
                                    while (instancja.getPrzerwy()[nrAktualnejPrzerwy].getRozpoczecie() < czasZakonczeniaZadania) {
                                        writer.write("maint" + maintlicznik + "_M1," + instancja.getPrzerwy()[nrAktualnejPrzerwy].getRozpoczecie() + "," + instancja.getPrzerwy()[nrAktualnejPrzerwy].getDlugosc() + ";");
                                        sumaMaint += instancja.getPrzerwy()[nrAktualnejPrzerwy].getDlugosc();
                                        maintlicznik++;
                                        nrAktualnejPrzerwy++;
                                        if (nrAktualnejPrzerwy == instancja.getPrzerwy().length) {
                                            break;
                                        }
                                    }
                                }
                                ostatniCzas = czasZakonczeniaZadania;
                            }
                        }
                    }

                    //ok
                } else {//  jesli nie ma idle
                    if(nrAktualnejPrzerwy<instancja.getLiczbaPrzerw()) { //wyszukiwanie przerw przed ropoczeciem zadania
                        while (instancja.getPrzerwy()[nrAktualnejPrzerwy].getRozpoczecie() == ostatniCzas) {
                            writer.write("maint" + maintlicznik + "_M1," + instancja.getPrzerwy()[nrAktualnejPrzerwy].getRozpoczecie() + "," + instancja.getPrzerwy()[nrAktualnejPrzerwy].getDlugosc() + ";");
                            sumaMaint += instancja.getPrzerwy()[nrAktualnejPrzerwy].getDlugosc();
                            ostatniCzas = instancja.getPrzerwy()[nrAktualnejPrzerwy].getRozpoczecie() + instancja.getPrzerwy()[nrAktualnejPrzerwy].getDlugosc();
                            maintlicznik++;
                            nrAktualnejPrzerwy++;
                            if (nrAktualnejPrzerwy == instancja.getPrzerwy().length) {
                                break;
                            }
                        }
                    }
                    if (ostatniCzas == czasZakonczeniaZadania - dlugoscZadania) {
                        writer.write("op1_" + (id + 1) + "," + ostatniCzas + "," + dlugoscZadania + "," + dlugoscZadania + ";");
                        ostatniCzas += dlugoscZadania;
                    } else {

                        // wyszukaj brake'i w srodku zadania
                        //dodaje zadanie z dlugoscią zwiekszona o 20 %
                        writer.write("op1_" + (id + 1) + "," + ostatniCzas + "," + dlugoscZadania + "," +(int) (Math.ceil(dlugoscZadania * 1.2)) + ";");
                        ostatniCzas += dlugoscZadania;
                        if(nrAktualnejPrzerwy<instancja.getLiczbaPrzerw()) {
                            while (instancja.getPrzerwy()[nrAktualnejPrzerwy].getRozpoczecie() < czasZakonczeniaZadania) {
                                writer.write("maint" + maintlicznik + "_M1," + instancja.getPrzerwy()[nrAktualnejPrzerwy].getRozpoczecie() + "," + instancja.getPrzerwy()[nrAktualnejPrzerwy].getDlugosc() + ";");
                                sumaMaint += instancja.getPrzerwy()[nrAktualnejPrzerwy].getDlugosc();
                                nrAktualnejPrzerwy++;
                                maintlicznik++;
                                if (nrAktualnejPrzerwy == instancja.getPrzerwy().length) {
                                    break;
                                }
                            }
                        }
                        ostatniCzas = czasZakonczeniaZadania;
                    }
                }
            }
        }


        ostatniCzas = 0;
        int idleLicznik = 1;
        int idle2suma = 0;
        writer.newLine();
        writer.write("M2:");
        for (int i = 0; i < maszyna2.getLiczbaZadan(); i++) {
            int id = maszyna2.getZadania()[i];
            int dlugosc = instancja.getZadaniaMaszyna2()[id].getDlugosc();
            if (ostatniCzas > maszyna1.getCzasyZakonczeniaZadan()[id]) {
                writer.write("op2_" + (id + 1) + "," + ostatniCzas + "," + dlugosc + "," + dlugosc + ";");
                ostatniCzas += dlugosc;
                maszyna2.getCzasyZakonczeniaZadan()[id] = ostatniCzas;
            } else {
                writer.write("idle" + idleLicznik + "_M2," + ostatniCzas + "," + (maszyna1.getCzasyZakonczeniaZadan()[id] - ostatniCzas) + ";");
                idle2suma += (maszyna1.getCzasyZakonczeniaZadan()[id] - ostatniCzas);
                ostatniCzas = maszyna1.getCzasyZakonczeniaZadan()[id];
                writer.write("op2_" + (id + 1) + "," + ostatniCzas + "," + dlugosc + "," + dlugosc + ";");
                ostatniCzas += dlugosc;
                idleLicznik++;
            }
        }
        writer.newLine();
        writer.write(sumaMaint + "\n");
        writer.write(0 + "\n");
        writer.write(sumaIdle + "\n");
        writer.write(idle2suma + "\n");

        writer.write("*** EOF ***");
        int czas = ostatniCzas;
        writer.close();
    }
}
