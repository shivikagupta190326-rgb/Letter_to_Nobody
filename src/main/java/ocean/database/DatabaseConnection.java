package ocean.database;
import java.sql.*; import java.nio.file.*;
/** Owns SQLite initialization and creates a fresh connection per operation. */
public final class DatabaseConnection {
 private static final String URL;
 static { try { Path dir=Path.of(System.getProperty("user.home"),".message-in-a-bottle"); Files.createDirectories(dir); URL="jdbc:sqlite:"+dir.resolve("ocean.db"); } catch(Exception e){throw new ExceptionInInitializerError(e);} }
 private DatabaseConnection(){}
 public static Connection getConnection() throws SQLException {Connection c=DriverManager.getConnection(URL);try(Statement s=c.createStatement()){s.execute("PRAGMA foreign_keys=ON");s.execute("PRAGMA busy_timeout=5000");}return c;}
 public static void initialize() throws SQLException {try(Connection c=getConnection();Statement s=c.createStatement()){s.executeUpdate("CREATE TABLE IF NOT EXISTS messages(message_id TEXT PRIMARY KEY,message_text TEXT NOT NULL,category TEXT NOT NULL,created_at TEXT NOT NULL,discovery_count INTEGER NOT NULL DEFAULT 0)");s.executeUpdate("CREATE TABLE IF NOT EXISTS future_letters(letter_id TEXT PRIMARY KEY,letter_text TEXT NOT NULL,target_date TEXT NOT NULL,created_at TEXT NOT NULL)");}}
}
