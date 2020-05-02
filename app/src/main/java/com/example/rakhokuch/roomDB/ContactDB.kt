package com.example.rakhokuch.roomDB

import android.content.Context
import android.os.AsyncTask
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Contacts::class], version = 5, exportSchema = false)
abstract class ContactDB : RoomDatabase() {


    abstract fun contactsDao(): ContactDao

    private class PopulateDbAsync internal constructor(db: ContactDB) :
        AsyncTask<Void, Void, Void>() {


        override fun doInBackground(vararg params: Void): Void? {

            return null
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ContactDB? = null
        private val sRoomDatabaseCallback = object : RoomDatabase.Callback() {

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                PopulateDbAsync(INSTANCE!!).execute()
            }
        }

        fun getDatabase(context: Context): ContactDB? {
            if (INSTANCE == null) {
                synchronized(ContactDB::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            ContactDB::class.java, "ContactDB"
                        )
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build()
                    }
                }
            }
            return INSTANCE
        }
    }
}