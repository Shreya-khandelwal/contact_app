package com.example.rakhokuch

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_details.*


class DetailsActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        setSupportActionBar(findViewById(R.id.toolbar))
        val actionBar = supportActionBar

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        val name = intent.extras.getString("name")
        actionBar!!.title = name
        val number = intent.extras.getString("number")
        detail_tv_name.text = name
        detail_tv_number.text = number
    }
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
