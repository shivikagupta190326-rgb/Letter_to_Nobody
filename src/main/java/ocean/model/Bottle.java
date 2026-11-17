package ocean.model;
/** UI-friendly specialization of a public message. */
public class Bottle extends Message { public Bottle(Message m){super(m.getId(),m.getText(),m.getCategory(),m.getCreatedAt(),m.getDiscoveryCount());} }
