package com.tonini.diego.musicat.entity;

import android.net.Uri;

import java.io.File;

/**
 * Created by Diego on 24/07/2015.
 */
public class Album extends Item{

    private Uri almbumUri;

    public Album(){
    }

    public void setName(String name) {
        super.setFirstTitle(name);
    }

    public void setAuthor(String author) {
        super.setSecondTitle(author);
    }

    public String getName() {
        return super.getFirstTitle();
    }

    public String getAuthor() {
        return super.getSecondTitle();
    }

    public void setAlmbumUri(Uri almbumUri) {
        this.almbumUri = almbumUri;
    }

    public Uri getAlmbumUri() {
        return almbumUri;
    }
}
