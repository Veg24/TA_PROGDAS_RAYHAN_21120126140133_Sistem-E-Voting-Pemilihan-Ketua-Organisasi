package model;

// Modul 6: Abstract Class
public abstract class Pengguna {
    // Modul 5: Enkapsulasi
    private String nama;

    public Pengguna(String nama) {
        this.nama = nama;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    // Modul 6: Abstract method
    public abstract String getPeran();
}
