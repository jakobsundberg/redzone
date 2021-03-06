package jakobsundberg.redzone.server;

import java.util.List;

public class ActivatedAbility {
    static int counter = 0;

    public int id;
    public List<Cost> costs;
    public List<Effect> effects;

    public ActivatedAbility(List<Cost> costs, List<Effect> effects) {
        id = counter++;
        this.costs = costs;
        this.effects = effects;
    }

    public void pay(Game game, Player owner, Card source){
        for(Cost cost : costs){
            cost.pay(game, owner, source);
        }
    }

    public void takeEffect(Game game, Player owner, Card source){
        for(Effect effect : effects){
            effect.takeEffect(game, owner, source);
        }
    }

    @Override
    public String toString(){
        return "Activated Ability " + id + " costs: " + costs + ", effects: " + effects;
    }
}
