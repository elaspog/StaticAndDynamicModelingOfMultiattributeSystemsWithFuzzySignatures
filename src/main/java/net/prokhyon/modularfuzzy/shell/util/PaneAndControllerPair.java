package net.prokhyon.modularfuzzy.shell.util;

import javafx.scene.layout.Pane;
import net.prokhyon.modularfuzzy.api.LoadableDataController;

public class PaneAndControllerPair{

    public Pane pane;
    public LoadableDataController controller;

    public PaneAndControllerPair(Pane pane, LoadableDataController controller) {
        this.pane = pane;
        this.controller = controller;
    }

}