/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package programliriklagu.program;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 *---------------------------------------------
 * @author Yuita Arum Sari 0710960009
 * Ilmu Komputer 2007 Universitas Brawijaya
 * --------------------------------------------
 * 
 */
public class StemmingNaziefAndriani extends OlahDokumen {

    private String kata;
    private String kembalikanKata;
    private String akarKata;
    private ArrayList<String> listKamus;
    private String readDokumen;
    private String katajadi;
    private boolean isHapusSuffix;
    public boolean cekPrefiksSuffiks;
    private String bersikan = "";
    //private String hapusInfleksional;

    public StemmingNaziefAndriani() throws FileNotFoundException, IOException {
        listKamus = new ArrayList<String>();
        bacaKamus();
    }

    /**
     * Membaca isi kamus dan memasukkan kata-kata di dalam kamus ke dalam sebuah list
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public void bacaKamus() throws FileNotFoundException, IOException {
        readDokumen = readDokumenTeks("kamus.txt");
        StringTokenizer strKamus = new StringTokenizer(readDokumen, "|");
        while (strKamus.hasMoreTokens()) {
            listKamus.add(strKamus.nextToken());
        }
    }

    /**
     * Kata diinputkan
     * @param kata 
     */
    public void setKata(String kata) {
        this.kata = kata;
        this.akarKata = kata;
        bersikan = "";
    }

