package jakobsundberg.redzone.server;

import java.util.*;

import static jakobsundberg.redzone.server.EventType.*;

public class Game {
    public static int MAX_SEATS = 2;
    static int counter = 0;
    public Map<Card, Player> attackers;
    public List<Player> players;
    public int id;
    public int gameTurn;
    public Player playerTurn;
    public Phase phase;
    public Player winner;
    public List<Event> events;

    public Game(){
        id = counter++;
        players = new ArrayList<>();
        attackers = new HashMap<>();
        events = new ArrayList<>();
    }

    public void join(Player player){
        players.add(player);
        Event joinEvent = new Event(Join);
        joinEvent.addExtraData("playerId", player.id);
        joinEvent.addExtraData("playerName", player.user.username);
        events.add(joinEvent);

        if(players.size() == MAX_SEATS){
            startGame();
        }
    }

    private void startGame() {
        Collections.shuffle(players);

        for(Player player : players){
            setPlayerLife(player, 20);
            setPlayerMana(player, 0);

            for(Card card : player.deck){
                Event event = new Event(CardInfo);
                event.addExtraData("multiverseId", card.cardIdentity.multiverseId);
                event.addExtraData("cardName", card.cardIdentity.name);
                event.addExtraData("cardId", card.id);
                if(card.activatedAbilities.size() != 0){
                    ActivatedAbility activatedAbility = card.activatedAbilities.get(0);
                    event.addExtraData("activatedAbilityId", activatedAbility.id);
                }
                events.add(event);
            }

            Collections.shuffle(player.deck);

            for(int i=0; i<7; i++){
                drawCard(player);
            }
        }

        playerTurn = players.get(0);
        gameTurn = 0;
        Event event = new Event(GameTurn);
        event.addExtraData("turn", gameTurn);
        events.add(event);
        startPlayerTurn();
    }

    public void drawCard(Player player){
        Card card = player.deck.remove(0);
        Event event = new Event(Draw);
        event.addExtraData("playerId", player.id);
        event.addExtraData("cardId", card.id);
        events.add(event);
        player.hand.add(card);
    }

    private void setPlayerLife(Player player, int amount) {
        player.life = amount;
        Event lifeEvent = new Event(SetLifeTotal);
        lifeEvent.addExtraData("playerId", player.id);
        lifeEvent.addExtraData("lifeTotal", player.life);
        events.add(lifeEvent);
    }

    private void startPlayerTurn() {
        Event event = new Event(PlayerTurn);
        event.addExtraData("playerId", playerTurn.id);
        events.add(event);
        changePhase(Phase.UNTAP);

        for(Card card : playerTurn.battlefield){
            card.untap();
            Event untapEvent = new Event(Untap);
            untapEvent.addExtraData("cardId", card.id);
            untapEvent.addExtraData("playerId", playerTurn.id);
            events.add(untapEvent);
        }

        startDrawPhase();
    }

    private void changePhase(Phase phase) {
        this.phase = phase;
        Event event = new Event(PhaseChange);
        event.addExtraData("phase", String.valueOf(phase));
        events.add(event);
    }

    private void startDrawPhase() {
        changePhase(Phase.DRAW);
        drawCard(playerTurn);
        startPrecombatMainphase();
    }

    private void startPrecombatMainphase() {
        changePhase(Phase.PRECOMBAT_MAINPHASE);
    }

    public void passPriority(){
        if(phase == Phase.PRECOMBAT_MAINPHASE){
            setPlayerMana(playerTurn, 0);
            startAttackPhase();
        }
        else if(phase == Phase.POSTCOMBAT_MAINPHASE){
            setPlayerMana(playerTurn, 0);
            int playerIndex = players.lastIndexOf(playerTurn);
            playerIndex++;

            if(playerIndex >= players.size()){
                gameTurn++;
                playerTurn = players.get(0);
                Event event = new Event(GameTurn);
                event.addExtraData("turnCount", gameTurn);
                events.add(event);
            }
            else{
                playerTurn = players.get(playerIndex);
            }

            startPlayerTurn();
        }
        else if(phase == Phase.COMBAT){
            for(Map.Entry<Card, Player> entry : attackers.entrySet()){
                Card attacker = entry.getKey();
                Player target = entry.getValue();
                attacker.tap();
                Event event = new Event(Tap);
                event.addExtraData("cardId", attacker.id);
                events.add(event);
                setPlayerLife(target, target.life - attacker.power);

                if(target.life <= 0){
                    players.remove(target);
                    Event deathEvent = new Event(Death);
                    deathEvent.addExtraData("playerId", target.id);
                    events.add(deathEvent);

                    if(players.size() == 1){
                        winner = players.get(0);
                        Event winEvent = new Event(Victory);
                        winEvent.addExtraData("playerId", winner.id);
                        events.add(winEvent);
                        return;
                    }
                }
            }

            for(Card attacker : attackers.keySet()){
                Event clearEvent = new Event(ClearAttackers);
                clearEvent.addExtraData("playerId", playerTurn.id);
                clearEvent.addExtraData("cardId", attacker.id);
                events.add(clearEvent);
            }

            attackers.clear();
            changePhase(Phase.POSTCOMBAT_MAINPHASE);
        }
    }

    public void setPlayerMana(Player player, int amount) {
        player.mana = amount;
        Event event = new Event(ManaPool);
        event.addExtraData("playerId", player.id);
        event.addExtraData("mana", player.mana);
        events.add(event);
    }

    private void startAttackPhase() {
        changePhase(Phase.COMBAT);
    }

    public void play(Game game, Card card) {
        Player player = game.playerTurn;
        player.hand.remove(card);
        card.payCosts(this, player);
        Event event = new Event(Play);
        event.addExtraData("playerId", playerTurn.id);
        event.addExtraData("cardId", card.id);
        events.add(event);
        player.battlefield.add(card);
    }

    public void activate(Card card, ActivatedAbility activatedAbility){
        Player player = playerTurn;
        Event event = new Event(Activate);
        event.addExtraData("playerId", playerTurn.id);
        event.addExtraData("cardId", card.id);
        event.addExtraData("abilityId", activatedAbility.id);
        events.add(event);
        activatedAbility.pay(this, player, card);
        activatedAbility.takeEffect(this, player, card);
    }

    public void declareAttacker(Card attacker, Player target) {
        attackers.put(attacker, target);
        Event event = new Event(DeclareAttackers);
        event.addExtraData("playerId", playerTurn.id);
        event.addExtraData("cardId", attacker.id);
        event.addExtraData("targetId", target.id);
        events.add(event);
    }
}
