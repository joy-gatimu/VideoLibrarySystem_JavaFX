package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import rmi_package.AdminClient;
import rmi_package.CustomerClient;

/**
 * Rental Management Interface.
 * Allows renting and returning movies for customers.
 */
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
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setStyle("-fx-background-color: BEIGE;");

        grid.add(lblCust, 0, 0);
        grid.add(comboCust, 1, 0);
        grid.add(lblGen, 0, 1);
        grid.add(comboGen, 1, 1);
        grid.add(lblMov, 0, 2);
        grid.add(comboMov, 1, 2);
        grid.add(btnSave, 1, 3);
        grid.add(lblBorr, 0, 4);
        grid.add(comboBorr, 1, 4);
        grid.add(btnRet, 1, 5);
        grid.add(lblRetd, 0, 6);
        grid.add(comboRetd, 1, 6);
        grid.add(btnBack, 0, 7);

        btnSave.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white;");
        btnRet.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white;");

        loadCustomers(comboCust);
        loadGenres(comboGen);

        // Load movies when genre is selected
        comboGen.setOnAction(e -> {
            String selectedGenre = comboGen.getValue();
            if (selectedGenre != null) {
                comboMov.getItems().clear();
                try {
                    if (AdminClient.service != null) {
                        comboMov.getItems().addAll(AdminClient.service.getMoviesByGenre(selectedGenre));
                    } else if (CustomerClient.service != null) {
                        comboMov.getItems().addAll(CustomerClient.service.getMoviesByGenre(selectedGenre));
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Load rental lists when customer is selected
        comboCust.setOnAction(e -> {
            String selectedCustomer = comboCust.getValue();
            if (selectedCustomer != null) {
                refreshRentalLists(selectedCustomer, comboBorr, comboRetd);
            }
        });

        // Save rental - uses RMI
        btnSave.setOnAction(e -> {
            String customer = comboCust.getValue();
            String movie = comboMov.getValue();
            if (customer != null && movie != null) {
                try {
                    if (AdminClient.service != null) {
                        AdminClient.service.rentMovie(customer, movie);
                    } else if (CustomerClient.service != null) {
                        CustomerClient.service.rentMovie(customer, movie);
                    }
                    refreshRentalLists(customer, comboBorr, comboRetd);
                    System.out.println("Movie rented: " + movie + " by " + customer);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Return movie - uses RMI
        btnRet.setOnAction(e -> {
            String customer = comboCust.getValue();
            String movie = comboBorr.getValue();
            if (customer != null && movie != null) {
                try {
                    if (AdminClient.service != null) {
                        AdminClient.service.returnMovie(customer, movie);
                    } else if (CustomerClient.service != null) {
                        CustomerClient.service.returnMovie(customer, movie);
                    }
                    refreshRentalLists(customer, comboBorr, comboRetd);
                    System.out.println("Movie returned: " + movie + " by " + customer);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        btnBack.setOnAction(e -> new Main().start(stage));

        stage.setScene(new Scene(grid, 650, 600));
        stage.setTitle("Video Library System - Rentals");
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

    private void refreshRentalLists(String customer, ComboBox<String> borr, ComboBox<String> retd) {
        borr.getItems().clear();
        retd.getItems().clear();
        try {
            if (AdminClient.service != null) {
                borr.getItems().addAll(AdminClient.service.getBorrowedMovies(customer));
                retd.getItems().addAll(AdminClient.service.getReturnedMovies(customer));
            } else if (CustomerClient.service != null) {
                borr.getItems().addAll(CustomerClient.service.getBorrowedMovies(customer));
                retd.getItems().addAll(CustomerClient.service.getReturnedMovies(customer));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}