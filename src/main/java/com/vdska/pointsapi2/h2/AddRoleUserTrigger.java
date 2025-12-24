package com.vdska.pointsapi2.h2;

import org.h2.api.Trigger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Триггер для H2 базы данных, который добавляет запись с ролью ROLE_USER каждый раз при добавлении нового пользователя.
 */
public class AddRoleUserTrigger implements Trigger {
    @Override
    public void init(Connection conn,
                     String schemaName,
                     String triggerName,
                     String tableName,
                     boolean before,
                     int type) {
    }

    @Override
    public void fire(Connection conn, Object[] oldRow, Object[] newRow) throws SQLException {
        String username = (String) newRow[1];

        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO authorities (username, authority) VALUES (?, 'ROLE_UNVERIFIED_USER')"
        )) {
            ps.setString(1, username);
            ps.executeUpdate();
        }
    }

    @Override
    public void close() {}

    @Override
    public void remove() {}
}

