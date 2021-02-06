package jakobsundberg.redzone.server;

import static jakobsundberg.redzone.server.EventType.ManaPool;
import static jakobsundberg.redzone.server.EventType.Play;

public class ManaEffect implements Effect{
    public int amount;

    public ManaEffect(int amount) {
        this.amount = amount;
    }

    @Override
    public void takeEffect(Game game, Player owner, Card source) {
        owner.mana += amount;
        Event event = new Event(ManaPool);
        event.addExtraData("playerId", owner.id);
        event.addExtraData("amount", owner.mana);
        game.events.add(event);
    }

    @Override
    public String toString(){
        return "Gain " + amount + " mana";
    }
}
