package com.voxalto.automotovoxalto.model;

/**
 * Created by Hassnae on 14/04/2016.
 */
public class ItemSildeMenu {

    private int imgId;
    private String title;

    public ItemSildeMenu(int imgId, String title) {
        this.imgId = imgId;
        this.title = title;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
