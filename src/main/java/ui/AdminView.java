package ui;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import model.Kandidat;
import model.Pemilih;
import service.VotingService;

// Modul 8: GUI Dashboard Admin
public class AdminView {
    private MainApp mainApp;
    private VotingService service;

    // Tab 1: Kandidat
    private TableView<Kandidat> tabelKandidat = new TableView<>();
    private TextField txtNoKandidat = new TextField();
    private TextField txtNamaKandidat = new TextField();
    private TextField txtVisiKandidat = new TextField();

    // Tab 2: Pemilih
    private TableView<Pemilih> tabelPemilih = new TableView<>();
    private TextField txtNimPemilih = new TextField();
    private TextField txtNamaPemilih = new TextField();

    // Tab 3: Hasil
    private TableView<Kandidat> tabelHasil = new TableView<>();
    private Label lblTotal = new Label();
    private TextArea areaPemenang = new TextArea();

    // Tab 4: Queue dan Stack
    private TableView<Pemilih> tabelAntrean = new TableView<>();
    private ComboBox<Pemilih> cbPemilih = new ComboBox<>();
    private ListView<String> listRiwayat = new ListView<>();

    public AdminView(MainApp mainApp, VotingService service) {
        this.mainApp = mainApp;
        this.service = service;
    }

    public Parent getView() {
        BorderPane root = new BorderPane();

        Label lblJudul = new Label("Panel Administrator E-Voting");
        lblJudul.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        Button btnLogout = new Button("Logout");
        btnLogout.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
        btnLogout.setOnAction(e -> mainApp.tampilkanLogin());

        BorderPane header = new BorderPane();
        header.setLeft(lblJudul);
        header.setRight(btnLogout);
        header.setPadding(new Insets(10, 15, 10, 15));
        root.setTop(header);

        TabPane tabPane = new TabPane(
            buatTabKandidat(),
            buatTabPemilih(),
            buatTabHasil(),
            buatTabQueueDanStack()
        );
        root.setCenter(tabPane);
        return root;
    }

