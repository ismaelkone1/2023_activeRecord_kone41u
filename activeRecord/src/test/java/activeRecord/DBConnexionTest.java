package activeRecord;

import java.sql.Connection;
import java.sql.SQLException;

import static junit.framework.Assert.assertSame;
import static org.junit.jupiter.api.Assertions.*;

class DBConnexionTest
{
    @org.junit.jupiter.api.Test
        public void testConnexion() throws SQLException {
            // Obtenez deux instances de connexion
            DBConnexion db = new DBConnexion();
            DBConnexion db2 = new DBConnexion();
            Connection conn1 = db.getConnexion();
            Connection conn2 = db2.getConnexion();

            // Vérifiez que les deux instances sont les mêmes (singleton)
            assertSame(conn1, conn2);
        }
}

