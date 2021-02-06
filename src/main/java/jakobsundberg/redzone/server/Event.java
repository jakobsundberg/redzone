package jakobsundberg.redzone.server;

import java.util.HashMap;
import java.util.Map;

public class Event {
    EventType type;
    Map<String, String> extraData;

    public Event(EventType type){
        this.type = type;
        extraData = new HashMap<>();
    }

    public void addExtraData(String key, String value){
        extraData.put(key, value);
    }

    public void addExtraData(String key, int value) {
        extraData.put(key, String.valueOf(value));
    }

    public String toString(){
        String result = "{\"eventType\":\""+type+"\",";

        for(Map.Entry<String, String> entry : extraData.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            result += "\"" + key + "\":\"" + value + "\",";
        }
        result+="}";

        return result;
    }
}
