package com.onlinetradeview.tv.mdel;

public class McxModel {
    private String name,date,tvchg,hnumber,mcxno,ltpone,mcxtwo,
            ltptwo;

    public McxModel(String name, String date, String tvchg, String hnumber, String mcxno, String ltpone, String mcxtwo, String ltptwo) {
        this.name = name;
        this.date = date;
        this.tvchg = tvchg;
        this.hnumber = hnumber;
        this.mcxno = mcxno;
        this.ltpone = ltpone;
        this.mcxtwo = mcxtwo;
        this.ltptwo = ltptwo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTvchg() {
        return tvchg;
    }

    public void setTvchg(String tvchg) {
        this.tvchg = tvchg;
    }

    public String getHnumber() {
        return hnumber;
    }

    public void setHnumber(String hnumber) {
        this.hnumber = hnumber;
    }

    public String getMcxno() {
        return mcxno;
    }

    public void setMcxno(String mcxno) {
        this.mcxno = mcxno;
    }

    public String getLtpone() {
        return ltpone;
    }

    public void setLtpone(String ltpone) {
        this.ltpone = ltpone;
    }

    public String getMcxtwo() {
        return mcxtwo;
    }

    public void setMcxtwo(String mcxtwo) {
        this.mcxtwo = mcxtwo;
    }

    public String getLtptwo() {
        return ltptwo;
    }

    public void setLtptwo(String ltptwo) {
        this.ltptwo = ltptwo;
    }
}
