package jakobsundberg.redzone.server;

public class ManaCost implements Cost{
    public int amount;

    public ManaCost(int amount){
        this.amount = amount;
    }

    @Override
    public void pay(Player payer, Card source) {
        payer.mana -= amount;
    }

    @Override
    public String toString(){
        return amount + " mana";
    }
}
