package com.onlinetradeview.tv.mdel;

/**
 * Created by ssridharatiwari on 2021.
 */
public class NotificationModel {
   String type, title, message, created;

    public NotificationModel(String type, String title, String message, String created) {
        this.type = type;
        this.title = title;
        this.message = message;
        this.created = created;
    }

    public NotificationModel(String type, String message, String created) {
        this.type = type;
        this.message = message;
        this.created = created;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}