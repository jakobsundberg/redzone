package jakobsundberg.redzone.server;

import java.util.List;

public class CardIdentity {
    public int multiverseId;
    public String name;
    public CardType cardType;
    public List<ActivatedAbility> activatedAbilities;
    public List<Cost> costs;
    int power;
    int toughness;

    public CardIdentity(int multiverseId, String name, CardType cardType, List<ActivatedAbility> activatedAbilities, List<Cost> costs, int power, int toughness) {
        this.multiverseId = multiverseId;
        this.name = name;
        this.cardType = cardType;
        this.activatedAbilities = activatedAbilities;
        this.costs = costs;
        this.power = power;
        this.toughness = toughness;
    }
}
