package com.example.megha.myapplication.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Continue {


    @SerializedName("picontinue")
    @Expose
    private Integer picontinue;
    @SerializedName("continue")
    @Expose
    private String _continue;

    public Integer getPicontinue() {
        return picontinue;
    }

    public void setPicontinue(Integer picontinue) {
        this.picontinue = picontinue;
    }

    public String getContinue() {
        return _continue;
    }

    public void setContinue(String _continue) {
        this._continue = _continue;
    }

}
