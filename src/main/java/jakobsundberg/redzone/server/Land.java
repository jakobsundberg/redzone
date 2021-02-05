package jakobsundberg.redzone.server;

import java.util.LinkedList;
import java.util.List;

public class Land extends CardIdentity{

    public Land(int multiverseId, String name, CardType cardType, List<ActivatedAbility> activatedAbilities) {
        super(multiverseId, name, cardType, activatedAbilities, new LinkedList<>(), 0, 0);
    }
}
