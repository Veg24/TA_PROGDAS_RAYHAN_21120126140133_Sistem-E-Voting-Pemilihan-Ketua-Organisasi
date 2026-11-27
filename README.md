# E-VOTING – Sistem Pemilihan Ketua Organisasi

Vega Muhammad Rayhan Mukhtas (21120126140133)

## Deskripsi

E-VOTING membantu organisasi melaksanakan pemilihan ketua secara tertib, transparan, dan efisien. Admin mengelola data kandidat dan pemilih serta memanggil antrean pemilih ke bilik suara secara berurutan. Pemilih login menggunakan NIM dan password untuk menyalurkan satu hak suaranya. Perolehan suara direkap secara otomatis beserta persentase dan penentuan pemenangnya. Kesalahan aksi dapat dibatalkan menggunakan fitur Undo, dan seluruh data tetap tersimpan di file walaupun aplikasi ditutup.

## Penerapan Modul

| Modul | Penerapan |
|---|---|
| 1. Variabel, Tipe Data, Array | data kandidat dan perolehan suara, array pada queue dan stack |
| 2. Pengkondisian | validasi login, validasi status sudah memilih, penentuan pemenang |
| 3. Perulangan | menampilkan antrean bilik suara, membaca dan menulis file database |
| 4. Function & Method | fungsi dan method pada setiap class |
| 5. OOP I | class `Kandidat`, `Pemilih` |
| 6. OOP II | abstract class `Pengguna`, turunan `Admin` dan `Pemilih` |
| 7. Stack & Queue | `AntreanPemilih` (queue bilik suara), `RiwayatAksi` (stack untuk undo) |
| 8. GUI | antarmuka JavaFX (`MainApp`) |

## Cara Menjalankan

Butuh JDK 21 dan Maven (atau IntelliJ IDEA).

```bash
mvn javafx:run
```

### Akun Pengguna
- **Admin:** Username `admin` | Password `admin123`
- **Pemilih:** NIM `2026001` s.d. `2026100` | Password `123`
