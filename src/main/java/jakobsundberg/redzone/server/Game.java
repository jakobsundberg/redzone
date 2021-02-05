package jakobsundberg.redzone.server;

import java.util.*;

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
    public List<String> events;

    public Game(){
        id = counter++;
        players = new ArrayList<>();
        attackers = new HashMap<>();
        events = new ArrayList<>();
    }

    public void join(Player player){
        players.add(player);
        events.add("Player " + player + " joined the game.");

        if(players.size() == MAX_SEATS){
            startGame();
        }
    }

    private void startGame() {
        events.add("The game has started.");
        Collections.shuffle(players);

        for(Player player : players){
            for(int i=0; i<7; i++){
                Card card = player.deck.remove(0);
                events.add("Player " + player + " has drawn " + card);
                player.hand.add(card);
            }
        }

        playerTurn = players.get(0);
        gameTurn = 0;
        events.add("Start of game turn " + gameTurn);
        startPlayerTurn();
    }

    private void startPlayerTurn() {
        events.add("Active player: " + playerTurn);
        changePhase(Phase.UNTAP);

        for(Card card : playerTurn.battlefield){
            card.untap();
            events.add("Untapped " + card);
        }

        startDrawPhase();
    }

    private void changePhase(Phase phase) {
        this.phase = phase;
        events.add("Phase changed to " + phase);
    }

    private void startDrawPhase() {
        changePhase(Phase.DRAW);
        Card card = playerTurn.deck.remove(0);
        events.add("Player " + playerTurn + " has drawn " + card);
        playerTurn.hand.add(card);
        startPrecombatMainphase();
    }

    private void startPrecombatMainphase() {
        changePhase(Phase.PRECOMBAT_MAINPHASE);
    }

    public void passPriority(){
        events.add("Player " + playerTurn + " passed priority.");

        if(phase == Phase.PRECOMBAT_MAINPHASE){
            playerTurn.mana = 0;
            startAttackPhase();
        }
        else if(phase == Phase.POSTCOMBAT_MAINPHASE){
            playerTurn.mana = 0;
            int playerIndex = players.lastIndexOf(playerTurn);
            playerIndex++;

            if(playerIndex >= players.size()){
                gameTurn++;
                playerTurn = players.get(0);
                events.add("Start of game turn " + gameTurn);
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
                events.add("Card " + attacker + " attacks " + target);
                target.life-= attacker.power;
                events.add("Target " + target + " now has " + target.life + " life.");

                if(target.life <= 0){
                    players.remove(target);
                    events.add("Player " + target + " died.");

                    if(players.size() == 1){
                        winner = players.get(0);
                        events.add("Player " + winner + " won the game!");
                        attackers.clear();
                        phase = null;
                        return;
                    }
                }
            }

            attackers.clear();
            changePhase(Phase.POSTCOMBAT_MAINPHASE);
        }
    }

    private void startAttackPhase() {
        changePhase(Phase.COMBAT);
    }

    public void play(Game game, Card card) {
        Player player = game.playerTurn;
        player.hand.remove(card);
        card.payCosts(player);
        events.add("Player " + player + " played " + card);
        player.battlefield.add(card);
        events.add(card + " entered the battlefield.");
    }

    public void activate(Card card, ActivatedAbility activatedAbility){
        Player player = playerTurn;
        events.add("Player " + player + " activates " + card + " ability " + activatedAbility);
        activatedAbility.pay(player, card);
        activatedAbility.takeEffect(player, card);
    }

    public void declareAttacker(Card attacker, Player target) {
        attackers.put(attacker, target);
        events.add("Player " + playerTurn + " declared " + attacker + " as attacking " + target);
    }
}
