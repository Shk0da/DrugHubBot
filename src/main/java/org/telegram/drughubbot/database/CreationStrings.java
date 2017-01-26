package org.telegram.drughubbot.database;

public class CreationStrings {

    public static final int version = 1;
    public static final String createLocationTable = "CREATE TABLE IF NOT EXISTS `users` ( `user_id` int(10) unsigned NOT NULL, `location` point DEFAULT NULL, `city` varchar(254) DEFAULT NULL, `phone` varchar(12) DEFAULT NULL, `is_dealer` tinyint(4) NOT NULL DEFAULT '0', `lang` enum('ru','en') DEFAULT 'ru', `rating` smallint(7) DEFAULT '0', `is_block` tinyint(4) NOT NULL DEFAULT '0', PRIMARY KEY (`user_id`), UNIQUE KEY `users_user_id_uindex` (`user_id`) ) ENGINE=InnoDB DEFAULT CHARSET=utf8";

}
