package edu.cmu.inmind.messenger.comm;

import android.util.Base64;
import android.util.Log;

/**
 * Created by oscarr on 6/9/18.
 */

public class InMindMessage{

    private String message_id;
    private String message;
    private long createdAt;

    public InMindMessage(String message_id, String message){
        this.message_id = message_id;
        this.message = message;
    }

    public byte[] serialize(){
        try {
            message.replace(": ", ":")
                    .replace("\"", "");
            createdAt = System.currentTimeMillis();
            String json = "{\n" +
                    "\"channel_type\": \"group\",\n" +
                    "\"channel_url\": \"\n" +
                    "sendbird_group_channel_73872400_4eff1293ac39b70f91415d1116f5c7fe5bf9f7b6\",\n" +
                    "\"custom_type\": \"\",\n" +
                    "\"data\": \"\",\n" +
                    "\"message\": \"" + message + "\",\n" +
                    "\"message_id\": \"" + message_id + "\",\n" +
                    "\"req_id\": \"\",\n" +
                    "\"type\": \"MESG\",\n" +
                    "\"user\": {\n" +
                    "\"user_id\": \"InMind\",\n" +
                    "\"nickname\": \"InMind\",\n" +
                    "\"profile_url\": \"https://sendbird.com/main/img/profiles/profile_35_512px.png\",\n" +
                    "\"last_seen_at\": 0,\n" +
                    "\"is_active\": true\n" +
                    "},\n" +
                    "\"version\": \"3.0.61\",\n" +
                    "\"created_at\": " + createdAt + ",\n" + //1528476106569L
                    "\"updated_at\": 0\n" +
                    "}";
            json = json.replace("\n", "");
            Log.i("inmindMessage", json);
            byte[] data = Base64.encode(json.getBytes("UTF-8"), 0);
            for (int i = 0; i < data.length; ++i) {
                data[i] = (byte) (data[i] ^ i & 255);
            }
            return data;
        }catch (Exception e){
            Log.i("InMindMessage", "exception in serialize");
            e.printStackTrace();
        }
        return null;
    }

    public String getMessage() {
        return message;
    }

    public long getCreatedAt() {
        return createdAt;
    }
}
