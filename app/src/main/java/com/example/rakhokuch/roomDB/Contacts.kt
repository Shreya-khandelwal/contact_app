package com.example.rakhokuch.roomDB

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Contacts(


    @ColumnInfo(name = "CONTACT_NUMBER")
    var contactNumber: String,

    @ColumnInfo(name = "CONTACT_NAME")
    var contactName: String

)
{
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass){
            return false
        }
        other as Contacts

        if (id != other.id){
            return false
        }

        if (contactName != other.contactName){
            return false
        }

        if (contactNumber != other.contactNumber){
            return false
        }
        return true
    }
}