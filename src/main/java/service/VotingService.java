package service;

import model.Kandidat;
import model.Pemilih;
import strukturdata.AntreanPemilih;
import strukturdata.RiwayatAksi;

import java.util.ArrayList;

// Modul 4: Function & Method, Modul 2: Pengkondisian, Modul 7: Queue & Stack
public class VotingService {
    private ArrayList<Kandidat> daftarKandidat;
    private ArrayList<Pemilih> daftarPemilih;
    private AntreanPemilih antreanPemilih;
    private RiwayatAksi riwayatAksi;

    public VotingService() {
        antreanPemilih = new AntreanPemilih(150);
        riwayatAksi = new RiwayatAksi(150);
        muatData();
    }

    public void muatData() {
        FileManager.buatDataContoh();
        daftarKandidat = FileManager.bacaKandidat();
        daftarPemilih = FileManager.bacaPemilih();
    }

    public ArrayList<Kandidat> getDaftarKandidat() { return daftarKandidat; }
    public ArrayList<Pemilih> getDaftarPemilih() { return daftarPemilih; }
    public AntreanPemilih getAntreanPemilih() { return antreanPemilih; }
    public RiwayatAksi getRiwayatAksi() { return riwayatAksi; }

    // Kelola data kandidat
    public Kandidat cariKandidat(int nomorUrut) {
        for (int i = 0; i < daftarKandidat.size(); i++) {
            if (daftarKandidat.get(i).getNomorUrut() == nomorUrut) return daftarKandidat.get(i);
        }
        return null;
    }

    public boolean tambahKandidat(int nomorUrut, String nama, String visi) {
        if (cariKandidat(nomorUrut) != null) return false;
        daftarKandidat.add(new Kandidat(nomorUrut, nama, visi));
        FileManager.simpanKandidat(daftarKandidat);
        riwayatAksi.push("TAMBAH_KANDIDAT," + nomorUrut);
        return true;
    }

    public boolean ubahKandidat(int nomorUrut, String namaBaru, String visiBaru) {
        Kandidat k = cariKandidat(nomorUrut);
        if (k == null) return false;
        k.setNama(namaBaru);
        k.setVisi(visiBaru);
        FileManager.simpanKandidat(daftarKandidat);
        return true;
    }

    public boolean hapusKandidat(int nomorUrut) {
        Kandidat k = cariKandidat(nomorUrut);
        if (k == null) return false;
        daftarKandidat.remove(k);
        FileManager.simpanKandidat(daftarKandidat);
        return true;
    }

    // Kelola data pemilih
    public Pemilih cariPemilih(String nim) {
        for (int i = 0; i < daftarPemilih.size(); i++) {
            if (daftarPemilih.get(i).getNim().equals(nim)) return daftarPemilih.get(i);
        }
        return null;
    }

    public boolean tambahPemilih(String nim, String nama) {
        if (cariPemilih(nim) != null) return false;
        daftarPemilih.add(new Pemilih(nim, nama, "123"));
        FileManager.simpanPemilih(daftarPemilih);
        riwayatAksi.push("TAMBAH_PEMILIH," + nim);
        return true;
    }

    public boolean hapusPemilih(String nim) {
        Pemilih p = cariPemilih(nim);
        if (p == null) return false;
        daftarPemilih.remove(p);
        FileManager.simpanPemilih(daftarPemilih);
        return true;
    }

    // Modul 2: Pengkondisian validasi login
    public String validasiLoginPemilih(String nim, String password) {
        if (nim.isEmpty() || password.isEmpty()) return "NIM dan password tidak boleh kosong!";
        Pemilih p = cariPemilih(nim);
        if (p == null) return "NIM tidak terdaftar!";
        if (!p.getPassword().equals(password)) return "Password salah!";
        if (p.isSudahMemilih()) return "Anda sudah menggunakan hak suara!";
        return "SUKSES";
    }

    // Modul 7: Queue bilik suara
    public boolean masukkanAntrean(String nim) {
        Pemilih p = cariPemilih(nim);
        if (p == null || p.isSudahMemilih()) return false;
        return antreanPemilih.enqueue(p);
    }

