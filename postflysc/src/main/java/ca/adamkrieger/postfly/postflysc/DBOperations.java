package ca.adamkrieger.postfly.postflysc;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.*;

@Scope("singleton")
@Component
class DBOperations {
    private static final Logger log = LoggerFactory.getLogger(DBOperations.class);
    private Connection conn;

    private String dbUrl;
    private String dbUser;
    private String dbPass;
    private String dbName;
    private String migrationDirectory;

    public DBOperations() {
        migrationDirectory = System.getProperty("migrationDir", "");
        dbUrl = System.getProperty("dbUrl", "");
        dbUser = System.getProperty("dbUser", "");
        dbPass = System.getProperty("dbPass", "");
        dbName = System.getProperty("dbName", "");
    }

    @Scheduled(fixedDelay = 5000)
    void maintainConnection() throws SQLException {

        if(conn == null) {
            if(!repairConnection()){
                return;
            }
        }

        if(!this.checkDB(conn)) {
            createDB(conn);
        }

        log.info("Database connection OK");
    }

    class DBOperationResult {
        DBOperationResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        boolean success;
        String message;
    }

    Flyway getFW() {
        return Flyway.configure().
                connectRetries(5).
                locations(migrationDirectory).
                dataSource(dbUrl + dbName, dbUser, dbPass).
                load();
    }

    DBOperationResult migrateDB() {
        Flyway fw = getFW();

        int numMigrationsApplied;

        try {
            numMigrationsApplied = fw.migrate();
        } catch (FlywayException fwEx) {
            String errMsg = fwEx.getMessage();
            return new DBOperationResult(false, errMsg);
        }

        return new DBOperationResult(true, String.format("%d migrations applied", numMigrationsApplied));
    }

    DBOperationResult getRevision() {
        Flyway fw = getFW();

        if(fw.info() == null) {
            return new DBOperationResult(false, "no db info available");
        } else if(fw.info().current() == null) {
            return new DBOperationResult(false, "no current revision available");
        } else {
            return new DBOperationResult(true, fw.info().current().getVersion().getVersion());
        }
    }

    private boolean repairConnection() {
        Connection c;

        try {
            c = DriverManager.getConnection(dbUrl, dbUser, dbPass);
        } catch (SQLException sqlEx) {
            log.info("{}", "error on connection");
            return false;
        }

        conn = c;
        return true;
    }

    private boolean checkDB(Connection c) throws SQLException {
        String getTables = "SELECT datname FROM pg_database WHERE datistemplate = false;";
        PreparedStatement ps = c.prepareStatement(getTables);
        ResultSet rs = ps.executeQuery();

        boolean dbExists = false;

        log.info("Checking DBs...");

        while (rs.next()) {
            String eachDBName = rs.getString(1);

            log.info("DB: {} - {}", eachDBName, rs.toString());

            if (eachDBName.equals(dbName)) {
                dbExists = true;
            }
        }
        rs.close();
        ps.close();

        log.info("done checking...");

        return dbExists;
    }

    private void createDB(Connection c) throws SQLException {
        Statement st = c.createStatement();

        st.executeUpdate("CREATE DATABASE " + dbName);
    }
}
