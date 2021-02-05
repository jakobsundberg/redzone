package jakobsundberg.redzone.server;

import java.util.HashMap;
import java.util.Map;

public class DeckList {
    static int counter = 0;

    public int id;
    public Map<CardIdentity, Integer> list;

    public DeckList() {
        id = counter++;
        this.list = new HashMap<>();
    }
}
