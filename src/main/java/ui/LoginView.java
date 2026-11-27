package ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import model.Admin;
import model.Pemilih;
import service.VotingService;

// Modul 8: GUI Form Login
public class LoginView {
    private MainApp mainApp;
    private VotingService votingService;

    private TextField txtUsername = new TextField();
    private PasswordField txtPasswordAdmin = new PasswordField();
    private TextField txtNim = new TextField();
    private PasswordField txtPasswordPemilih = new PasswordField();

    private RadioButton rbAdmin = new RadioButton("Admin");
    private RadioButton rbPemilih = new RadioButton("Pemilih");

    public LoginView(MainApp mainApp, VotingService votingService) {
        this.mainApp = mainApp;
        this.votingService = votingService;
    }

    public Parent getView() {
        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));

        Label lblJudul = new Label("SISTEM E-VOTING");
        lblJudul.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
        Label lblSub = new Label("Pemilihan Ketua Organisasi");
        lblSub.setFont(Font.font("Segoe UI", 13));

        ToggleGroup grup = new ToggleGroup();
        rbAdmin.setToggleGroup(grup);
        rbPemilih.setToggleGroup(grup);
        rbAdmin.setSelected(true);
        HBox boxPeran = new HBox(15, new Label("Masuk Sebagai:"), rbAdmin, rbPemilih);
        boxPeran.setAlignment(Pos.CENTER);

        GridPane gridAdmin = new GridPane();
        gridAdmin.setHgap(10); gridAdmin.setVgap(10); gridAdmin.setAlignment(Pos.CENTER);
        gridAdmin.addRow(0, new Label("Username:"), txtUsername);
        gridAdmin.addRow(1, new Label("Password:"), txtPasswordAdmin);

        GridPane gridPemilih = new GridPane();
        gridPemilih.setHgap(10); gridPemilih.setVgap(10); gridPemilih.setAlignment(Pos.CENTER);
        gridPemilih.addRow(0, new Label("NIM:"), txtNim);
        gridPemilih.addRow(1, new Label("Password:"), txtPasswordPemilih);
        txtNim.setPromptText("Contoh: 2026001");
        txtPasswordPemilih.setPromptText("Default: 123");

        StackPane formContainer = new StackPane(gridAdmin, gridPemilih);
        gridPemilih.setVisible(false);

        rbAdmin.setOnAction(e -> { gridAdmin.setVisible(true); gridPemilih.setVisible(false); });
        rbPemilih.setOnAction(e -> { gridAdmin.setVisible(false); gridPemilih.setVisible(true); });

        Button btnLogin = new Button("Masuk");
        btnLogin.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold;");
        btnLogin.setPrefWidth(180);
        btnLogin.setOnAction(e -> prosesLogin());

        root.getChildren().addAll(lblJudul, lblSub, boxPeran, formContainer, btnLogin);
        return root;
    }

    private void prosesLogin() {
        if (rbAdmin.isSelected()) {
            String u = txtUsername.getText().trim();
            String p = txtPasswordAdmin.getText().trim();
            if (Admin.validasiLogin(u, p)) {
                txtUsername.clear(); txtPasswordAdmin.clear();
                mainApp.tampilkanAdmin();
            } else {
                pesan(Alert.AlertType.ERROR, "Login Gagal", "Username atau password admin salah!");
            }
        } else {
            String nim = txtNim.getText().trim();
            String pass = txtPasswordPemilih.getText().trim();
            String status = votingService.validasiLoginPemilih(nim, pass);
            if (status.equals("SUKSES")) {
                Pemilih pemilih = votingService.cariPemilih(nim);
                txtNim.clear(); txtPasswordPemilih.clear();
                mainApp.tampilkanPemilih(pemilih);
            } else {
                pesan(Alert.AlertType.ERROR, "Login Gagal", status);
            }
        }
    }

    private void pesan(Alert.AlertType tipe, String judul, String isi) {
        Alert alert = new Alert(tipe);
        alert.setTitle(judul);
        alert.setHeaderText(null);
        alert.setContentText(isi);
        alert.showAndWait();
    }
}
