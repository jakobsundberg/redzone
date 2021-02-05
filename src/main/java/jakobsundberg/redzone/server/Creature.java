package jakobsundberg.redzone.server;

import java.util.List;

public class Creature extends CardIdentity{

    public Creature(int multiverseId, String name, List<Cost> costs, CardType cardType, List<ActivatedAbility> activatedAbilities, int power, int toughness) {
        super(multiverseId, name, cardType, activatedAbilities, costs, power, toughness);
    }
}
