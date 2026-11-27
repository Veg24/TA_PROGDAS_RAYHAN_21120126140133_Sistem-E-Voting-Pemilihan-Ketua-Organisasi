package model;

// Modul 6: Inheritance (Turunan dari Pengguna)
public class Pemilih extends Pengguna {
    // Modul 5: Enkapsulasi
    private String nim;
    private String password;
    private boolean sudahMemilih;

    public Pemilih(String nim, String nama, String password) {
        super(nama);
        this.nim = nim;
        this.password = password;
        this.sudahMemilih = false;
    }

    @Override
    public String getPeran() {
        return "Pemilih";
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isSudahMemilih() {
        return sudahMemilih;
    }

    public void setSudahMemilih(boolean sudahMemilih) {
        this.sudahMemilih = sudahMemilih;
    }

    // Modul 2: Pengkondisian
    public String getStatusTeks() {
        return sudahMemilih ? "Sudah Memilih" : "Belum Memilih";
    }

    @Override
    public String toString() {
        return nim + " - " + getNama();
    }
}
