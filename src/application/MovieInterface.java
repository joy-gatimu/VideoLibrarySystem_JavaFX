package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import rmi_package.AdminClient;
import rmi_package.CustomerClient;

/**
 * Movie Management Interface.
 * Allows adding and removing movies with genre association.
 */
public class MovieInterface {

    public void start(Stage stage) {
        Text lblGenre = new Text("Genre:");
        ComboBox<String> comboGenre = new ComboBox<>();
        Text lblName = new Text("Name:");
        TextField txtName = new TextField();
        Button btnSave = new Button("Save Movie");
        Text lblReg = new Text("Registered:");
        ComboBox<String> comboReg = new ComboBox<>();
        Button btnRemove = new Button("Remove Movie");
        Button btnBack = new Button("Back to Menu");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setStyle("-fx-background-color: BEIGE;");

        grid.add(lblGenre, 0, 0);
        grid.add(comboGenre, 1, 0);
        grid.add(lblName, 0, 1);
        grid.add(txtName, 1, 1);
        grid.add(btnSave, 1, 2);
        grid.add(lblReg, 0, 3);
        grid.add(comboReg, 1, 3);
        grid.add(btnRemove, 1, 4);
        grid.add(btnBack, 0, 5);

        btnSave.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white;");
        btnRemove.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white;");

        loadGenres(comboGenre);
        loadMovies(comboReg);

        // Save movie - uses RMI
        btnSave.setOnAction(e -> {
            String movieTitle = txtName.getText();
            String selectedGenre = comboGenre.getValue();

            if (!movieTitle.isEmpty() && selectedGenre != null) {
                try {
                    if (AdminClient.service != null) {
                        AdminClient.service.addMovie(selectedGenre, movieTitle);
                    } else if (CustomerClient.service != null) {
                        CustomerClient.service.addMovie(selectedGenre, movieTitle);
                    }
                    txtName.clear();
                    loadMovies(comboReg);
                    System.out.println("Movie added: " + movieTitle + " (Genre: " + selectedGenre + ")");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        btnBack.setOnAction(e -> new Main().start(stage));

        stage.setScene(new Scene(grid, 600, 450));
        stage.setTitle("Video Library System - Movies");
        stage.show();
    }

    private void loadGenres(ComboBox<String> cb) {
        cb.getItems().clear();
        try {
            if (AdminClient.service != null) {
                cb.getItems().addAll(AdminClient.service.getGenres());
            } else if (CustomerClient.service != null) {
                cb.getItems().addAll(CustomerClient.service.getGenres());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadMovies(ComboBox<String> cb) {
        cb.getItems().clear();
        try {
            if (AdminClient.service != null) {
                cb.getItems().addAll(AdminClient.service.getMovies());
            } else if (CustomerClient.service != null) {
                cb.getItems().addAll(CustomerClient.service.getMovies());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}