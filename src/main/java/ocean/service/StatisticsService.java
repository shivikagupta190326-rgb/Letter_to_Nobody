package ocean.service;
import java.sql.SQLException;import java.util.*;
public class StatisticsService {private final MessageService messages;private final FutureLetterService letters;public StatisticsService(MessageService m,FutureLetterService f){messages=m;letters=f;}public Map<String,Object> snapshot()throws SQLException{Map<String,Object>s=new LinkedHashMap<>();s.put("released",messages.count());s.put("discovered",messages.totalDiscoveries());s.put("letters",letters.count());s.put("categories",messages.categories());return s;}}
