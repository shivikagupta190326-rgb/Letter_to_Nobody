package ocean.model;
import java.time.LocalDateTime;
import java.util.UUID;
/** Immutable anonymous note released to the public ocean. */
public class Message {
 private final String id,text,category; private final LocalDateTime createdAt; private final int discoveryCount;
 public Message(String text,String category){this(UUID.randomUUID().toString(),text,category,LocalDateTime.now(),0);}
 public Message(String id,String text,String category,LocalDateTime createdAt,int discoveryCount){this.id=id;this.text=text;this.category=category;this.createdAt=createdAt;this.discoveryCount=discoveryCount;}
 public String getId(){return id;} public String getText(){return text;} public String getCategory(){return category;} public LocalDateTime getCreatedAt(){return createdAt;} public int getDiscoveryCount(){return discoveryCount;}
}
