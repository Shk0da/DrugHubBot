package org.telegram.drughubbot.database;

import org.telegram.drughubbot.Config;
import org.telegram.telegrambots.logging.BotLogger;

import java.sql.*;

public class ConectionDB {

    private static final Config appConfig = Config.getInstance();
    private static final String LOGTAG = "CONNECTIONDB";
    private Connection currentConection;

    public ConectionDB() {
        this.currentConection = openConexion();
    }

    public Boolean checkDB() {
        return true;
    }

    private Connection openConexion() {
        Connection connection = null;
        try {
            Class.forName(appConfig.getDbDriver()).newInstance();
            connection = DriverManager.getConnection(appConfig.getDbLink(), appConfig.getDbUser(), appConfig.getDbPass());
        } catch (SQLException | ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            BotLogger.error(LOGTAG, e);
        }

        return connection;
    }

    public void closeConexion() {
        try {
            this.currentConection.close();
        } catch (SQLException e) {
            BotLogger.error(LOGTAG, e);
        }
    }

    public ResultSet runSqlQuery(String query) throws SQLException {
        final Statement statement;
        statement = this.currentConection.createStatement();
        return statement.executeQuery(query);
    }

    public Boolean executeQuery(String query) throws SQLException {
        final Statement statement = this.currentConection.createStatement();
        return statement.execute(query);
    }

    public PreparedStatement getPreparedStatement(String query) throws SQLException {
        return this.currentConection.prepareStatement(query);
    }

    public PreparedStatement getPreparedStatement(String query, int flags) throws SQLException {
        return this.currentConection.prepareStatement(query, flags);
    }

    /**
     * Initilize a transaction in database
     *
     * @throws SQLException If initialization fails
     */
    public void initTransaction() throws SQLException {
        this.currentConection.setAutoCommit(false);
    }

    /**
     * Finish a transaction in database and commit changes
     *
     * @throws SQLException If a rollback fails
     */
    public void commitTransaction() throws SQLException {
        try {
            this.currentConection.commit();
        } catch (SQLException e) {
            if (this.currentConection != null) {
                this.currentConection.rollback();
            }
        } finally {
            this.currentConection.setAutoCommit(false);
        }
    }

}
