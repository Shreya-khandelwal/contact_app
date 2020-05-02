package com.example.rakhokuch.roomDB

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface ContactDao {
    @Query("SELECT * from Contacts ORDER BY LOWER(CONTACT_NAME)")
    fun getContacts(): LiveData<List<Contacts>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(mobileGetDarConfigs: Contacts)

    @Query("DELETE FROM Contacts WHERE id = :cid")
    fun deleteContact(cid: Int)


    /*@Query("DELETE FROM Contacts")
    fun deleteAll()*/

    @Query("UPDATE Contacts SET CONTACT_NAME = :contactName , CONTACT_NUMBER = :contactNumber WHERE id = :cid")
    fun updateContact(contactName: String, contactNumber: String, cid: Int)

    @Query("SELECT * FROM Contacts WHERE CONTACT_NAME LIKE :search")
    fun searchContact(search: String):LiveData<List<Contacts>>
}
