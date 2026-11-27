package model;

// Modul 5: OOP I (Class & Enkapsulasi)
public class Kandidat {
    private int nomorUrut;
    private String nama;
    private String visi;
    private int jumlahSuara;

    public Kandidat(int nomorUrut, String nama, String visi) {
        this.nomorUrut = nomorUrut;
        this.nama = nama;
        this.visi = visi;
        this.jumlahSuara = 0;
    }

    public int getNomorUrut() {
        return nomorUrut;
    }

    public void setNomorUrut(int nomorUrut) {
        this.nomorUrut = nomorUrut;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getVisi() {
        return visi;
    }

    public void setVisi(String visi) {
        this.visi = visi;
    }

    public int getJumlahSuara() {
        return jumlahSuara;
    }

    public void setJumlahSuara(int jumlahSuara) {
        this.jumlahSuara = jumlahSuara;
    }

    public void tambahSuara() {
        this.jumlahSuara++;
    }

    public void kurangSuara() {
        if (this.jumlahSuara > 0) {
            this.jumlahSuara--;
        }
    }
}
