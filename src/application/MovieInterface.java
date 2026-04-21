package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.sql.*; // Import for database connectivity

import database_package.DBConnection;

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
        grid.setVgap(10); grid.setHgap(10);
        grid.setStyle("-fx-background-color: BEIGE;");

        grid.add(lblGenre, 0, 0); grid.add(comboGenre, 1, 0);
        grid.add(lblName, 0, 1);  grid.add(txtName, 1, 1);
        grid.add(btnSave, 1, 2);
        grid.add(lblReg, 0, 3);   grid.add(comboReg, 1, 3);
        grid.add(btnRemove, 1, 4);
        grid.add(btnBack, 0, 5);

        
        loadGenres(comboGenre);
        loadMovies(comboReg);

     
        btnSave.setOnAction(e -> {
            String movieTitle = txtName.getText();
            String selectedGenre = comboGenre.getValue();

            if(!movieTitle.isEmpty() && selectedGenre != null) {
                try (Connection conn = DBConnection.getConnection()) {
                   
                    String findGenreId = "SELECT id FROM Genres WHERE genre = ?";
                    PreparedStatement psId = conn.prepareStatement(findGenreId);
                    psId.setString(1, selectedGenre);
                    ResultSet rs = psId.executeQuery();

                    if(rs.next()) {
                        int genreId = rs.getInt("id");

                       
                        String insertMovie = "INSERT INTO Movies (genre_id, Title, isactive) VALUES (?, ?, 1)";
                        PreparedStatement psIns = conn.prepareStatement(insertMovie);
                        psIns.setInt(1, genreId);
                        psIns.setString(2, movieTitle);
                        psIns.executeUpdate();

                        txtName.clear();
                        loadMovies(comboReg); 
                        System.out.println("Movie saved successfully!");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        
        btnBack.setOnAction(e -> new Main().start(stage));

        stage.setScene(new Scene(grid, 600, 450));
        stage.setTitle("Movie Library System - Movies");
    }

  
    private void loadGenres(ComboBox<String> cb) {
        cb.getItems().clear();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT genre FROM Genres WHERE isactive = 1")) {
            while (rs.next()) {
                cb.getItems().add(rs.getString("genre"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    
    private void loadMovies(ComboBox<String> cb) {
        cb.getItems().clear();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT Title FROM Movies WHERE isactive = 1")) {
            while (rs.next()) {
                cb.getItems().add(rs.getString("Title"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }
}