package com.chronoscoper.android.broadcastdictionary

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.*
import com.chronoscoper.android.broadcastdictionary.database.Dictionary

class DictionaryService : Service() {
    companion object {
        private const val WHAT_REGISTER = 1
        private const val WHAT_LOAD = 2
        private const val DICTIONARY_PACKAGE =
                "com.chronoscoper.android.broadcastdictionary.PACKAGE"
        private const val DICTIONARY_KIND = "com.chronoscoper.android.broadcastdictionary.KIND"
        private const val DICTIONARY_WORD = "com.chronoscoper.android.broadcastdictionary.WORD"
        private const val DICTIONARY_RESULT = "com.chronoscoper.android.broadcastdictionary.WORD"
    }

    private val messenger by lazy { Messenger(WordHandler(applicationContext)) }

    override fun onBind(intent: Intent?): IBinder = messenger.binder

    class WordHandler(private val context: Context) : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            msg?.data ?: return
            val packageName = msg.data[DICTIONARY_PACKAGE] as String? ?: return
            val kind = msg.data[DICTIONARY_KIND] as String? ?: return
            val word = msg.data[DICTIONARY_WORD] as String?
            val application = context.applicationContext as DictionaryApplication
            when (msg.what) {
                WHAT_REGISTER -> {
                    word ?: return
                    application.database.insertIntoDictionary(Dictionary(packageName, kind, word))
                }
                WHAT_LOAD -> {
                    val replyMessenger = msg.replyTo ?: return
                    val message = Message.obtain().apply {
                        data = Bundle().apply {
                            putStringArray(DICTIONARY_RESULT,
                                    MutableList::class.java.cast(
                                            application.database.selectFromDictionary()
                                                    .packageNameEq(packageName)
                                                    .kindEq(kind)
                                                    .toList())
                                            .map { (it as Dictionary).word }
                                            .toTypedArray())
                        }
                    }
                    replyMessenger.send(message)
                }
            }
            msg.recycle()
        }
    }
}
