import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Group group = new Group();
        Scene s = new Scene(group, 1000, 800, Color.GREY);

        Group g = new Group();

        s.setRoot(g);

        primaryStage.setScene(s);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Dukes of the realm");
        primaryStage.show();

    }


}
