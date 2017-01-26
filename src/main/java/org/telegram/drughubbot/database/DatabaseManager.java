package org.telegram.drughubbot.database;

import org.telegram.telegrambots.logging.BotLogger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseManager {

    private static final String LOGTAG = "DATABASEMANAGER";
    private static volatile DatabaseManager instance;
    private static volatile ConectionDB connetion;

    public static class ModelUser extends org.telegram.drughubbot.database.model.User {
    }

    private DatabaseManager() {
        connetion = new ConectionDB();

        if (!connetion.checkDB()) {
            BotLogger.info(LOGTAG, "DB Migrate");
            try {
                int currentVersion = createNewTables();
                BotLogger.info(LOGTAG, "Current DB version: " + currentVersion);
            } catch (SQLException e) {
                BotLogger.info(LOGTAG, e.getMessage());
            }

        }
    }

    public static DatabaseManager getInstance() {
        final DatabaseManager currentInstance;
        if (instance == null) {
            synchronized (DatabaseManager.class) {
                if (instance == null) {
                    instance = new DatabaseManager();
                }
                currentInstance = instance;
            }
        } else {
            currentInstance = instance;
        }
        return currentInstance;
    }

    private int createNewTables() throws SQLException {
        connetion.executeQuery(CreationStrings.createLocationTable);

        return CreationStrings.version;
    }

    public ModelUser getUser(long userId) {
        ModelUser user = null;
        try {
            final PreparedStatement preparedStatement = connetion.getPreparedStatement("SELECT * FROM users WHERE user_id = ? LIMIT 1");
            preparedStatement.setLong(1, userId);
            final ResultSet result = preparedStatement.executeQuery();

            if (result.next()) {
                user = new ModelUser();
                user.setUserId(result.getLong("user_id"));
                user.setLocation(result.getString("location"));
                user.setCity(result.getString("city"));
                user.setIsDealer(result.getInt("is_dealer"));
                user.setLang(result.getString("lang"));
                user.setRating(result.getInt("rating"));
                user.setIsBlock(result.getInt("is_block"));
                user.setPhone(result.getString("phone"));
            }
        } catch (SQLException e) {
            BotLogger.info(LOGTAG, e.getMessage());
        }

        return user;
    }

    public void saveUser(ModelUser user) {
        try {
            final PreparedStatement preparedStatement = connetion.getPreparedStatement("SELECT user_id FROM users WHERE user_id = ? LIMIT 1");
            preparedStatement.setLong(1, user.getUserId());
            final ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {

                StringBuilder sql = new StringBuilder();
                sql.append("UPDATE users SET ");
                if (user.getLocation() != null) {
                    sql.append("location = '" + user.getLocation() + "', ");
                }

                if (user.getCity() != null) {
                    sql.append("city = '" + user.getCity() + "', ");
                }

                if (user.getLang() != null) {
                    sql.append("lang = '" + user.getLang() + "', ");
                }

                if (user.getPhone() != null) {
                    sql.append("phone = '" + user.getPhone() + "', ");
                }

                sql.append("is_dealer = '" + user.getIsDealer() + "', ");

                sql.delete(sql.length() - 2, sql.length());
                sql.append("WHERE user_id = " + user.getUserId());

                BotLogger.fine(LOGTAG, sql.toString());

                connetion.executeQuery(sql.toString());
            } else {
                StringBuilder sql = new StringBuilder();
                sql.append("INSERT INTO users SET ");
                sql.append("user_id = '" + user.getUserId() + "', ");

                if (user.getLocation() != null) {
                    sql.append("location = '" + user.getLocation() + "', ");
                }

                if (user.getCity() != null) {
                    sql.append("city = '" + user.getCity() + "', ");
                }

                if (user.getLang() != null) {
                    sql.append("lang = '" + user.getLang() + "', ");
                }

                if (user.getPhone() != null) {
                    sql.append("phone = '" + user.getPhone() + "', ");
                }

                sql.delete(sql.length() - 2, sql.length());

                BotLogger.fine(LOGTAG, sql.toString());

                connetion.executeQuery(sql.toString());
            }
        } catch (SQLException e) {
            BotLogger.info(LOGTAG, e.getMessage());
        }
    }

}
