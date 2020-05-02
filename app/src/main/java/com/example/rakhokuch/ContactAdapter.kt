package com.example.rakhokuch

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.rakhokuch.roomDB.Contacts

class ContactAdapter(
    private val context: Context
) : ListAdapter<Contacts, ContactHolder>(ContactDiffCallback()) {

    override fun onBindViewHolder(holder: ContactHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactHolder {
        val inflatedView = LayoutInflater.from(parent.context)
        return ContactHolder(inflatedView, parent, context)
    }


}

class ContactDiffCallback : DiffUtil.ItemCallback<Contacts>() {
    override fun areItemsTheSame(oldItem: Contacts, newItem: Contacts): Boolean {
        return oldItem.id == newItem.id
    }


    override fun areContentsTheSame(oldItem: Contacts, newItem: Contacts): Boolean {
        return oldItem == newItem
    }


}


