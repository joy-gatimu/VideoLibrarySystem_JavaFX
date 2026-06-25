package rmi_package;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.sql.*;
import java.util.ArrayList;
import database_package.DBConnection;

/**
 * Implementation of the VLSService remote interface.
 * Handles all database operations for the Video Library System.
 */
public class VLSServiceImpl extends UnicastRemoteObject implements VLSService {

    public VLSServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public void addGenre(String genre) throws RemoteException {
        String sql = "INSERT INTO Genres (genre, isactive) VALUES (?, 1)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, genre);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeGenre(String genre) throws RemoteException {
        String sql = "UPDATE Genres SET isactive = 0 WHERE genre = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, genre);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<String> getGenres() throws RemoteException {
        ArrayList<String> list = new ArrayList<>();
        String sql = "SELECT genre FROM Genres WHERE isactive = 1";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(rs.getString("genre"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void addCustomer(String customer) throws RemoteException {
        String sql = "INSERT INTO Clients (Fullname, isactive) VALUES (?, 1)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, customer);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeCustomer(String customer) throws RemoteException {
        String sql = "UPDATE Clients SET isactive = 0 WHERE Fullname = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, customer);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<String> getCustomers() throws RemoteException {
        ArrayList<String> list = new ArrayList<>();
        String sql = "SELECT Fullname FROM Clients WHERE isactive = 1";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(rs.getString("Fullname"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void addMovie(String genre, String movie) throws RemoteException {
        String sql = "INSERT INTO Movies (genre_id, Title, isactive) VALUES (" +
                "(SELECT id FROM Genres WHERE genre = ?), ?, 1)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, genre);
            ps.setString(2, movie);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<String> getMovies() throws RemoteException {
        ArrayList<String> list = new ArrayList<>();
        String sql = "SELECT Title FROM Movies WHERE isactive = 1";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(rs.getString("Title"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public ArrayList<String> getMoviesByGenre(String genre) throws RemoteException {
        ArrayList<String> list = new ArrayList<>();
        String sql = "SELECT m.Title FROM Movies m JOIN Genres g ON m.genre_id = g.id " +
                "WHERE g.genre = ? AND m.isactive = 1";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, genre);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(rs.getString("Title"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void rentMovie(String customer, String movie) throws RemoteException {
        String sql = "INSERT INTO Rentals (client_id, movie_id, Returned) VALUES (" +
                "(SELECT id FROM Clients WHERE Fullname = ?), " +
                "(SELECT id FROM Movies WHERE Title = ?), 0)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, customer);
            ps.setString(2, movie);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void returnMovie(String customer, String movie) throws RemoteException {
        String sql = "UPDATE Rentals SET Returned = 1 WHERE " +
                "client_id = (SELECT id FROM Clients WHERE Fullname = ?) AND " +
                "movie_id = (SELECT id FROM Movies WHERE Title = ?) AND Returned = 0";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, customer);
            ps.setString(2, movie);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<String> getBorrowedMovies(String customer) throws RemoteException {
        ArrayList<String> list = new ArrayList<>();
        String sql = "SELECT m.Title FROM Rentals r " +
                "JOIN Movies m ON r.movie_id = m.id " +
                "JOIN Clients c ON r.client_id = c.id " +
                "WHERE c.Fullname = ? AND r.Returned = 0";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, customer);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(rs.getString("Title"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public ArrayList<String> getReturnedMovies(String customer) throws RemoteException {
        ArrayList<String> list = new ArrayList<>();
        String sql = "SELECT m.Title FROM Rentals r " +
                "JOIN Movies m ON r.movie_id = m.id " +
                "JOIN Clients c ON r.client_id = c.id " +
                "WHERE c.Fullname = ? AND r.Returned = 1";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, customer);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(rs.getString("Title"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}