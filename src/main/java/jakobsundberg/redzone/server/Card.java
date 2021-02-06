package jakobsundberg.redzone.server;

import java.util.List;

public class Card {
    static int counter = 0;

    public int id;
    public CardIdentity cardIdentity;
    public CardType cardType;
    public List<ActivatedAbility> activatedAbilities;
    public boolean isTapped;
    public List<Cost> costs;
    public int power;
    public int toughness;

    public Card(CardIdentity cardIdentity) {
        id = counter++;
        this.cardIdentity = cardIdentity;
        cardType = cardIdentity.cardType;
        activatedAbilities = cardIdentity.activatedAbilities;
        costs = cardIdentity.costs;
        power = cardIdentity.power;
        toughness = cardIdentity.toughness;
        isTapped = false;
    }

    public void tap(){
        isTapped = true;
    }

    public void untap(){
        isTapped = false;
    }

    public void payCosts(Game game, Player owner){
        for(Cost cost : costs){
            cost.pay(game, owner, this);
        }
    }

    @Override
    public String toString(){
        return id + ": " + cardIdentity.name + " with " + activatedAbilities;
    }
}
