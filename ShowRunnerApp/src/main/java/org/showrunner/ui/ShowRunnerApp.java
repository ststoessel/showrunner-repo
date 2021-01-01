package org.showrunner.ui;

import com.airhacks.afterburner.injection.Injector;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ShowRunnerApp extends Application {

    @Override
    public void start(Stage stage) {
        ShowRunnerView appView = new ShowRunnerView();
        Scene scene = new Scene(appView.getView());
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        Injector.forgetAll();
    }

    public static void main(String[] args) {

        if (args.length > 0) {
            log.info(args[0]);
        }

        launch(args);
    }

}
