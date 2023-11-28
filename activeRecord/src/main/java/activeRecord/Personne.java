package activeRecord;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Personne
{
    private int id;
    private String nom;
    private String prenom;

    public Personne(String nom, String prenom) {
        this.id = -1;
        this.nom = nom;
        this.prenom = prenom;
    }

    public static void createTable() throws SQLException {
        Connection connection = DBConnexion.getConnexion();
        String createString = "CREATE TABLE Personne ( " + "ID INTEGER  AUTO_INCREMENT, "
                + "NOM varchar(40) NOT NULL, " + "PRENOM varchar(40) NOT NULL, " + "PRIMARY KEY (ID))";
        try (PreparedStatement statement = connection.prepareStatement(createString)) {
            statement.executeUpdate();
        }
    }

    public static void deleteTable() throws SQLException {
        Connection connection = DBConnexion.getConnexion();
        String drop = "DROP TABLE IF EXISTS Personne";
        try (PreparedStatement statement = connection.prepareStatement(drop)) {
            statement.executeUpdate();
        }
    }

    public void delete() throws SQLException {
        if (this.id != -1) {
            Connection connection = DBConnexion.getConnexion();
            String deleteString = "DELETE FROM Personne WHERE ID = ?";
            try (PreparedStatement statement = connection.prepareStatement(deleteString)) {
                statement.setInt(1, this.id);
                statement.executeUpdate();
                this.id = -1;
            }
        }
    }

    public void save() throws SQLException {
        if (this.id == -1) {
            saveNew();
        } else {
            update();
        }
    }

    private void saveNew() throws SQLException {
        Connection connection = DBConnexion.getConnexion();
        String insertString = "INSERT INTO Personne (nom, prenom) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(insertString,
                PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, this.nom);
            statement.setString(2, this.prenom);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            try (resultSet) {
                if (resultSet.next()) {
                    this.id = resultSet.getInt(1);
                }
            }
        }
    }

    private void update() throws SQLException {
        Connection connection = DBConnexion.getConnexion();
        String updateString = "UPDATE Personne SET nom = ?, prenom = ? WHERE ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(updateString)) {
            statement.setString(1, this.nom);
            statement.setString(2, this.prenom);
            statement.setInt(3, this.id);
            statement.executeUpdate();
        }
    }

    public static Personne findById(int id) throws SQLException {
        Personne personne = new Personne("", "");
        Connection connection = DBConnexion.getConnexion();
        String query = "SELECT * FROM Personne WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, id);
        preparedStatement.execute();
        // s'il y a un resultat retourner la personne correspondante
        ResultSet resultSet = preparedStatement.getResultSet();
        if (resultSet.next()) {
            String nom = resultSet.getString("nom");
            String prenom = resultSet.getString("prenom");
            personne.setNom(nom);
            personne.setPrenom(prenom);
            personne.setId(id);
        }
        return personne;
    }

    public static List<Personne> findByName(String nom) throws SQLException {
        List<Personne> personnes = new ArrayList<>();
        Connection connection = DBConnexion.getConnexion();
        String query = "SELECT * FROM Personne WHERE nom = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, nom);
        preparedStatement.execute();
        // s'il y a un resultat remplir la liste de personnes
        ResultSet resultSet = preparedStatement.getResultSet();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String prenom = resultSet.getString("prenom");
            Personne personne = new Personne(nom, prenom);
            personne.setId(id);
            personnes.add(personne);
        }
        return personnes;
    }

    public static List<Personne> findAll() throws SQLException {
        List<Personne> personnes = new ArrayList<>();
        Connection connection = DBConnexion.getConnexion();
        String query = "SELECT * FROM Personne";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.execute();
        // s'il y a un resultat remplir la liste de personnes
        ResultSet resultSet = preparedStatement.getResultSet();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String nom = resultSet.getString("nom");
            String prenom = resultSet.getString("prenom");
            Personne personne = new Personne(nom, prenom);
            personne.setId(id);
            personnes.add(personne);
        }
        return personnes;
    }




    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
}
