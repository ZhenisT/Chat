package client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("sample.fxml"));

//        прочитал из
//        http://qaru.site/questions/354403/invocationtargetexception-when-running-a-javafx-program
//        чтобы передать сслыку на контроллер

        fxmlLoader.setController(new Controller());

        Controller controller = fxmlLoader.getController();
        controller.setStage(primaryStage);

        Parent root =fxmlLoader.load();
//        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));

        primaryStage.setTitle("Супер чат 2019");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
