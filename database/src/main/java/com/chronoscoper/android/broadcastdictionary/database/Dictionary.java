package com.chronoscoper.android.broadcastdictionary.database;

import android.content.Intent;

import com.github.gfx.android.orma.annotation.Column;
import com.github.gfx.android.orma.annotation.Setter;
import com.github.gfx.android.orma.annotation.Table;

@Table("dictionary")
public class Dictionary {
    @Setter
    public Dictionary(String packageName, String kind, String word) {
        this.packageName = packageName;
        this.kind = kind;
        this.word = word;
    }

    @Column(value = "package_name", indexed = true)
    public String packageName;

    @Column(value = "kind", indexed = true)
    public String kind;

    @Column("word")
    public String word;
}
