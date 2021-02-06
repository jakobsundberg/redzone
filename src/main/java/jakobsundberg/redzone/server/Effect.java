package jakobsundberg.redzone.server;

public interface Effect {
    public void takeEffect(Game game, Player owner, Card source);
}
