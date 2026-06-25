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
 * Customer Management Interface.
 * Allows adding and removing customers.
 */
public class CustomerInterface {

    public void start(Stage stage) {
        Text lblName = new Text("Name:");
        TextField txtName = new TextField();
        Text lblReg = new Text("Registered:");
        ComboBox<String> comboReg = new ComboBox<>();
        Button btnSave = new Button("Save Customer");
        Button btnRemove = new Button("Remove Customer");
        Button btnBack = new Button("Back to Menu");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setStyle("-fx-background-color: BEIGE;");

        grid.add(lblName, 0, 0);
        grid.add(txtName, 1, 0);
        grid.add(btnSave, 1, 1);
        grid.add(lblReg, 0, 2);
        grid.add(comboReg, 1, 2);
        grid.add(btnRemove, 1, 3);
        grid.add(btnBack, 0, 4);

        btnSave.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white;");
        btnRemove.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white;");

        loadCustomers(comboReg);

        // Save customer - uses RMI
        btnSave.setOnAction(e -> {
            String fullName = txtName.getText();
            if (!fullName.isEmpty()) {
                try {
                    if (AdminClient.service != null) {
                        AdminClient.service.addCustomer(fullName);
                    } else if (CustomerClient.service != null) {
                        CustomerClient.service.addCustomer(fullName);
                    }
                    txtName.clear();
                    loadCustomers(comboReg);
                    System.out.println("Customer added: " + fullName);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Remove customer - uses RMI
        btnRemove.setOnAction(e -> {
            String selected = comboReg.getValue();
            if (selected != null) {
                try {
                    if (AdminClient.service != null) {
                        AdminClient.service.removeCustomer(selected);
                    } else if (CustomerClient.service != null) {
                        CustomerClient.service.removeCustomer(selected);
                    }
                    loadCustomers(comboReg);
                    System.out.println("Customer removed: " + selected);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        btnBack.setOnAction(e -> new Main().start(stage));

        stage.setScene(new Scene(grid, 600, 450));
        stage.setTitle("Video Library System - Customers");
        stage.show();
    }

    private void loadCustomers(ComboBox<String> cb) {
        cb.getItems().clear();
        try {
            if (AdminClient.service != null) {
                cb.getItems().addAll(AdminClient.service.getCustomers());
            } else if (CustomerClient.service != null) {
                cb.getItems().addAll(CustomerClient.service.getCustomers());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}