package activeRecord;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Film {

    private String titre;
    private int id;
    private int id_real;

    public Film(String titre, Personne realisateur) {
        this.titre = titre;
        this.id_real = realisateur.getId();
        this.id = -1;
    }

    private Film(int id, int id_real, String titre) {
        this.id = id;
        this.id_real = id_real;
        this.titre = titre;
    }

    public static Film createFromResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        int id_real = resultSet.getInt("id_real");
        String titre = resultSet.getString("titre");
        return new Film(id, id_real, titre);
    }

    public static void createTable() throws SQLException {
        Connection connection = DBConnexion.getConnexion();
        String createString = "CREATE TABLE IF NOT EXISTS Film ( "
                + "id INTEGER PRIMARY KEY AUTO_INCREMENT, "
                + "id_real INTEGER, "
                + "titre VARCHAR(255), "
                + "FOREIGN KEY (id_real) REFERENCES Personne(id))";
        try (PreparedStatement statement = connection.prepareStatement(createString)) {
            statement.executeUpdate();
        }
    }

    public static void dropTable() throws SQLException {
        Connection connection = DBConnexion.getConnexion();
        String dropString = "DROP TABLE IF EXISTS Film";
        try (PreparedStatement statement = connection.prepareStatement(dropString)) {
            statement.executeUpdate();
        }
    }

    public static void beforeEach() throws SQLException {
        createTable();
        Film filmTest = new Film("TitreTest", Personne.findById(1));
        filmTest.save();
    }

    public static void afterEach() throws SQLException {
        dropTable();
    }

    public static List<Film> findAll() throws SQLException {
        List<Film> films = new ArrayList<>();
        Connection connection = DBConnexion.getConnexion();
        String query = "SELECT * FROM Film";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    films.add(createFromResultSet(resultSet));
                }
            }
        }
        return films;
    }

    public static Film findById(int id) throws SQLException {
        Film film = new Film("", null);
        Connection connection = DBConnexion.getConnexion();
        String query = "SELECT * FROM Film WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, id);
        preparedStatement.execute();
        ResultSet resultSet = preparedStatement.getResultSet();
        if (resultSet.next()) {
            film = createFromResultSet(resultSet);
        }
        return film;
    }

    public Personne getRealisateur() throws SQLException {
        return Personne.findById(id_real);
    }

    public static List<Film> findByRealisateur(Personne p) throws SQLException {
        List<Film> films = new ArrayList<>();
        Connection connection = DBConnexion.getConnexion();
        String query = "SELECT * FROM Film WHERE id_real = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, p.getId());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    films.add(createFromResultSet(resultSet));
                }
            }
        }
        return films;
    }

    public void save() throws SQLException {
        if (id == -1) {
            saveNew();
        } else {
            update();
        }
    }

    private void saveNew() throws SQLException {
        Connection connection = DBConnexion.getConnexion();
        String insertString = "INSERT INTO Film (id_real, titre) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(insertString,
                PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, id_real);
            statement.setString(2, titre);
            statement.executeUpdate();
            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    id = resultSet.getInt(1);
                }
            }
        }
    }

    private void update() throws SQLException {
        Connection connection = DBConnexion.getConnexion();
        String updateString = "UPDATE Film SET id_real = ?, titre = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(updateString)) {
            statement.setInt(1, id_real);
            statement.setString(2, titre);
            statement.setInt(3, id);
            statement.executeUpdate();
        }
    }

    public void delete() throws SQLException {
        if (id != -1) {
            Connection connection = DBConnexion.getConnexion();
            String deleteString = "DELETE FROM Film WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(deleteString)) {
                statement.setInt(1, id);
                statement.executeUpdate();
                id = -1;
            }
        }
    }


}
