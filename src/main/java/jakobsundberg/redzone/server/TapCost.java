package jakobsundberg.redzone.server;

import static jakobsundberg.redzone.server.EventType.ManaPool;
import static jakobsundberg.redzone.server.EventType.Tap;

public class TapCost implements Cost{

    @Override
    public void pay(Game game, Player payer, Card source) {
        source.tap();
        Event event = new Event(Tap);
        event.addExtraData("playerId", payer.id);
        event.addExtraData("cardId", source.id);
        game.events.add(event);
    }

    @Override
    public String toString(){
        return "Tap";
    }
}
