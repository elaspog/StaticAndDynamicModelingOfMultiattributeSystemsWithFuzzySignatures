package net.prokhyon.modularfuzzy.shell;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import net.prokhyon.modularfuzzy.common.CommonServices;
import net.prokhyon.modularfuzzy.common.CommonServicesImplSingleton;
import net.prokhyon.modularfuzzy.shell.view.ShellLayoutController;

public class ShellApp extends Application {

	private Stage primaryStage;
	private BorderPane shellLayout;
	private ShellLayoutController shellLayoutController;
	private CommonServices services;

	public static void main(String[] args) {
		launch(args);
	}

	public ShellApp() {
		services = CommonServicesImplSingleton.getInstance();
		((CommonServicesImplSingleton) services).initializeModules();
	}

	@Override
	public void start(Stage primaryStage) {

		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Modular Fuzzy Modeller");
		initShellLayout();
	}

	public void initShellLayout() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ShellApp.class.getResource("view/ShellLayout.fxml"));
			shellLayout = (BorderPane) loader.load();
			shellLayoutController = loader.getController();
			shellLayoutController.loadModules();
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
