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
 * Genre Management Interface.
 * Allows adding and removing movie genres.
 */
public class GenreInterface {

    public void start(Stage stage) {
        Text text1 = new Text("Name:");
        Text text2 = new Text("Registered:");
        TextField textField1 = new TextField();
        ComboBox<String> comboBox = new ComboBox<>();
        Button button1 = new Button("Save");
        Button button2 = new Button("Remove");
        Button btnBack = new Button("Back to Menu");

        // Load genres from server
        loadGenres(comboBox);

        GridPane gridPane = new GridPane();
        gridPane.setMinSize(600, 400);
        gridPane.setPadding(new Insets(10));
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setAlignment(Pos.CENTER);

        gridPane.add(text1, 0, 0);
        gridPane.add(textField1, 1, 0);
        gridPane.add(button1, 1, 1);
        gridPane.add(text2, 0, 2);
        gridPane.add(comboBox, 1, 2);
        gridPane.add(button2, 1, 3);
        gridPane.add(btnBack, 0, 4);

        gridPane.setStyle("-fx-background-color: BEIGE;");
        button1.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white;");
        button2.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white;");

        // Save genre - uses RMI
        button1.setOnAction(e -> {
            String genreName = textField1.getText();
            if (!genreName.isEmpty()) {
                try {
                    // Try Admin client first, fallback to Customer client
                    if (AdminClient.service != null) {
                        AdminClient.service.addGenre(genreName);
                    } else if (CustomerClient.service != null) {
                        CustomerClient.service.addGenre(genreName);
                    }
                    textField1.clear();
                    loadGenres(comboBox);
                    System.out.println("Genre added: " + genreName);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Remove genre - uses RMI
        button2.setOnAction(e -> {
            String selected = comboBox.getValue();
            if (selected != null) {
                try {
                    if (AdminClient.service != null) {
                        AdminClient.service.removeGenre(selected);
                    } else if (CustomerClient.service != null) {
                        CustomerClient.service.removeGenre(selected);
                    }
                    loadGenres(comboBox);
                    System.out.println("Genre removed: " + selected);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        btnBack.setOnAction(e -> new Main().start(stage));

        stage.setScene(new Scene(gridPane));
        stage.setTitle("Video Library System - Genres");
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
}