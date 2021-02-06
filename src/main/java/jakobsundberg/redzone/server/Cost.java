package jakobsundberg.redzone.server;

public interface Cost {
    public void pay(Game game, Player owner, Card source);
}
