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

// Modul 8: GUI Bilik Suara
public class PemilihView {
    private MainApp mainApp;
    private VotingService votingService;
    private Pemilih pemilih;
    private TableView<Kandidat> tabel = new TableView<>();

    public PemilihView(MainApp mainApp, VotingService votingService, Pemilih pemilih) {
        this.mainApp = mainApp;
        this.votingService = votingService;
        this.pemilih = pemilih;
    }

    public Parent getView() {
        VBox root = new VBox(12);
        root.setPadding(new Insets(20));

        Label lblSelamat = new Label("Bilik Suara - " + pemilih.getNama() + " (" + pemilih.getNim() + ")");
        lblSelamat.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        Label lblPetunjuk = new Label("Pilih salah satu kandidat di tabel lalu klik tombol 'Pilih Kandidat':");

        TableColumn<Kandidat, Integer> cNo = new TableColumn<>("No. Urut");
        cNo.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getNomorUrut()).asObject());
        cNo.setPrefWidth(80);

        TableColumn<Kandidat, String> cNama = new TableColumn<>("Nama Kandidat");
        cNama.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNama()));
        cNama.setPrefWidth(200);

        TableColumn<Kandidat, String> cVisi = new TableColumn<>("Visi");
        cVisi.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getVisi()));
        cVisi.setPrefWidth(350);

        tabel.getColumns().addAll(cNo, cNama, cVisi);
        tabel.setItems(FXCollections.observableArrayList(votingService.getDaftarKandidat()));
        VBox.setVgrow(tabel, Priority.ALWAYS);

        Button btnPilih = new Button("Pilih Kandidat");
        btnPilih.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold;");
        btnPilih.setOnAction(e -> coblos());

        Button btnKembali = new Button("Kembali");
        btnKembali.setOnAction(e -> mainApp.tampilkanLogin());

        HBox boxTombol = new HBox(15, btnPilih, btnKembali);
        boxTombol.setAlignment(Pos.CENTER);

        root.getChildren().addAll(lblSelamat, lblPetunjuk, tabel, boxTombol);
        return root;
    }

    private void coblos() {
        Kandidat k = tabel.getSelectionModel().getSelectedItem();
        if (k == null) {
            new Alert(Alert.AlertType.WARNING, "Pilih salah satu kandidat terlebih dahulu!").showAndWait();
            return;
        }

        Alert konfirmasi = new Alert(Alert.AlertType.CONFIRMATION, "Yakin memilih " + k.getNama() + " (No. " + k.getNomorUrut() + ")?", ButtonType.YES, ButtonType.NO);
        konfirmasi.showAndWait();

        if (konfirmasi.getResult() == ButtonType.YES) {
            String hasil = votingService.prosesVoting(pemilih.getNim(), k.getNomorUrut());
            if (hasil.equals("SUKSES")) {
                new Alert(Alert.AlertType.INFORMATION, "Terima kasih! Suara Anda telah dicatat.").showAndWait();
                mainApp.tampilkanLogin();
            } else {
                new Alert(Alert.AlertType.ERROR, hasil).showAndWait();
            }
        }
    }
}
