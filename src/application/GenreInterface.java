package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.sql.*; 

import database_package.DBConnection;

public class GenreInterface {
    public void start(Stage stage) {
       
        Text text1 = new Text("Name:");
        Text text2 = new Text("Registered:");
        TextField textField1 = new TextField();
        ComboBox<String> comboBox = new ComboBox<>();
        Button button1 = new Button("Save");
        Button button2 = new Button("Remove");
        Button btnBack = new Button("Back to Menu");

       
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

        
        loadGenres(comboBox);

        
        button1.setOnAction(e -> {
            String genreName = textField1.getText();
            if(!genreName.isEmpty()) {
                String sql = "INSERT INTO Genres (genre, isactive) VALUES (?, 1)";
                try (Connection conn = DBConnection.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    
                    pstmt.setString(1, genreName);
                    pstmt.executeUpdate();
                    
                    textField1.clear();
                    loadGenres(comboBox);
                    System.out.println("Genre saved to database.");
                    
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

       
        button2.setOnAction(e -> {
            String selected = comboBox.getValue();
            if (selected != null) {
                String sql = "DELETE FROM Genres WHERE genre = ?";
                try (Connection conn = DBConnection.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    
                    pstmt.setString(1, selected);
                    pstmt.executeUpdate();
                    loadGenres(comboBox); 
                    
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        
        btnBack.setOnAction(e -> new Main().start(stage));

        stage.setScene(new Scene(gridPane));
        stage.setTitle("Movie Library System - Genres");
    }

   
    private void loadGenres(ComboBox<String> cb) {
        cb.getItems().clear();
        String sql = "SELECT genre FROM Genres WHERE isactive = 1";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                cb.getItems().add(rs.getString("genre"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}