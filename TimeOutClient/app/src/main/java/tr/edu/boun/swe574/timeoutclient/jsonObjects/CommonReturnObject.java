package tr.edu.boun.swe574.timeoutclient.jsonObjects;

/**
 * Created by haluks on 19/04/15.
 */
public class CommonReturnObject {

    // {"type":"Success","message":"It is successfully done."}

    String type;
    String message;
    String sessionId;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
