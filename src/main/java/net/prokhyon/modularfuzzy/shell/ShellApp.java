package net.prokhyon.modularfuzzy.shell;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import net.prokhyon.modularfuzzy.api.ModuleDescriptor;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.FuzzyAutomatonModuleDescriptor;
import net.prokhyon.modularfuzzy.fuzzySet.FuzzySetModuleDescriptor;
import net.prokhyon.modularfuzzy.fuzzySignature.FuzzySignatureModuleDescriptor;
import net.prokhyon.modularfuzzy.pathValues.PathValuesModuleDescriptor;
import net.prokhyon.modularfuzzy.shell.view.ShellLayoutController;

public class ShellApp extends Application {

	private Stage primaryStage;
	private BorderPane shellLayout;
	private FXMLLoader loader;
	private List<ModuleDescriptor> pseudoModules;
	private ShellLayoutController shellLayoutController;

	public static void main(String[] args) {
		launch(args);
	}

	public ShellApp() {

		pseudoModules = new ArrayList<ModuleDescriptor>();
		pseudoModules.add(new FuzzySetModuleDescriptor());
		pseudoModules.add(new FuzzyAutomatonModuleDescriptor());
		pseudoModules.add(new FuzzySignatureModuleDescriptor());
		pseudoModules.add(new PathValuesModuleDescriptor());
	}

	@Override
	public void start(Stage primaryStage) {

		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Modular Fuzzy Modeller");
		initShellLayout();
	}

	public void initShellLayout() {
		try {
			loader = new FXMLLoader();
			loader.setLocation(ShellApp.class.getResource("view/ShellLayout.fxml"));
			shellLayout = (BorderPane) loader.load();
			shellLayoutController = loader.getController();
			shellLayoutController.loadModules(pseudoModules);
			Scene scene = new Scene(shellLayout);
			primaryStage.setScene(scene);
			primaryStage.setMinWidth(800);
			primaryStage.setMinHeight(600);
			primaryStage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
