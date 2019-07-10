package client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class Controller {
    @FXML
    public TextArea textArea;
    @FXML
    public TextField textField;
    @FXML
    public Button btnSend;
    @FXML
    public HBox upperPanel;
    @FXML
    public TextField loginField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public HBox bottomPanel;
    @FXML
    public ListView<String> clientList;

    private Stage stage ;

//    добавил ник
    private String nick;

    private boolean isAuthorized;

    Socket socket;
    DataOutputStream out;
    DataInputStream in;

    final String IP_ADRESS = "localhost";
    final int PORT = 8189;



    public void setAuthorized(boolean isAuthorized){
        this.isAuthorized = isAuthorized;
        if(isAuthorized){
            upperPanel.setVisible(false);
            upperPanel.setManaged(false);
            bottomPanel.setVisible(true);
            bottomPanel.setManaged(true);
            clientList.setVisible(true);
            clientList.setManaged(true);
        }else {
            upperPanel.setVisible(true);
            upperPanel.setManaged(true);
            bottomPanel.setVisible(false);
            bottomPanel.setManaged(false);
            clientList.setVisible(false);
            clientList.setManaged(false);
        }
    }

    public void connect() {
        try {
            socket = new Socket(IP_ADRESS, PORT);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            new Thread(()->{
                    try {
                        //цикл авторизации
                        while(!isAuthorized){
                            String str = in.readUTF();
                            if (str.startsWith("/authok")) {
                                setAuthorized(true);
                                // получим ник
                                nick = str.split(" ")[1];
                                break;
                            }
                            textArea.appendText(str +"\n");
                        }


                        // добавим в заголовок nick пользователя
                        Platform.runLater(() ->{
                            getStage().setTitle("Супер чат. "+ nick);
                        } );

                        //цикл работы
                        while(isAuthorized){
                            String str = in.readUTF();
                            if (str.startsWith("/")){
                                if (str.equals("/end")) {
                                    System.out.println("Клиент отключился");
                                    break;
                                }
                                if (str.startsWith("/clientlist")) {
                                    String[] tokens = str.split(" ");
                                    //
                                    Platform.runLater(()->{
                                        clientList.getItems().clear();
                                        for (int i = 1; i <tokens.length ; i++) {
                                            clientList.getItems().add(tokens[i]);
                                        }
                                    });
                                }
                            }else {
                                textArea.appendText(str +"\n");
                            }
                        }
                    }catch (IOException e) {
                        e.printStackTrace();
                    }finally {
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        setAuthorized(false);
                    }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMSG(ActionEvent actionEvent) {
        try {
            out.writeUTF(textField.getText());
            textField.setText("");
            textField.requestFocus();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void tryToAuth(ActionEvent actionEvent) {
        if(socket == null || socket.isClosed()){
            connect();
        }
        try {
            out.writeUTF("/auth "+loginField.getText()+" "
                    + passwordField.getText());
            loginField.clear();
            passwordField.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clickClientList(MouseEvent mouseEvent) {
        String receiver = clientList.getSelectionModel().getSelectedItem();
        textField.setText("/w "+receiver+" ");
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }
}
