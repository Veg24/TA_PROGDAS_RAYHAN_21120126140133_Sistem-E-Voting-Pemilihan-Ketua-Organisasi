package strukturdata;

// Modul 7: Stack (LIFO) menggunakan array manual untuk undo
public class RiwayatAksi {
    // Modul 1: Array manual untuk stack
    private String[] data;
    private int top;
    private int kapasitas;

    public RiwayatAksi(int kapasitas) {
        this.kapasitas = kapasitas;
        this.data = new String[kapasitas];
        this.top = -1;
    }

    public boolean isEmpty() {
        return top == -1;
    }

    public boolean isFull() {
        return top == kapasitas - 1;
    }

    // Simpan aksi ke stack (push)
    public boolean push(String aksi) {
        if (isFull()) return false;
        top++;
        data[top] = aksi;
        return true;
    }

    // Ambil aksi terakhir untuk dibatalkan (pop)
    public String pop() {
        if (isEmpty()) return null;
        String aksi = data[top];
        data[top] = null;
        top--;
        return aksi;
    }

    // Modul 3: Perulangan for untuk baca riwayat dari atas ke bawah
    public String[] getSemuaRiwayat() {
        int ukuran = top + 1;
        String[] hasil = new String[ukuran];
        int idx = 0;
        for (int i = top; i >= 0; i--) {
            hasil[idx] = data[i];
            idx++;
        }
        return hasil;
    }

    public void clear() {
        top = -1;
    }
}