    /**
     * Kata yang diinputkan dicek, apakah sesuai di dalam kamus, jika kata yang diinputkan
     * sesuai dengan yang ada pada kamus maka kata tersebut merupakan akar kata (root word),
     * @param kata
     * @return 
     */
    public boolean cekKamus(String kata) {
        if (listKamus.contains(kata)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Untuk mrndapatkan kata dasar setlah mengalami proses stemming dengan menggunakan 
     * algoritma Nazief-Andriani
     * @param kata
     * @return 
     */
    public String KataDasar(String kata) {
        setKata(kata);
        if (cekKamus(kata)) {
            return akarKata;
        } else {
            hapusInfleksionalSuffiks();
            hapusDerivationSuffiks();

        }
        return akarKata;
    }

    /**
     * proses pada Infleksional Suffixes merupakan proses menghapus partikel suffiks
     * -lah,-kah,-nya,-tah,-pun,-ku,-mu
     */
    public void hapusInfleksionalSuffiks() {
        if (kata.endsWith("lah") || kata.endsWith("kah") || kata.endsWith("nya")
                || kata.endsWith("tah") || kata.endsWith("pun")) {
            kata = kata.substring(0, kata.length() - 3);
        } else if (kata.endsWith("ku") || kata.endsWith("mu")) {
            kata = kata.substring(0, kata.length() - 2);
        }
    }

    /**
     * hapus derivation suffixes -i,-an,-kan, jika kata ditemukan didalam kamus maka algoritma berhenti
     * 
     */
    public void hapusDerivationSuffiks() {
        isHapusSuffix = false;
        if (kata.endsWith("i")) {
            bersikan = kata.substring(0, kata.length() - 1);
            isHapusSuffix = true;
        } else if (kata.endsWith("kan")) {
            bersikan = kata.substring(0, kata.length() - 3);
            isHapusSuffix = true;
        } else if (kata.endsWith("an")) {
            bersikan = kata.substring(0, kata.length() - 2);
            isHapusSuffix = true;
        }

        //3a.jika kata ditemukan dalam kamus maka algoritma berhenti
        if (cekKamus(bersikan)) {
            akarKata = bersikan;
        } else {
            //lanjut ke langkah 4.
            //jika ada suffiks yang dihapus pergi ke langkah 4a.
            akarKata = kata;
            if (isHapusSuffix == true) {
                derivationPrefiksA();
            } else {//jika tidak ada pergi langkah 4b.
                derivationPrefiksB();
            }

        }
    }

    private void derivationPrefiksA() {
        //periksa kombinasi awalan dan akhiran yang tidak diijinkan
        boolean tipe1 = (akarKata.startsWith("be") && akarKata.endsWith("i"));
        boolean tipe2 = akarKata.startsWith("di") && akarKata.endsWith("an");
        boolean tipe3 = akarKata.startsWith("ke") && (akarKata.endsWith("i") || akarKata.endsWith("kan"));
        boolean tipe4 = akarKata.startsWith("me") && akarKata.endsWith("an");
        boolean tipe5 = akarKata.startsWith("se") && (akarKata.endsWith("i") || akarKata.endsWith("kan"));
        //jika kata tidak ditemukan maka menuju ke langkah 4b
        if (((tipe1) || (tipe2) || (tipe3) || (tipe4) || (tipe5)) == false) {
            derivationPrefiksB();
        }
    }

    private void derivationPrefiksB() {
        //tipe awalan ke-1 : -di,-ke,-se
        if ((akarKata.startsWith("di") || akarKata.startsWith("ke") || akarKata.startsWith("se"))
                && akarKata.length() > 2) {
            akarKata = akarKata.substring(2, akarKata.length());
        } //tipe awalan ke-2 : -te,-me,-be,-pe
        else if ((akarKata.startsWith("te") || akarKata.startsWith("me")
                || akarKata.startsWith("be") || akarKata.startsWith("pe"))) {
            //jika tipe prefiks bukan none digunakan rabel 2.
            String kata2 = akarKata.substring(2, akarKata.length());
            /*
             * tipe prefik : row 1 -->prefix none pada row pertama di tabel 2.
             * huruf r pada index pertama dihilangkan untuk mendapatkan kata dasar
             */
            if (kata2.length() > 3) {
                if ((kata2.charAt(0) == 'r') && (kata2.charAt(1) == 'r')) {
                    akarKata = kata2;
                } //tipe prefik:row 2 --> ter-luluh,digunakan tabel 3 sehingga yang dihilangkan adalah prefiks ter-
                else if ((kata2.charAt(0) == 'r') && (vowel(kata2.charAt(1)))) {
                    akarKata = kata2.substring(1, kata2.length());
                    //contoh: terevolusi-->evolusi
                } //tipe prefik: row 3 -->digunakan tabel 3 sehingga yang dihilangkan adalah prefiks ter-
                else if ((kata2.charAt(0) == 'r') && !((kata2.charAt(1) == 'r') || (vowel(kata2.charAt(1))))
                        && (kata2.charAt(2) == 'e') && (kata2.charAt(3) == 'r') && (vowel(kata2.charAt(4)))) {
                    akarKata = kata2.substring(1, kata2.length());
                }//row 4
                else if ((kata2.charAt(0) == 'r') && !((kata2.charAt(1) == 'r') || (vowel(kata2.charAt(1))))
                        && (kata2.charAt(2) == 'e') && (kata2.charAt(3) == 'r') && !(vowel(kata2.charAt(4)))) {
                    akarKata = kata2;
                }//row 5
                else if ((kata2.charAt(0) == 'r') && !((kata2.charAt(1) == 'r') || (vowel(kata2.charAt(1))))
                        && !(kata2.charAt(2) == 'e') && !(kata2.charAt(3) == 'r')) {
                    akarKata = kata2.substring(1, kata2.length());
                }//row 6
                else if (!((kata2.charAt(0) == 'r') || (vowel(kata2.charAt(0))))
                        && (kata2.charAt(1) == 'e') && (kata2.charAt(2) == 'r') && (vowel(kata2.charAt(3)))) {
                    akarKata = kata2;
                }//row 7
                else if (!((kata2.charAt(0) == 'r') || (vowel(kata2.charAt(0))))
                        && (kata2.charAt(1) == 'e') && (kata2.charAt(2) == 'r') && (!vowel(kata2.charAt(3)))) {
                    akarKata = kata2;
                } else {
                    akarKata = kata2;
                }
            }
        }
    }

    /**
     * seleksi vokal
     */
    private boolean vowel(char huruf) {
        if (huruf == 'a' || huruf == 'i' || huruf == 'u' || huruf == 'e' || huruf == 'o') {
            return true;
        }
        return false;
    }
}
