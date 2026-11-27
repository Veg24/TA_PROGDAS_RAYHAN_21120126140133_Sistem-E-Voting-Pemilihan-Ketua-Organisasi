package strukturdata;

import model.Pemilih;

// Modul 7: Queue (FIFO) menggunakan array manual
public class AntreanPemilih {
    // Modul 1: Array manual untuk queue
    private Pemilih[] data;
    private int depan;
    private int belakang;
    private int jumlah;
    private int kapasitas;

    public AntreanPemilih(int kapasitas) {
        this.kapasitas = kapasitas;
        this.data = new Pemilih[kapasitas];
        this.depan = 0;
        this.belakang = -1;
        this.jumlah = 0;
    }

    public boolean isEmpty() {
        return jumlah == 0;
    }

    public boolean isFull() {
        return jumlah == kapasitas;
    }

    // Masuk antrean (enqueue)
    public boolean enqueue(Pemilih p) {
        if (isFull()) return false;
        belakang = (belakang + 1) % kapasitas;
        data[belakang] = p;
        jumlah++;
        return true;
    }

    // Keluar antrean untuk masuk bilik (dequeue)
    public Pemilih dequeue() {
        if (isEmpty()) return null;
        Pemilih p = data[depan];
        data[depan] = null;
        depan = (depan + 1) % kapasitas;
        jumlah--;
        return p;
    }

    // Modul 3: Perulangan while untuk ambil data antrean
    public Pemilih[] getSemuaAntrean() {
        Pemilih[] hasil = new Pemilih[jumlah];
        int idx = depan;
        int i = 0;
        while (i < jumlah) {
            hasil[i] = data[idx];
            idx = (idx + 1) % kapasitas;
            i++;
        }
        return hasil;
    }

    public void clear() {
        depan = 0;
        belakang = -1;
        jumlah = 0;
    }
}
