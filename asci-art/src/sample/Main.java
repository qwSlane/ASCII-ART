package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("sample.fxml"));
        Parent root = loader.load();

        Controller ctrl = (Controller)(loader.getController());
        ctrl.Presets();

        primaryStage.setScene(new Scene(root,900,600));
        primaryStage.show();
        primaryStage.setTitle("Beta 1♂");
    }
    public static void main(String[] args) {

        launch(args);
    }
}
