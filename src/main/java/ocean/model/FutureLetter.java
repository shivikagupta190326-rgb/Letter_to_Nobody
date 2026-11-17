package ocean.model;
import java.time.LocalDate;
import java.time.LocalDateTime;
public class FutureLetter {
 private final String id,text; private final LocalDate targetDate; private final LocalDateTime createdAt;
 public FutureLetter(String text,LocalDate targetDate){this(java.util.UUID.randomUUID().toString(),text,targetDate,LocalDateTime.now());}
 public FutureLetter(String id,String text,LocalDate targetDate,LocalDateTime createdAt){this.id=id;this.text=text;this.targetDate=targetDate;this.createdAt=createdAt;}
 public String getId(){return id;} public String getText(){return text;} public LocalDate getTargetDate(){return targetDate;} public LocalDateTime getCreatedAt(){return createdAt;}
 public boolean isReady(){return !targetDate.isAfter(LocalDate.now());}
}
