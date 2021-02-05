package jakobsundberg.redzone.server;

import java.util.LinkedList;
import java.util.List;

public class Player {
    static int counter = 0;

    public int id;
    public User user;
    public int life;
    public int mana;
    public List<Card> deck;
    public List<Card> hand;
    public List<Card> battlefield;

    public Player(User user, List<Card> deck) {
        id = counter++;
        this.user = user;
        life = 20;
        mana = 0;
        this.deck = deck;
        hand = new LinkedList<>();
        battlefield = new LinkedList<>();
    }

    @Override
    public String toString(){
        return id + ": " + user.username;
    }
}
