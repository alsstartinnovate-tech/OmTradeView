package com.onlinetradeview.tv.mdel;

/**
 * Created by ssridharatiwari on 2021.
 */
public class SpinnerModel {
    String id;
    String title;
    String desc;
    String image;
    String strImgUrl;

    public SpinnerModel(String id, String title) {
        this.id = id;
        this.title = title;
        this.desc = "";
        this.image = "";
        this.strImgUrl = "";
    }

    public SpinnerModel(String id, String title, String strImgUrl) {
        this.id = id;
        this.title = title;
        this.desc = "";
        this.image = "";
        this.strImgUrl = strImgUrl;
    }

    public SpinnerModel(String id, String title, String desc, String image, String strImgUrl) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.image = image;
        this.strImgUrl = strImgUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getDesc() {
        return desc;
    }

    public String getStrImgUrl() {
        return strImgUrl;
    }

}