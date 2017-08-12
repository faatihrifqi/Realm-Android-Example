package com.latihanandroid.kerjakan.model;

import io.realm.RealmObject;

public class Kerjaan extends RealmObject {

    private String itemKerjaan;

    public String getItemKerjaan() {
        return itemKerjaan;
    }

    public void setItemKerjaan(String itemKerjaan) {
        this.itemKerjaan = itemKerjaan;
    }
}
