package org.showrunner.ui;

import javafx.event.ActionEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ShowRunnerPresenter {

    public void buttonPressed(ActionEvent actionEvent) {
        log.info("Button pressed!");
    }

}
