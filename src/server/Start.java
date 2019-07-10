package server;

import java.sql.SQLException;

public class Start {
    public static void main(String[] args) {
        try {
            new Server();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
