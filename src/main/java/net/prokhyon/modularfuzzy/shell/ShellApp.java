package net.prokhyon.modularfuzzy.shell;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ShellApp extends Application {

	private Stage primaryStage;
	private BorderPane rootLayout;
	private FXMLLoader loader;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {

		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Modular Fuzzy Modeller");
		initRootLayout();
	}

	public void initRootLayout() {
		try {
			loader = new FXMLLoader();
			loader.setLocation(ShellApp.class.getResource("view/ShellLayout.fxml"));
			rootLayout = (BorderPane) loader.load();

			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.setMinWidth(800);
			primaryStage.setMinHeight(600);
			primaryStage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