    // Tab 1: Kelola Kandidat
    private Tab buatTabKandidat() {
        Tab tab = new Tab("Kelola Kandidat");
        tab.setClosable(false);

        VBox box = new VBox(10);
        box.setPadding(new Insets(15));

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(8);
        grid.addRow(0, new Label("No. Urut:"), txtNoKandidat, new Label("Nama:"), txtNamaKandidat);
        grid.addRow(1, new Label("Visi:"), txtVisiKandidat);
        GridPane.setColumnSpan(txtVisiKandidat, 3);
        txtVisiKandidat.setPrefWidth(380);

        Button btnTambah = new Button("Tambah");
        Button btnUbah = new Button("Ubah");
        Button btnHapus = new Button("Hapus");
        Button btnReset = new Button("Bersihkan");
        btnTambah.setOnAction(e -> tambahKandidat());
        btnUbah.setOnAction(e -> ubahKandidat());
        btnHapus.setOnAction(e -> hapusKandidat());
        btnReset.setOnAction(e -> bersihkanKandidat());
        HBox boxBtn = new HBox(8, btnTambah, btnUbah, btnHapus, btnReset);

        TableColumn<Kandidat, Integer> cNo = new TableColumn<>("No. Urut");
        cNo.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getNomorUrut()).asObject());
        TableColumn<Kandidat, String> cNama = new TableColumn<>("Nama");
        cNama.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNama()));
        TableColumn<Kandidat, String> cVisi = new TableColumn<>("Visi");
        cVisi.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getVisi()));
        TableColumn<Kandidat, Integer> cSuara = new TableColumn<>("Suara");
        cSuara.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getJumlahSuara()).asObject());

        tabelKandidat.getColumns().addAll(cNo, cNama, cVisi, cSuara);
        tabelKandidat.getSelectionModel().selectedItemProperty().addListener((obs, l, b) -> {
            if (b != null) {
                txtNoKandidat.setText(String.valueOf(b.getNomorUrut()));
                txtNoKandidat.setDisable(true);
                txtNamaKandidat.setText(b.getNama());
                txtVisiKandidat.setText(b.getVisi());
            }
        });
        VBox.setVgrow(tabelKandidat, Priority.ALWAYS);

        box.getChildren().addAll(grid, boxBtn, tabelKandidat);
        refreshKandidat();
        tab.setContent(box);
        return tab;
    }

    private void tambahKandidat() {
        try {
            int no = Integer.parseInt(txtNoKandidat.getText().trim());
            String nama = txtNamaKandidat.getText().trim();
            String visi = txtVisiKandidat.getText().trim();
            if (nama.isEmpty() || visi.isEmpty()) { pesan("Peringatan", "Semua kolom wajib diisi!"); return; }
            if (service.tambahKandidat(no, nama, visi)) {
                refreshKandidat();
                bersihkanKandidat();
            } else {
                pesan("Gagal", "Nomor urut sudah digunakan!");
            }
        } catch (NumberFormatException ex) {
            pesan("Gagal", "Nomor urut harus berupa angka!");
        }
    }

    private void ubahKandidat() {
        Kandidat k = tabelKandidat.getSelectionModel().getSelectedItem();
        if (k == null) return;
        service.ubahKandidat(k.getNomorUrut(), txtNamaKandidat.getText().trim(), txtVisiKandidat.getText().trim());
        refreshKandidat();
        bersihkanKandidat();
    }

    private void hapusKandidat() {
        Kandidat k = tabelKandidat.getSelectionModel().getSelectedItem();
        if (k == null) return;
        service.hapusKandidat(k.getNomorUrut());
        refreshKandidat();
        bersihkanKandidat();
    }

    private void bersihkanKandidat() {
        txtNoKandidat.clear(); txtNoKandidat.setDisable(false);
        txtNamaKandidat.clear(); txtVisiKandidat.clear();
        tabelKandidat.getSelectionModel().clearSelection();
    }

    private void refreshKandidat() {
        tabelKandidat.setItems(FXCollections.observableArrayList(service.getDaftarKandidat()));
    }

    // Tab 2: Kelola Pemilih
    private Tab buatTabPemilih() {
        Tab tab = new Tab("Kelola Pemilih");
        tab.setClosable(false);

        VBox box = new VBox(10);
        box.setPadding(new Insets(15));

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(8);
        grid.addRow(0, new Label("NIM:"), txtNimPemilih, new Label("Nama:"), txtNamaPemilih);
        txtNimPemilih.setPromptText("Contoh: 2026101");

        Button btnTambah = new Button("Tambah Pemilih");
        Button btnHapus = new Button("Hapus");
        btnTambah.setOnAction(e -> {
            String nim = txtNimPemilih.getText().trim();
            String nama = txtNamaPemilih.getText().trim();
            if (nim.isEmpty() || nama.isEmpty()) { pesan("Peringatan", "NIM dan nama harus diisi!"); return; }
            if (service.tambahPemilih(nim, nama)) {
                refreshPemilih();
                txtNimPemilih.clear(); txtNamaPemilih.clear();
            } else {
                pesan("Gagal", "NIM sudah terdaftar!");
            }
        });
        btnHapus.setOnAction(e -> {
            Pemilih p = tabelPemilih.getSelectionModel().getSelectedItem();
            if (p != null) { service.hapusPemilih(p.getNim()); refreshPemilih(); }
        });
        HBox boxBtn = new HBox(8, btnTambah, btnHapus);

        TableColumn<Pemilih, String> cNim = new TableColumn<>("NIM");
        cNim.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNim()));
        TableColumn<Pemilih, String> cNama = new TableColumn<>("Nama");
        cNama.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNama()));
        TableColumn<Pemilih, String> cPass = new TableColumn<>("Password");
        cPass.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getPassword()));
        TableColumn<Pemilih, String> cStatus = new TableColumn<>("Status");
        cStatus.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getStatusTeks()));

        tabelPemilih.getColumns().addAll(cNim, cNama, cPass, cStatus);
        VBox.setVgrow(tabelPemilih, Priority.ALWAYS);

        box.getChildren().addAll(grid, boxBtn, tabelPemilih);
        refreshPemilih();
        tab.setContent(box);
        return tab;
    }

    private void refreshPemilih() {
        tabelPemilih.setItems(FXCollections.observableArrayList(service.getDaftarPemilih()));
        cbPemilih.setItems(FXCollections.observableArrayList(service.getDaftarPemilih()));
    }

    // Tab 3: Hasil Pemilihan
    private Tab buatTabHasil() {
        Tab tab = new Tab("Hasil Pemilihan");
        tab.setClosable(false);

        VBox box = new VBox(10);
        box.setPadding(new Insets(15));

        Button btnRefresh = new Button("Refresh");
        btnRefresh.setOnAction(e -> refreshHasil());
        Button btnReset = new Button("Reset Suara");
        btnReset.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white;");
        btnReset.setOnAction(e -> {
            service.resetSuara();
            refreshHasil();
            refreshKandidat();
            refreshPemilih();
            refreshQueueStack();
        });
        HBox topBox = new HBox(15, lblTotal, btnRefresh, btnReset);
        topBox.setAlignment(Pos.CENTER_LEFT);

        TableColumn<Kandidat, Integer> cNo = new TableColumn<>("No. Urut");
        cNo.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getNomorUrut()).asObject());
        TableColumn<Kandidat, String> cNama = new TableColumn<>("Nama Kandidat");
        cNama.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNama()));
        TableColumn<Kandidat, Integer> cSuara = new TableColumn<>("Jumlah Suara");
        cSuara.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getJumlahSuara()).asObject());
        TableColumn<Kandidat, String> cPersen = new TableColumn<>("Persentase (%)");
        cPersen.setCellValueFactory(d -> new SimpleStringProperty(String.format("%.1f%%", service.hitungPersentase(d.getValue()))));

        tabelHasil.getColumns().addAll(cNo, cNama, cSuara, cPersen);
        VBox.setVgrow(tabelHasil, Priority.ALWAYS);

        areaPemenang.setEditable(false);
        areaPemenang.setPrefRowCount(2);
        areaPemenang.setStyle("-fx-font-weight: bold; -fx-text-fill: #27ae60;");

        box.getChildren().addAll(topBox, tabelHasil, new Label("Hasil Akhir:"), areaPemenang);
        tab.setOnSelectionChanged(e -> { if (tab.isSelected()) refreshHasil(); });
        refreshHasil();
        tab.setContent(box);
        return tab;
    }

    private void refreshHasil() {
        tabelHasil.setItems(FXCollections.observableArrayList(service.getDaftarKandidat()));
        lblTotal.setText("Total Suara Masuk: " + service.hitungTotalSuara());
        areaPemenang.setText(service.cariPemenang());
    }

    // Tab 4: Queue dan Stack (Modul 7)
    private Tab buatTabQueueDanStack() {
        Tab tab = new Tab("Antrean & Riwayat (Stack & Queue)");
        tab.setClosable(false);

        HBox root = new HBox(15);
        root.setPadding(new Insets(15));

        // Queue: Antrean Bilik Suara
        VBox boxQueue = new VBox(8);
        HBox.setHgrow(boxQueue, Priority.ALWAYS);
        Label lblQ = new Label("1. Antrean Bilik Suara (Queue - FIFO)");
        lblQ.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));

        Button btnEnqueue = new Button("Masuk Antrean");
        btnEnqueue.setOnAction(e -> {
            Pemilih p = cbPemilih.getSelectionModel().getSelectedItem();
            if (p != null && service.masukkanAntrean(p.getNim())) refreshQueueStack();
            else pesan("Peringatan", "Pilih pemilih yang belum memilih!");
        });
        HBox boxInputQ = new HBox(8, cbPemilih, btnEnqueue);

        TableColumn<Pemilih, String> cNimQ = new TableColumn<>("NIM");
        cNimQ.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNim()));
        TableColumn<Pemilih, String> cNamaQ = new TableColumn<>("Nama");
        cNamaQ.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNama()));
        tabelAntrean.getColumns().addAll(cNimQ, cNamaQ);
        VBox.setVgrow(tabelAntrean, Priority.ALWAYS);

        Button btnDequeue = new Button("Panggil Pemilih Terdepan (Dequeue)");
        btnDequeue.setOnAction(e -> {
            Pemilih p = service.panggilAntreanBerikutnya();
            if (p != null) pesan("Panggilan Bilik Suara", "Silakan " + p.getNama() + " (" + p.getNim() + ") masuk ke bilik suara.");
            refreshQueueStack();
        });
        boxQueue.getChildren().addAll(lblQ, boxInputQ, tabelAntrean, btnDequeue);

        // Stack: Log Aktivitas dan Undo
        VBox boxStack = new VBox(8);
        HBox.setHgrow(boxStack, Priority.ALWAYS);
        Label lblS = new Label("2. Log Aktivitas & Undo (Stack - LIFO)");
        lblS.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));

        VBox.setVgrow(listRiwayat, Priority.ALWAYS);
        Button btnUndo = new Button("Batalkan Aksi Terakhir (Undo)");
        btnUndo.setStyle("-fx-background-color: #e67e22; -fx-text-fill: white; -fx-font-weight: bold;");
        btnUndo.setOnAction(e -> {
            String hasil = service.undoAksiTerakhir();
            pesan("Hasil Undo", hasil);
            refreshKandidat(); refreshPemilih(); refreshHasil(); refreshQueueStack();
        });
        boxStack.getChildren().addAll(lblS, listRiwayat, btnUndo);

        root.getChildren().addAll(boxQueue, boxStack);
        tab.setOnSelectionChanged(e -> { if (tab.isSelected()) refreshQueueStack(); });
        refreshQueueStack();
        tab.setContent(root);
        return tab;
    }

    private void refreshQueueStack() {
        tabelAntrean.setItems(FXCollections.observableArrayList(service.getAntreanPemilih().getSemuaAntrean()));
        listRiwayat.setItems(FXCollections.observableArrayList(service.getRiwayatAksi().getSemuaRiwayat()));
    }

    private void pesan(String judul, String isi) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(judul); a.setHeaderText(null); a.setContentText(isi);
        a.showAndWait();
    }
}
