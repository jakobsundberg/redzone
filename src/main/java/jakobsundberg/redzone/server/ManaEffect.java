package jakobsundberg.redzone.server;

public class ManaEffect implements Effect{
    public int amount;

    public ManaEffect(int amount) {
        this.amount = amount;
    }

    @Override
    public void takeEffect(Player owner, Card source) {
        owner.mana += amount;
    }

    @Override
    public String toString(){
        return "Gain " + amount + " mana";
    }
}
