package tr.edu.boun.swe574.timeoutclient.jsonObjects;

/**
 * Created by haluks on 19/04/15.
 */
public class CommonReturnObject {

    // {"type":"Success","message":"It is successfully done."}

    String type;
    String message;
    String cookie;

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

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }
}
