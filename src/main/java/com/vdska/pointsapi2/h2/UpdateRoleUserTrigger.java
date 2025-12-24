package com.vdska.pointsapi2.h2;

import org.h2.api.Trigger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateRoleUserTrigger implements Trigger {
    @Override
    public void init(Connection conn, String schemaName, String triggerName,
                     String tableName, boolean before, int type) { }

    @Override
    public void fire(Connection conn, Object[] oldRow, Object[] newRow) throws SQLException {
        if (oldRow == null || newRow == null) return;

        boolean oldVerified = (Boolean) oldRow[5];
        boolean newVerified = (Boolean) newRow[5];

        if (oldVerified || !newVerified) return;

        String username = (String) newRow[1];

        try (PreparedStatement ps = conn.prepareStatement(
                "UPDATE authorities " +
                        "SET authority = ? " +
                        "WHERE username = ? AND authority = ?"
        )) {
            ps.setString(1, "ROLE_USER");
            ps.setString(2, username);
            ps.setString(3, "ROLE_UNVERIFIED_USER");
            ps.executeUpdate();
        }
    }

    @Override public void close() { }
    @Override public void remove() { }
}