    public Pemilih panggilAntreanBerikutnya() {
        return antreanPemilih.dequeue();
    }

    // Proses voting
    public String prosesVoting(String nim, int nomorUrut) {
        Pemilih p = cariPemilih(nim);
        if (p == null) return "Pemilih tidak terdaftar!";
        if (p.isSudahMemilih()) return "Anda sudah memilih!";

        Kandidat k = cariKandidat(nomorUrut);
        if (k == null) return "Kandidat tidak ditemukan!";

        k.tambahSuara();
        p.setSudahMemilih(true);

        FileManager.simpanKandidat(daftarKandidat);
        FileManager.simpanPemilih(daftarPemilih);
        FileManager.simpanSuara(nim, nomorUrut);

        riwayatAksi.push("VOTE," + nim + "," + nomorUrut);
        return "SUKSES";
    }

    // Modul 7: Stack undo
    public String undoAksiTerakhir() {
        if (riwayatAksi.isEmpty()) return "Riwayat kosong, tidak ada aksi yang bisa dibatalkan.";

        String aksi = riwayatAksi.pop();
        String[] data = aksi.split(",");

        // Modul 2: Switch case pembatalan aksi
        switch (data[0]) {
            case "VOTE":
                String nim = data[1];
                int no = Integer.parseInt(data[2]);
                Kandidat k = cariKandidat(no);
                if (k != null) k.kurangSuara();
                Pemilih p = cariPemilih(nim);
                if (p != null) p.setSudahMemilih(false);
                FileManager.simpanKandidat(daftarKandidat);
                FileManager.simpanPemilih(daftarPemilih);
                return "Undo: Suara pemilih " + nim + " dibatalkan.";

            case "TAMBAH_KANDIDAT":
                hapusKandidat(Integer.parseInt(data[1]));
                return "Undo: Kandidat nomor " + data[1] + " dihapus.";

            case "TAMBAH_PEMILIH":
                hapusPemilih(data[1]);
                return "Undo: Pemilih NIM " + data[1] + " dihapus.";

            default:
                return "Aksi tidak dikenal: " + aksi;
        }
    }

    // Modul 3: Perulangan for-each untuk rekap suara
    public int hitungTotalSuara() {
        int total = 0;
        for (Kandidat k : daftarKandidat) {
            total += k.getJumlahSuara();
        }
        return total;
    }

    public double hitungPersentase(Kandidat k) {
        int total = hitungTotalSuara();
        return total == 0 ? 0.0 : (double) k.getJumlahSuara() / total * 100;
    }

    public String cariPemenang() {
        if (daftarKandidat.isEmpty()) return "Belum ada kandidat.";
        int total = hitungTotalSuara();
        if (total == 0) return "Belum ada suara masuk.";

        int maks = 0;
        for (Kandidat k : daftarKandidat) {
            if (k.getJumlahSuara() > maks) maks = k.getJumlahSuara();
        }

        ArrayList<Kandidat> pemenang = new ArrayList<Kandidat>();
        for (Kandidat k : daftarKandidat) {
            if (k.getJumlahSuara() == maks) pemenang.add(k);
        }

        if (pemenang.size() == 1) {
            Kandidat p = pemenang.get(0);
            return "PEMENANG: " + p.getNama() + " (" + p.getJumlahSuara() + " suara)";
        } else {
            String s = "HASIL SERI (" + maks + " suara): ";
            for (Kandidat p : pemenang) s += p.getNama() + ", ";
            return s.substring(0, s.length() - 2);
        }
    }

    public void resetSuara() {
        // Modul 3: Perulangan while
        int i = 0;
        while (i < daftarKandidat.size()) {
            daftarKandidat.get(i).setJumlahSuara(0);
            i++;
        }
        for (Pemilih p : daftarPemilih) p.setSudahMemilih(false);

        FileManager.simpanKandidat(daftarKandidat);
        FileManager.simpanPemilih(daftarPemilih);
        FileManager.resetFileSuara();
        antreanPemilih.clear();
        riwayatAksi.clear();
    }
}
