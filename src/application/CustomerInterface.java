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

public class CustomerInterface {
    public void start(Stage stage) {
        
        Text lblName = new Text("Name:");
        TextField txtName = new TextField();
        Text lblPhone = new Text("Phone:");
        TextField txtPhone = new TextField();
        Text lblEmail = new Text("Email:");
        TextField txtEmail = new TextField();
        Button btnSave = new Button("Save Customer");
        Text lblReg = new Text("Registered:");
        ComboBox<String> comboReg = new ComboBox<>();
        Button btnRemove = new Button("Remove Customer");
        Button btnBack = new Button("Back to Menu");

        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(20));
        grid.setVgap(10); grid.setHgap(10);
        grid.setStyle("-fx-background-color: BEIGE;");

     
        grid.add(lblName, 0, 0);  grid.add(txtName, 1, 0);
        grid.add(lblPhone, 0, 1); grid.add(txtPhone, 1, 1);
        grid.add(lblEmail, 0, 2); grid.add(txtEmail, 1, 2);
        grid.add(btnSave, 1, 3);
        grid.add(lblReg, 0, 4);   grid.add(comboReg, 1, 4);
        grid.add(btnRemove, 1, 5);
        grid.add(btnBack, 0, 6);

        
        btnSave.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white;");
        btnRemove.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white;");

       
        loadCustomers(comboReg);

       
        btnSave.setOnAction(e -> {
            String fullName = txtName.getText();
            if(!fullName.isEmpty()) {
               
                String sql = "INSERT INTO Clients (Fullname, isactive) VALUES (?, 1)";
                try (Connection conn = DBConnection.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    
                    pstmt.setString(1, fullName);
                    pstmt.executeUpdate();
                    
                    txtName.clear(); txtPhone.clear(); txtEmail.clear();
                    loadCustomers(comboReg);
                    System.out.println("Customer saved to database.");
                    
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        
        btnRemove.setOnAction(e -> {
            String selected = comboReg.getValue();
            if (selected != null) {
                String sql = "DELETE FROM Clients WHERE Fullname = ?";
                try (Connection conn = DBConnection.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    
                    pstmt.setString(1, selected);
                    pstmt.executeUpdate();
                    loadCustomers(comboReg); // Refresh list
                    
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        
        btnBack.setOnAction(e -> new Main().start(stage));

        stage.setScene(new Scene(grid, 600, 500));
        stage.setTitle("Movie Library System - Customers");
    }

  
    private void loadCustomers(ComboBox<String> cb) {
        cb.getItems().clear();
        String sql = "SELECT Fullname FROM Clients WHERE isactive = 1";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                cb.getItems().add(rs.getString("Fullname"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}