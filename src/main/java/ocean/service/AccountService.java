package ocean.service;

import ocean.database.DatabaseConnection;
import ocean.model.LocalUser;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

/** Local login service using salted PBKDF2 password hashes. */
public class AccountService {
    private static final int ITERATIONS = 120_000;
    private static final int KEY_LENGTH = 256;
    private final SecureRandom random = new SecureRandom();

    public LocalUser register(String username, char[] password) throws SQLException {
        String cleanName = username == null ? "" : username.trim();
        if (cleanName.length() < 3 || cleanName.length() > 24) {
            throw new IllegalArgumentException("Choose a username between 3 and 24 characters.");
        }
        if (password == null || password.length < 6) {
            throw new IllegalArgumentException("Choose a password with at least 6 characters.");
        }

        byte[] salt = new byte[16];
        random.nextBytes(salt);
        String id = UUID.randomUUID().toString();
        String saltText = Base64.getEncoder().encodeToString(salt);
        String hash = hash(password, salt);

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement p = c.prepareStatement(
                     "INSERT INTO users(user_id,username,salt,password_hash,created_at) VALUES(?,?,?,?,?)")) {
            p.setString(1, id);
            p.setString(2, cleanName);
            p.setString(3, saltText);
            p.setString(4, hash);
            p.setString(5, LocalDateTime.now().toString());
            p.executeUpdate();
            try (Statement s = c.createStatement(); ResultSet r = s.executeQuery("SELECT COUNT(*) FROM users")) {
                if (r.next() && r.getInt(1) == 1) {
                    try (PreparedStatement oldLetters = c.prepareStatement(
                            "UPDATE future_letters SET owner_id=? WHERE owner_id IS NULL")) {
                        oldLetters.setString(1, id);
                        oldLetters.executeUpdate();
                    }
                }
            }
            return new LocalUser(id, cleanName);
        } catch (SQLException e) {
            if (e.getMessage() != null && e.getMessage().toLowerCase().contains("unique")) {
                throw new IllegalArgumentException("That username is already taken.");
            }
            throw e;
        } finally {
            if (password != null) java.util.Arrays.fill(password, '\0');
        }
    }

    public Optional<LocalUser> login(String username, char[] password) throws SQLException {
        if (username == null || password == null) return Optional.empty();
        String sql = "SELECT user_id,username,salt,password_hash FROM users WHERE username=?";
        try (Connection c = DatabaseConnection.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, username.trim());
            try (ResultSet r = p.executeQuery()) {
                if (!r.next()) return Optional.empty();
                byte[] salt = Base64.getDecoder().decode(r.getString("salt"));
                String expected = r.getString("password_hash");
                String actual = hash(password, salt);
                if (java.security.MessageDigest.isEqual(expected.getBytes(java.nio.charset.StandardCharsets.UTF_8),
                        actual.getBytes(java.nio.charset.StandardCharsets.UTF_8))) {
                    return Optional.of(new LocalUser(r.getString("user_id"), r.getString("username")));
                }
                return Optional.empty();
            }
        } finally {
            java.util.Arrays.fill(password, '\0');
        }
    }

    private String hash(char[] password, byte[] salt) {
        PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
        try {
            byte[] result = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(result);
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Password hashing is unavailable.", e);
        } finally {
            spec.clearPassword();
        }
    }
}
