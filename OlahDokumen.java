/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package programliriklagu.program;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 *---------------------------------------------
 * @author Yuita Arum Sari 0710960009
 * Ilmu Komputer 2007 Universitas Brawijaya
 * --------------------------------------------
 * 
 */
public class OlahDokumen {

    private BufferedReader bufferBaca;
    private String barisData = "";
    private String bersih = "";
    private StringTokenizer token;
    private String words = "";
    private HashMap<String, Integer> map;
    private ArrayList<String> listKata;
    
    public String readDokumenTeks(String bacateks) throws FileNotFoundException, IOException {
        File bacafile = new File(bacateks);//mengubah inputan string mejadi sebuah file
        FileReader inputDokumen = new FileReader(bacafile);//membaca inputan sebuah dokumen
        bufferBaca = new BufferedReader(inputDokumen);//buffer dari dokumen ketika dibaca
        StringBuffer content = new StringBuffer();//untuk menampung string dalam bufer
        while ((barisData = bufferBaca.readLine()) != null) {//jika barisdata ada
          //  barisData = barisData.toLowerCase();
            content.append(barisData);//mencetak baris kata dalam dokumen
            content.append(System.getProperty("line.separator"));
        }
        return content.toString();
    }
    
    public String caseFolding(String inputFile) throws FileNotFoundException, IOException{
        return inputFile.toLowerCase();
    }

    public String bersihkanStopword(String inputdokumen) throws FileNotFoundException, IOException {
        String daftarstopword = readDokumenTeks("stopwords.txt");
        String stop = "\\b(" + daftarstopword + ")\\b\\s*";
        Pattern stopword = Pattern.compile(stop, Pattern.CASE_INSENSITIVE);
        Matcher sesuai = stopword.matcher(inputdokumen);
        bersih = sesuai.replaceAll("");
        return bersih;
    }

    public static String HapusAngkadanTandaBaca(String kalimat) {
        kalimat = kalimat.replaceAll("\\p{Punct}|\\d", " ");
        return kalimat;
    }

    public ArrayList<String> token(String kalimat) {
        String hasilKalimat = HapusAngkadanTandaBaca(kalimat);
        String hapusNewline = hasilKalimat.replace(System.getProperty("line.separator"), " ");
        listKata = new ArrayList<String>();
        token = new StringTokenizer(hapusNewline, " ");
        while (token.hasMoreTokens()) {
            words = token.nextToken();
            listKata.add(words);
        }
        return listKata;
    }

    public HashMap frekuensi(ArrayList<String> input) throws FileNotFoundException {
        map = new HashMap<String, Integer>();
        for (int i = 0; i < input.size(); i++) {
            String kata = input.get(i);
            Integer jumlah = map.get(kata);
            if (map.containsKey(kata)) {
                map.put(kata, jumlah + 1);
            } else {
                map.put(kata, 1);
            }
        }
        return map;
    }

}
