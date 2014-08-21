package fw;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;

public class DB {
    /*
    Connection conn;
    PreparedStatement statementGetVersion = null;
    ResultSet rs;
    conn = DriverManager.getConnection(url, user, password);
    */

    Connection conn;
    String jdbcUri, username, password;
    Map<String, PreparedStatement> cachedStatements = new HashMap<String, PreparedStatement>();

    public DB(String driverClass, String jdbcUri, String username, String password) throws Exception {
        Class.forName(driverClass);
        this.jdbcUri = jdbcUri;
        this.username = username;
        this.password = password;
        this.connect();
    }

    public void connect() throws Exception {
        conn = DriverManager.getConnection(jdbcUri, username, password);
    }

    public void disconnect() {
    }

    public PreparedStatement getStatement(String sql) throws Exception {
        PreparedStatement stmt = cachedStatements.get(sql);
        if (stmt != null)
            return stmt;
        stmt = conn.prepareStatement(sql);
        cachedStatements.put(sql,stmt);
        return stmt;
    }
}
