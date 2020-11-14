package denis.SketchIt;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class FXMLApplication extends Application {

    private denis.SketchIt.Model model = new denis.SketchIt.Model();

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("View.fxml").openStream());
        denis.SketchIt.FXMLController controller = fxmlLoader.getController();
        controller.setModel(model);
        controller.addStage(stage);
        model.addController(controller);

        Scene scene = new Scene(root);
        scene.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ESCAPE) { model.removeSelection(); }
            else if(event.getCode() == KeyCode.DELETE) { model.eraseSelected(); }
        });
        stage.setMaxWidth(3840.0);
        stage.setMinWidth(700.0);
        stage.setMaxHeight(2160.0);
        stage.setMinHeight(700.0);
        stage.setTitle("SketchIt");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
