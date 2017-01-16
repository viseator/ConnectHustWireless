package com.viseator.connecthustwireless;

/**
 * Created by viseator on 1/16/17.
 * Wu Di
 * Email: viseator@gmail.com
 */

public class ConnectResultBean {

    /**
     * userIndex : 32653966653236323932616530323866633765396637643732643662396138385f31302e31302e36342e375f55323031363134373533
     * result : success
     * message :
     * keepaliveInterval : 0
     * validCodeUrl :
     */

    private String userIndex;
    private String result;
    private String message;
    private int keepaliveInterval;
    private String validCodeUrl;

    public String getUserIndex() {
        return userIndex;
    }

    public void setUserIndex(String userIndex) {
        this.userIndex = userIndex;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getKeepaliveInterval() {
        return keepaliveInterval;
    }

    public void setKeepaliveInterval(int keepaliveInterval) {
        this.keepaliveInterval = keepaliveInterval;
    }

    public String getValidCodeUrl() {
        return validCodeUrl;
    }

    public void setValidCodeUrl(String validCodeUrl) {
        this.validCodeUrl = validCodeUrl;
    }
}
