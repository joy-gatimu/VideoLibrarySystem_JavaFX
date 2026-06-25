package rmi_package;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Remote interface for Video Library System service.
 * Defines all methods that can be invoked remotely.
 */
public interface VLSService extends Remote {

    // Genre Management
    void addGenre(String genre) throws RemoteException;
    void removeGenre(String genre) throws RemoteException;
    ArrayList<String> getGenres() throws RemoteException;

    // Customer Management
    void addCustomer(String customer) throws RemoteException;
    void removeCustomer(String customer) throws RemoteException;
    ArrayList<String> getCustomers() throws RemoteException;

    // Movie Management
    void addMovie(String genre, String movie) throws RemoteException;
    ArrayList<String> getMovies() throws RemoteException;
    ArrayList<String> getMoviesByGenre(String genre) throws RemoteException;

    // Rental Management
    void rentMovie(String customer, String movie) throws RemoteException;
    void returnMovie(String customer, String movie) throws RemoteException;
    ArrayList<String> getBorrowedMovies(String customer) throws RemoteException;
    ArrayList<String> getReturnedMovies(String customer) throws RemoteException;
}