package model;

// Modul 6: Inheritance (Turunan dari Pengguna)
public class Admin extends Pengguna {
    // Modul 1: Variabel dan konstanta
    public static final String USERNAME = "admin";
    public static final String PASSWORD = "admin123";

    public Admin() {
        super("Administrator");
    }

    // Modul 6: Overriding method getPeran
    @Override
    public String getPeran() {
        return "Administrator";
    }

    // Modul 2 & 4: Validasi login admin
    public static boolean validasiLogin(String user, String pass) {
        return USERNAME.equals(user) && PASSWORD.equals(pass);
    }
}
