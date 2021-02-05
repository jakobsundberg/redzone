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

    public User getUser(String username) {
        return users.get(username);
    }

    public void createGame(String username, int deckListId) {
        User user = users.get(username);
        Game game = new Game();
        DeckList deckList = deckLists.get(deckListId);
        Player player1 = new Player(user, createDeck(deckList));
        players.put(player1.id, player1);
        game.join(player1);
        games.put(game.id, game);
    }

    public void joinGame(int gameId, String username, int deckListId) {
        User user = users.get(username);
        Game game = games.get(gameId);
        DeckList deckList = deckLists.get(deckListId);
        Player player2 = new Player(user, createDeck(deckList));
        game.join(player2);
        players.put(player2.id, player2);
    }

    public Collection<Game> getGames() {
        return games.values();
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

        Collections.shuffle(result);
        return result;
    }

    public void declareAttacker(int gameId, int creatureId, int targetId) {
        Game game = games.get(gameId);
        Card attacker = cards.get(creatureId);
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

    public List<String> getGameEvents(int gameId) {
        Game game = games.get(gameId);
        return game.events;
    }

    public void activate(int gameId, int cardId, int activatedAbilityId){
        Game game = games.get(gameId);
        Card card = cards.get(cardId);
        ActivatedAbility activatedAbility = activatedAbilities.get(activatedAbilityId);
        game.activate(card, activatedAbility);
    }
}
