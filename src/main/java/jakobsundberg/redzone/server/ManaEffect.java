package jakobsundberg.redzone.server;

public class ManaEffect implements Effect {
    public int amount;

    public ManaEffect(int amount) {
        this.amount = amount;
    }

    @Override
    public void takeEffect(Game game, Player owner, Card source) {
        game.setPlayerMana(owner, owner.mana + amount);
    }

    @Override
    public String toString() {
        return "Gain " + amount + " mana";
    }
}
