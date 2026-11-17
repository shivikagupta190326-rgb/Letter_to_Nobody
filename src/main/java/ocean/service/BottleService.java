package ocean.service;
import ocean.model.*; import java.sql.SQLException; import java.util.*;
public class BottleService {private final MessageService messages;public BottleService(MessageService m){messages=m;}public Optional<Bottle> discover(Set<String> seen)throws SQLException{return discover(seen,null);}public Optional<Bottle> discover(Set<String> seen,String category)throws SQLException{return messages.random(seen,category).map(Bottle::new);}}
