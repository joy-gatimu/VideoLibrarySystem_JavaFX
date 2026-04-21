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

public class RentalInterface {
    public void start(Stage stage) {
        
        Text lblCust = new Text("Customer:");
        ComboBox<String> comboCust = new ComboBox<>();
        Text lblGen = new Text("Genre:");
        ComboBox<String> comboGen = new ComboBox<>();
        Text lblMov = new Text("Movies:");
        ComboBox<String> comboMov = new ComboBox<>();
        Button btnSave = new Button("Save Rental");
        
        Text lblBorr = new Text("Borrowed:");
        ComboBox<String> comboBorr = new ComboBox<>();
        Button btnRet = new Button("Return Movie");
        
        Text lblRetd = new Text("Returned:");
        ComboBox<String> comboRetd = new ComboBox<>();
        Button btnBack = new Button("Back to Menu");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(20));
        grid.setVgap(10); grid.setHgap(10);
        grid.setStyle("-fx-background-color: BEIGE;");

       
        grid.add(lblCust, 0, 0);  grid.add(comboCust, 1, 0);
        grid.add(lblGen, 0, 1);   grid.add(comboGen, 1, 1);
        grid.add(lblMov, 0, 2);   grid.add(comboMov, 1, 2);
        grid.add(btnSave, 1, 3);
        grid.add(lblBorr, 0, 4);  grid.add(comboBorr, 1, 4);
        grid.add(btnRet, 1, 5);
        grid.add(lblRetd, 0, 6);  grid.add(comboRetd, 1, 6);
        grid.add(btnBack, 0, 7);

        // Styling
        String btnStyle = "-fx-background-color: darkslateblue; -fx-text-fill: white;";
        btnSave.setStyle(btnStyle);
        btnRet.setStyle(btnStyle);

       
        loadData("SELECT Fullname FROM Clients WHERE isactive = 1", comboCust);
        loadData("SELECT genre FROM Genres WHERE isactive = 1", comboGen);

       
        comboGen.setOnAction(e -> {
            String selectedGenre = comboGen.getValue();
            if (selectedGenre != null) {
                comboMov.getItems().clear();
                String sql = "SELECT m.Title FROM Movies m JOIN Genres g ON m.genre_id = g.id " +
                             "WHERE g.genre = ? AND m.isactive = 1";
                try (Connection conn = DBConnection.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, selectedGenre);
                    ResultSet rs = pstmt.executeQuery();
                    while (rs.next()) {
                        comboMov.getItems().add(rs.getString("Title"));
                    }
                } catch (SQLException ex) { ex.printStackTrace(); }
            }
        });


        comboCust.setOnAction(e -> {
            refreshRentalLists(comboCust.getValue(), comboBorr, comboRetd);
        });

        
        btnSave.setOnAction(e -> {
            String customer = comboCust.getValue();
            String movie = comboMov.getValue();
            if (customer != null && movie != null) {
                String sql = "INSERT INTO Rentals (client_id, movie_id, Returned) VALUES " +
                             "((SELECT id FROM Clients WHERE Fullname = ?), " +
                             "(SELECT id FROM Movies WHERE Title = ?), 0)";
                try (Connection conn = DBConnection.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, customer);
                    pstmt.setString(2, movie);
                    pstmt.executeUpdate();
                    refreshRentalLists(customer, comboBorr, comboRetd);
                } catch (SQLException ex) { ex.printStackTrace(); }
            }
        });

      
        btnRet.setOnAction(e -> {
            String customer = comboCust.getValue();
            String movie = comboBorr.getValue();
            if (customer != null && movie != null) {
                String sql = "UPDATE Rentals SET Returned = 1 WHERE " +
                             "client_id = (SELECT id FROM Clients WHERE Fullname = ?) AND " +
                             "movie_id = (SELECT id FROM Movies WHERE Title = ?) AND Returned = 0 LIMIT 1";
                try (Connection conn = DBConnection.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, customer);
                    pstmt.setString(2, movie);
                    pstmt.executeUpdate();
                    refreshRentalLists(customer, comboBorr, comboRetd);
                } catch (SQLException ex) { ex.printStackTrace(); }
            }
        });

        btnBack.setOnAction(e -> new Main().start(stage));
        stage.setScene(new Scene(grid, 650, 600));
        stage.setTitle("Movie Library System - Rentals");
    }

    private void loadData(String sql, ComboBox<String> cb) {
        cb.getItems().clear();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) { cb.getItems().add(rs.getString(1)); }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private void refreshRentalLists(String customerName, ComboBox<String> borr, ComboBox<String> retd) {
        borr.getItems().clear();
        retd.getItems().clear();
        if (customerName == null) return;

        String sql = "SELECT m.Title, r.Returned FROM Rentals r " +
                     "JOIN Movies m ON r.movie_id = m.id " +
                     "JOIN Clients c ON r.client_id = c.id " +
                     "WHERE c.Fullname = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, customerName);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                if (rs.getInt("Returned") == 1) {
                    retd.getItems().add(rs.getString("Title"));
                } else {
                    borr.getItems().add(rs.getString("Title"));
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }
}