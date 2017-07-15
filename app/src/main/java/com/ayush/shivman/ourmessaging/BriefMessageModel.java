package com.ayush.shivman.ourmessaging;

/**
 * Created by SHIVM@n on 7/11/2017.
 */

public class BriefMessageModel {
    String username="";
    String briefmessage="";
    int no_of_unread;
    int image;
    public String getUserName()
    {
        return username;
    }

    public void setUserName(String username) {
        this.username = username;
    }
    public String getBriefMessage()
    {
        return briefmessage;
    }

    public void setBriefMessage(String briefmessage) {
        this.briefmessage = briefmessage;
    }
    public int getImage()
    {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
    public int getunread()
    {
        return no_of_unread;
    }

    public void setunread(int no_of_unread) {
        this.no_of_unread = no_of_unread;
    }

}
