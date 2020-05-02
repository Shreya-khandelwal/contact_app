package com.example.rakhokuch.roomDB

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData


class ContactViewModel(application: Application) : AndroidViewModel(application) {

    private val contactRepository: ContactRepository =
        ContactRepository(application)
    val contact: LiveData<List<Contacts>>

    init {
        contact = contactRepository.getContact()
    }

    fun insert(contacts: Contacts) {
        contactRepository.insert(contacts)
    }


    fun deleteContact(id:Int){
        contactRepository.deleteContact(id)
    }

    fun updateContact(name:String, number:String,id:Int) {
         contactRepository.updateContact(name,number,id)
    }

    fun searchContact(search:String):LiveData<List<Contacts>>
    {
        return contactRepository.searchContact(search)
    }
}
