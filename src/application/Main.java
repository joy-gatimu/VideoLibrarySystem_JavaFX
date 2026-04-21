package application;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        Text title = new Text("Movie Library System");
        title.setFont(new Font("Serif", 24));

        Button btnGenre = new Button("1. Manage Genres");
        Button btnMovie = new Button("2. Movie Registration");
        Button btnCustomer = new Button("3. Customer Registration");
        Button btnRental = new Button("4. Rental Process");

      
        String style = "-fx-background-color: darkslateblue; -fx-text-fill: white; -fx-min-width: 200px;";
        btnGenre.setStyle(style);
        btnMovie.setStyle(style);
        btnCustomer.setStyle(style);
        btnRental.setStyle(style);

        btnGenre.setOnAction(e -> new GenreInterface().start(primaryStage));
        btnMovie.setOnAction(e -> new MovieInterface().start(primaryStage));
        btnCustomer.setOnAction(e -> new CustomerInterface().start(primaryStage));
        btnRental.setOnAction(e -> new RentalInterface().start(primaryStage));

        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: LIGHTSTEELBLUE; -fx-padding: 50;");
        layout.getChildren().addAll(title, btnGenre, btnMovie, btnCustomer, btnRental);

        primaryStage.setScene(new Scene(layout, 600, 500));
        primaryStage.setTitle("VLS Dashboard");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
        
        
    }
}