package ocean.service;
import ocean.database.DatabaseConnection;import ocean.model.FutureLetter;import ocean.utils.ValidationUtil;import java.sql.*;import java.time.*;import java.util.*;
public class FutureLetterService implements Service {
 public void initialize()throws SQLException{DatabaseConnection.initialize();}
 public FutureLetter save(String text,LocalDate date)throws SQLException{String clean=ValidationUtil.validate(text);if(date==null||date.isBefore(LocalDate.now()))throw new IllegalArgumentException("Choose today or a date in the future.");FutureLetter f=new FutureLetter(clean,date);try(Connection c=DatabaseConnection.getConnection();PreparedStatement p=c.prepareStatement("INSERT INTO future_letters VALUES(?,?,?,?)")){p.setString(1,f.getId());p.setString(2,f.getText());p.setString(3,date.toString());p.setString(4,f.getCreatedAt().toString());p.executeUpdate();}return f;}
 public List<FutureLetter> all()throws SQLException{List<FutureLetter> a=new ArrayList<>();try(Connection c=DatabaseConnection.getConnection();Statement s=c.createStatement();ResultSet r=s.executeQuery("SELECT * FROM future_letters ORDER BY target_date")){while(r.next())a.add(new FutureLetter(r.getString("letter_id"),r.getString("letter_text"),LocalDate.parse(r.getString("target_date")),LocalDateTime.parse(r.getString("created_at"))));}return a;}
 public int count()throws SQLException{try(Connection c=DatabaseConnection.getConnection();Statement s=c.createStatement();ResultSet r=s.executeQuery("SELECT COUNT(*) FROM future_letters")){return r.next()?r.getInt(1):0;}}
}
