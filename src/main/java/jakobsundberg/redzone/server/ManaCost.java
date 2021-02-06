package jakobsundberg.redzone.server;

import static jakobsundberg.redzone.server.EventType.Draw;
import static jakobsundberg.redzone.server.EventType.ManaPool;

public class ManaCost implements Cost{
    public int amount;

    public ManaCost(int amount){
        this.amount = amount;
    }

    @Override
    public void pay(Game game, Player payer, Card source) {
        payer.mana -= amount;
        Event event = new Event(ManaPool);
        event.addExtraData("playerId", payer.id);
        event.addExtraData("amount", payer.mana);
        game.events.add(event);
    }

    @Override
    public String toString(){
        return amount + " mana";
    }
}
