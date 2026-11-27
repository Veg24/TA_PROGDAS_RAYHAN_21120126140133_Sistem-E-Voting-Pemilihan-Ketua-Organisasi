package ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Pemilih;
import service.VotingService;

// Modul 8: GUI JavaFX Main
public class MainApp extends Application {
    private Stage stage;
    private VotingService service;

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        this.service = new VotingService();
        tampilkanLogin();
    }

    public void tampilkanLogin() {
        Scene scene = new Scene(new LoginView(this, service).getView(), 440, 380);
        stage.setTitle("Sistem E-Voting - Login");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    public void tampilkanAdmin() {
        Scene scene = new Scene(new AdminView(this, service).getView(), 880, 600);
        stage.setTitle("Sistem E-Voting - Panel Administrator");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    public void tampilkanPemilih(Pemilih p) {
        Scene scene = new Scene(new PemilihView(this, service, p).getView(), 700, 480);
        stage.setTitle("Sistem E-Voting - Bilik Suara");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
