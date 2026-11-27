package service;

import model.Kandidat;
import model.Pemilih;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

// Pengelolaan baca dan tulis file data
public class FileManager {
    private static final String FOLDER = "data";
    private static final String FILE_KANDIDAT = FOLDER + "/kandidat.txt";
    private static final String FILE_PEMILIH = FOLDER + "/pemilih.txt";
    private static final String FILE_SUARA = FOLDER + "/suara.txt";

    private static void cekFolder() {
        File f = new File(FOLDER);
        if (!f.exists()) f.mkdirs();
    }

    // Baca data kandidat dari file
    public static ArrayList<Kandidat> bacaKandidat() {
        ArrayList<Kandidat> daftar = new ArrayList<Kandidat>();
        File file = new File(FILE_KANDIDAT);
        if (!file.exists()) return daftar;

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String baris;
            // Modul 3: Perulangan while membaca baris file
            while ((baris = br.readLine()) != null) {
                baris = baris.trim();
                if (baris.isEmpty()) continue;
                String[] p = baris.split(",");
                if (p.length >= 3) {
                    Kandidat k = new Kandidat(Integer.parseInt(p[0]), p[1], p[2]);
                    if (p.length >= 4) k.setJumlahSuara(Integer.parseInt(p[3]));
                    daftar.add(k);
                }
            }
            br.close();
        } catch (IOException e) {
            System.out.println("Error baca kandidat: " + e.getMessage());
        }
        return daftar;
    }

    // Baca data pemilih dari file
    public static ArrayList<Pemilih> bacaPemilih() {
        ArrayList<Pemilih> daftar = new ArrayList<Pemilih>();
        File file = new File(FILE_PEMILIH);
        if (!file.exists()) return daftar;

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String baris;
            while ((baris = br.readLine()) != null) {
                baris = baris.trim();
                if (baris.isEmpty()) continue;
                String[] p = baris.split(",");
                if (p.length >= 3) {
                    String password = (p.length >= 4) ? p[2] : "123";
                    boolean status = Boolean.parseBoolean(p[p.length - 1]);
                    Pemilih pemilih = new Pemilih(p[0], p[1], password);
                    pemilih.setSudahMemilih(status);
                    daftar.add(pemilih);
                }
            }
            br.close();
        } catch (IOException e) {
            System.out.println("Error baca pemilih: " + e.getMessage());
        }
        return daftar;
    }

    // Simpan data kandidat ke file
    public static void simpanKandidat(ArrayList<Kandidat> daftar) {
        cekFolder();
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_KANDIDAT));
            for (int i = 0; i < daftar.size(); i++) {
                Kandidat k = daftar.get(i);
                bw.write(k.getNomorUrut() + "," + k.getNama() + "," + k.getVisi() + "," + k.getJumlahSuara());
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            System.out.println("Error simpan kandidat: " + e.getMessage());
        }
    }

    // Simpan data pemilih ke file
    public static void simpanPemilih(ArrayList<Pemilih> daftar) {
        cekFolder();
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PEMILIH));
            for (int i = 0; i < daftar.size(); i++) {
                Pemilih p = daftar.get(i);
                bw.write(p.getNim() + "," + p.getNama() + "," + p.getPassword() + "," + p.isSudahMemilih());
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            System.out.println("Error simpan pemilih: " + e.getMessage());
        }
    }

    // Simpan log suara masuk
    public static void simpanSuara(String nim, int nomorUrut) {
        cekFolder();
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_SUARA, true));
            bw.write(nim + "," + nomorUrut);
            bw.newLine();
            bw.close();
        } catch (IOException e) {
            System.out.println("Error simpan suara: " + e.getMessage());
        }
    }

    public static void resetFileSuara() {
        cekFolder();
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_SUARA));
            bw.write("");
            bw.close();
        } catch (IOException e) {
            System.out.println("Error reset suara: " + e.getMessage());
        }
    }

    // Data awal jika file belum ada
    public static void buatDataContoh() {
        cekFolder();

        File fk = new File(FILE_KANDIDAT);
        if (!fk.exists()) {
            ArrayList<Kandidat> listK = new ArrayList<Kandidat>();
            listK.add(new Kandidat(1, "Ahmad Fauzi", "Meningkatkan solidaritas dan kebersamaan"));
            listK.add(new Kandidat(2, "Siti Nurhaliza", "Mendorong inovasi dan kreativitas organisasi"));
            listK.add(new Kandidat(3, "Budi Santoso", "Memperluas jaringan kerjasama kampus"));
            simpanKandidat(listK);
        }

        File fp = new File(FILE_PEMILIH);
        if (!fp.exists()) {
            ArrayList<Pemilih> listP = new ArrayList<Pemilih>();
            String[] depan = {"Andi", "Budi", "Citra", "Dewi", "Eka", "Fajar", "Gilang", "Hendra", "Ilham", "Joko"};
            String[] belakang = {"Pratama", "Santoso", "Saputra", "Lestari", "Hidayat", "Kurniawan", "Nugroho", "Wibowo", "Ramadhan", "Putri"};

            for (int i = 1; i <= 100; i++) {
                String nim = String.format("2026%03d", i);
                String nama = depan[(i - 1) % 10] + " " + belakang[((i - 1) / 10) % 10];
                listP.add(new Pemilih(nim, nama, "123"));
            }
            simpanPemilih(listP);
        }
    }
}
