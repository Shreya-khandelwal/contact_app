package com.example.rakhokuch.roomDB

import android.app.Application
import android.os.AsyncTask
import android.util.Log

import androidx.lifecycle.LiveData


class ContactRepository internal constructor(application: Application) {
    private val contactDao: ContactDao
    private val contacts: LiveData<List<Contacts>>


    init {
        val db = ContactDB.getDatabase(application)
        contactDao = db!!.contactsDao()
        contacts = contactDao.getContacts()
    }

    internal fun getContact(): LiveData<List<Contacts>> {
        return contacts
    }


    internal fun insert(mobileGetInpUnits: Contacts) {
        InsertAsyncTask(contactDao)
            .execute(mobileGetInpUnits)
    }

    private class InsertAsyncTask internal constructor(private val mAsyncTaskDao: ContactDao) :
        AsyncTask<Contacts, Void, Void>() {

        override fun doInBackground(vararg params: Contacts): Void? {
            mAsyncTaskDao.insert(params[0])
            return null
        }
    }

    fun deleteContact(id: Int) {
        DeleteAsyncTask(contactDao,id).execute()
    }


    fun searchContact(search: String):LiveData<List<Contacts>> {
        return contactDao.searchContact(search)

    }

    fun updateContact(name: String, number: String, id: Int) {
        UpdateAsyncTask(contactDao, name, number, id).execute()
    }


    private class UpdateAsyncTask internal constructor(
        private val mAsyncTaskDao: ContactDao, val name: String, val number: String, val id: Int
    ) :

        AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg voids: Void): Void? {

            mAsyncTaskDao.updateContact(name, number, id)
            return null
        }
    }

    private class DeleteAsyncTask internal constructor(private val mAsyncTaskDao: ContactDao,val id: Int) :

        AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg voids: Void): Void? {

            mAsyncTaskDao.deleteContact(id)
            return null
        }
    }
}
