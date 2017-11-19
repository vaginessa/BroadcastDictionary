package com.chronoscoper.android.broadcastdictionary

import android.app.Application
import com.chronoscoper.android.broadcastdictionary.database.OrmaDatabase

class DictionaryApplication : Application() {
    val database by lazy { OrmaDatabase.Builder(this).build() }
}
