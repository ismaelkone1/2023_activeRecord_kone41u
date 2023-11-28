package activeRecord;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnexionFactory {
    private static final String userName = "kone";
    private static final String password = "record";
    private static final String serverName = "localhost";
    private static final String portNumber = "3306";
    private static final String tableName = "personne";
    private static String dbName = "testpersonne";
    private static final String urlDB = "jdbc:mysql://" + serverName + ":" + portNumber + "/" + dbName;

    public static Connection createConnection() throws SQLException {
    return DriverManager.getConnection(urlDB, userName, password);
    }

    public static void closeConnection(Connection connect) throws SQLException {
    connect.close();
    }

    public static void setNomDB(String nomDB) {
        dbName = nomDB;
    }

}
