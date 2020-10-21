package TimeManager.rest;

import java.util.Date;

/**
 * The class which a RestObject is created. There we have everything we need. The MAC Address of the TimeFlip and the Content which we got from the pi.
 */
public class RestObject {

    private String timeflipMac;
    private String content;
    private long id;

    public String getTimeflipMac() {
        return timeflipMac;
    }

    public void setTimeflipMac(String timeflipMac) {
        this.timeflipMac = timeflipMac;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getID(){
        return id;
    }

    public void setID(long id){
        this.id = id;
    }

}
