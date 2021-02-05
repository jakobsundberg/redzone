package jakobsundberg.redzone.server;

public class TapCost implements Cost{

    @Override
    public void pay(Player payer, Card source) {
        source.tap();
    }

    @Override
    public String toString(){
        return "Tap";
    }
}
