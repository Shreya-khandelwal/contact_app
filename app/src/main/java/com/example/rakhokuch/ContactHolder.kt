package com.example.rakhokuch

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.rakhokuch.roomDB.Contacts

class ContactHolder(
    inflater: LayoutInflater,
    parent: ViewGroup,
    private val context: Context
) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.contact_child, parent, false)) {
    private var name: TextView? = null
    private var number: TextView? = null
    var image: ImageView?= null
    var relativeLayout: RelativeLayout?=null


    init {
        name = itemView.findViewById(R.id.name)
        number = itemView.findViewById(R.id.number)
        image= itemView.findViewById(R.id.profile)
        relativeLayout= itemView.findViewById(R.id.contact_view)
    }
    fun bind(contact: Contacts) {
        name?.text = contact.contactName
        number?.text = contact.contactNumber
        relativeLayout?.setOnClickListener{
            val intent = Intent(context,DetailsActivity::class.java)
            intent.putExtra("name",contact.contactName)
            intent.putExtra("number",contact.contactNumber)
            context.startActivity(intent)
        }
    }
}
