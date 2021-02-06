package jakobsundberg.redzone.server;

import java.util.*;

public class Server {
    public static Server INSTANCE = new Server();

    Map<String, User> users;
    Map<Integer, Game> games;
    Map<Integer, CardIdentity> cardIdentities;
    Map<Integer, Player> players;
    Map<Integer, Card> cards;
    Map<Integer, DeckList> deckLists;
    Map<Integer, ActivatedAbility> activatedAbilities;

    public Server() {
        users = new HashMap<>();
        games = new HashMap<>();
        players = new HashMap<>();
        cards = new HashMap<>();
        deckLists = new HashMap<>();
        cardIdentities = new HashMap<>();
        activatedAbilities = new HashMap<>();
        Database database = new Database();

        for (User user : database.getUsers()) {
            users.put(user.username, user);
        }

        for (CardIdentity cardIdentity : database.getCardIdentities()) {
            cardIdentities.put(cardIdentity.multiverseId, cardIdentity);

            for(ActivatedAbility activatedAbility : cardIdentity.activatedAbilities){
                activatedAbilities.put(activatedAbility.id, activatedAbility);
            }
        }

        for (DeckList deckList : database.getDeckLists()) {
            deckLists.put(deckList.id, deckList);
        }
    }

    public Game createGame(String username, int deckListId) {
        User user = users.get(username);
        DeckList deckList = deckLists.get(deckListId);
        Game game = new Game();
        games.put(game.id, game);
        Player player = new Player(user, createDeck(deckList));
        players.put(player.id, player);
        game.join(player);
        return game;
    }

    public Game joinGame(int gameId, String username, int deckListId) {
        Game game = games.get(gameId);
        User user = users.get(username);
        DeckList deckList = deckLists.get(deckListId);
        Player player = new Player(user, createDeck(deckList));
        game.join(player);
        players.put(player.id, player);
        return game;
    }

    private List<Card> createDeck(DeckList deckList) {
        List<Card> result = new LinkedList<>();

        for (Map.Entry<CardIdentity, Integer> entry : deckList.list.entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                Card card = new Card(entry.getKey());
                cards.put(card.id, card);
                result.add(card);
            }
        }

        return result;
    }

    public Collection<Game> getGames() {
        return games.values();
    }

    public void declareAttacker(int gameId, int cardId, int targetId) {
        Game game = games.get(gameId);
        Card attacker = cards.get(cardId);
        Player target = players.get(targetId);
        game.declareAttacker(attacker, target);
    }

    public void passPriority(int gameId) {
        Game game = games.get(gameId);
        game.passPriority();
    }

    public void play(int gameId, int cardId) {
        Game game = games.get(gameId);
        Card card = cards.get(cardId);
        game.play(game, card);
    }

    public List<Event> getGameEvents(int gameId, int from) {
        Game game = games.get(gameId);
        if(from >= game.events.size()){
            return new ArrayList<>();
        }
        else {
            return game.events.subList(from, game.events.size());
        }
    }

    public void activate(int gameId, int cardId, int activatedAbilityId){
        Game game = games.get(gameId);
        Card card = cards.get(cardId);
        ActivatedAbility activatedAbility = activatedAbilities.get(activatedAbilityId);
        game.activate(card, activatedAbility);
    }
}
