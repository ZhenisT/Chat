package server;

import java.sql.*;

public class AuthService {
    private static Connection connection;
    private static Statement stmt;

    public static void connect() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:main.db");
            stmt = connection.createStatement();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void disconnect(){
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getNickByLoginAndPass(String login, String pass){
        String sql = String.format("SELECT nickname FROM main\n" +
                "WHERE login = '%s'\n" +
                "AND password = '%s'",login,pass);
        try {
            ResultSet rs = stmt.executeQuery(sql);

            if(rs.next()){
                return rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void addMessageToDB(String sender, String receiver, String text, String date){
        String sql = String.format("INSERT INTO messages(sender, receiver,text,date)\n" +
                "VALUES('%s','%s','%s','%s')",sender,receiver,text,date);
        try {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getMessagesFromDBForNick(String nick){
        String sql = String.format("SELECT * FROM messages\n" +
                "WHERE sender='%s'\n" +
                "OR receiver = '%s'\n" +
                "OR receiver = 'null'", nick,nick);

        StringBuilder sb = new StringBuilder();

        try {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()){
                String sender = rs.getString(2);
                String receiver = rs.getString(3);
                String text = rs.getString(4);
                String date = rs.getString(5);

                if(receiver.equals("null")){
                    sb.append(sender +" : "+text + "\n");
                }else {
                    sb.append("private ["+ sender +" ] to [ "+receiver+" ] :"+ text + "\n");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

}
