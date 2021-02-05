package jakobsundberg.redzone.server;

import java.util.*;

public class Database {
    private Set<CardIdentity> cardIdentities = new HashSet<>();

    public Database() {
        List<Cost> swampActivatedAbilityCost = new LinkedList<>();
        swampActivatedAbilityCost.add(new TapCost());
        List<Effect> swampActivatedAbilityEffect = new LinkedList<>();
        swampActivatedAbilityEffect.add(new ManaEffect(1));
        List<ActivatedAbility> swampActivatedAbilities = new LinkedList<>();
        swampActivatedAbilities.add(new ActivatedAbility(swampActivatedAbilityCost, swampActivatedAbilityEffect));
        CardIdentity swamp = new Land(575, "Swamp", CardType.Land, swampActivatedAbilities);
        cardIdentities.add(swamp);

        List<ActivatedAbility> hulkingDevilActivatedAbilities = new LinkedList<>();
        List<Cost> hulkingDevilCosts = new ArrayList<>();
        hulkingDevilCosts.add(new ManaCost(4));
        CardIdentity hulkingDevil = new Creature(409919, "Hulking Devil", hulkingDevilCosts, CardType.Creature, hulkingDevilActivatedAbilities, 5, 2);
        cardIdentities.add(hulkingDevil);
    }

    public Set<User> getUsers() {
        Set<User> result = new HashSet<User>();
        result.add(new User("jakob"));
        result.add(new User("william"));
        result.add(new User("staffan"));
        return result;
    }

    public Set<CardIdentity> getCardIdentities() {
        return cardIdentities;
    }

    public Set<DeckList> getDeckLists() {
        Set<DeckList> result = new HashSet<DeckList>();
        DeckList one = new DeckList();

        for (CardIdentity cardIdentity : cardIdentities) {
            one.list.put(cardIdentity, 30);
        }

        result.add(one);
        return result;
    }
}
