package com.example.rakhokuch

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rakhokuch.roomDB.ContactViewModel
import com.example.rakhokuch.roomDB.Contacts
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.add_contact.*
import kotlinx.android.synthetic.main.add_contact.view.*
import kotlinx.android.synthetic.main.contact_child.view.*
import kotlinx.android.synthetic.main.layout_edit_contact.*
import kotlinx.android.synthetic.main.layout_edit_contact.view.*


class MainActivity : AppCompatActivity() {

    private var searchView: SearchView? = null
    private var tvname: TextView? = null
    private var tvphone: TextView? = null
    var contactList = ArrayList<Contacts>()
    var objAdapter: ContactAdapter? = null
    var p = Paint()
    var contactViewModel: ContactViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {

        tvname = findViewById(R.id.detail_tv_name)
        tvphone = findViewById(R.id.detail_tv_number)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.toolbar))
        val actionBar = supportActionBar
        actionBar!!.title = "Contacts"
        supportActionBar?.setDisplayHomeAsUpEnabled(false)    //disable back button
        supportActionBar?.setDisplayShowHomeEnabled(true)

        rv_contact.layoutManager = LinearLayoutManager(this)
        objAdapter = ContactAdapter(this)
        rv_contact.adapter = objAdapter
        contactViewModel = ViewModelProvider(this).get(ContactViewModel::class.java)
        contactViewModel?.contact?.observe(this, Observer {

            //fetching
            for (contact in it) {
                Log.d("ListUpdate", contact.contactName)
            }

            if (it.isEmpty()) {
                saveContactsToDB()
            } else {
                objAdapter?.submitList(it)
                contactList.clear()
                contactList.addAll(it)
            }
        })

        enableSwipe()
        addNewContact()
    }


    override fun onSupportNavigateUp(): Boolean {
        searchView?.isIconified = true
        return true
    }

    fun saveContactsToDB() {
        val contacts = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )
        var lastNumber = "0"

        while (contacts!!.moveToNext()) {
            val name =
                contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            var phoneNumber =
                contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

            if (phoneNumber.contains("+")) {
                phoneNumber = phoneNumber.substring(3, phoneNumber.length)
            }
            if (phoneNumber != lastNumber) {
                lastNumber = phoneNumber
                //contactList.add(contactModel)
                val con = Contacts(phoneNumber, name)
                contactViewModel?.insert(con)
            }
            /* val photoUri =
                 contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI))

             if (photoUri != null) {
                 contactModel.image =
                     MediaStore.Images.Media.getBitmap(contentResolver, Uri.parse(photoUri))
             }*/
        }
        contacts.close()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.main, menu)
        val searchItem = menu.findItem(R.id.menu_search)
        if (searchItem != null) {
            searchView = searchItem.actionView as SearchView

            searchView?.queryHint = "Search here..."

            searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {

                    if (newText!!.isNotEmpty()) {
                        val search = "%$newText%"
                        contactViewModel?.searchContact(search)
                            ?.observe(this@MainActivity, Observer {
                                objAdapter?.submitList(it)
                            })
                        /*       for (i in contactList.indices) {
                                   if (search.length <= contactList[i].contactName.toLowerCase().length && contactList[i].contactName.toLowerCase().contains(
                                           search
                                       )
                                   )
                                       displayList.add(contactList[i])
                                   if (search.length <= contactList[i].contactNumber.length) {
                                       if (contactList[i].contactNumber.contains(search))
                                           displayList.add(contactList[i])
                                   }
                               }*/

                    } else {
                        //displayList.clear()
                        objAdapter?.submitList(contactList)


                    }
                    return true
                }
            })
        }
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        return super.onCreateOptionsMenu(menu)
    }

    private fun enableSwipe() {
        val simpleItemTouchCallback =
            object :
                ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    val mDialogView = LayoutInflater.from(this@MainActivity)
                        .inflate(R.layout.layout_edit_contact, null)
                    //AlertDialogBuilder
                    val mBuilder = AlertDialog.Builder(this@MainActivity)
                        .setView(mDialogView)
                        .setTitle("Edit Contact")

                    val editName = contactList[position].contactName
                    val editNumber = contactList[position].contactNumber
                    val contactID = contactList[position].id

                    if (direction == ItemTouchHelper.LEFT) {
                        contactViewModel?.deleteContact(contactID)
                        //Log.d("delete contact","gdf", contactList.size[].toString())
                    } else {
                        //show dialog
                        val mAlertDialog = mBuilder.show()

                        val editTextName = mDialogView.edit_name
                        editTextName.setText(editName).toString()

                        val editTextNumber = mDialogView.edit_number
                        editTextNumber.setText(editNumber).toString()

                        //submit button click of custom layout
                        mDialogView.submit_button.setOnClickListener {
                            val name = editTextName.text.toString()
                            val number = editTextNumber.text.toString()
                            Log.d("ContactUpdate :out", name)
                            Log.d("ContactUpdate :out", number)
                            contactViewModel?.updateContact(
                                name, number, contactID
                            )
                            mAlertDialog.dismiss()
                        }

                        //cancel button click of custom layout
                        mDialogView.cancel_button.setOnClickListener {
                            objAdapter?.notifyDataSetChanged()
                            mAlertDialog.dismiss()
                        }
                    }
                }

                override fun onChildDraw(
                    c: Canvas,
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    dX: Float,
                    dY: Float,
                    actionState: Int,
                    isCurrentlyActive: Boolean
                ) {

                    val icon: Bitmap?
                    if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                        val itemView = viewHolder.itemView
                        val height = itemView.bottom.toFloat() - itemView.top.toFloat()
                        val width = height / 3

                        if (dX > 0) {
                            p.color = Color.parseColor("#388E3C")
                            val background =
                                RectF(
                                    itemView.left.toFloat(),
                                    itemView.top.toFloat(),
                                    dX,
                                    itemView.bottom.toFloat()
                                )
                            c.drawRect(background, p)
                            val drawable = getDrawable(R.drawable.ic_pencil)
                            icon = drawableToBitmap(drawable)
                            val iconDest = RectF(
                                itemView.left.toFloat() + width,
                                itemView.top.toFloat() + width,
                                itemView.left.toFloat() + 2 * width,
                                itemView.bottom.toFloat() - width
                            )
                            c.drawBitmap(icon, null, iconDest, p)
                        } else {
                            p.color = Color.parseColor("#D32F2F")
                            val background = RectF(
                                itemView.right.toFloat() + dX,
                                itemView.top.toFloat(),
                                itemView.right.toFloat(),
                                itemView.bottom.toFloat()
                            )
                            c.drawRect(background, p)
                            val drawable = getDrawable(R.drawable.ic_trash)
                            icon = drawableToBitmap(drawable)
                            val iconDest = RectF(
                                itemView.right.toFloat() - 2 * width,
                                itemView.top.toFloat() + width,
                                itemView.right.toFloat() - width,
                                itemView.bottom.toFloat() - width
                            )
                            c.drawBitmap(icon, null, iconDest, p)
                        }
                    }
                    super.onChildDraw(
                        c,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                }
            }
        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(rv_contact)
    }

    fun drawableToBitmap(drawable: Drawable): Bitmap? {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    fun addNewContact() {

        fab.setOnClickListener {
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.add_contact, null)
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)
                .setTitle("Add Contact")
            val mAlertDialog = mBuilder.show()
            Toast.makeText(applicationContext, "Add Contact", Toast.LENGTH_SHORT).show()

            //save button click of custom layout
            mDialogView.Save_button.setOnClickListener {

                //get text from EditTexts of custom layout
                if(new_name!=null && new_number!=null)
                {
                    val newName = mDialogView.new_name.text.toString()
                    val newNumber = mDialogView.new_number.text.toString()
                    Log.d("SaveContact : name", newName)
                    Log.d("SaveContact :number", newNumber)
                    //val contactModel = Contacts(newNumber, newName)
                    //objAdapter?.notifyDataSetChanged()
                    val con = Contacts(newNumber, newName)
                    contactViewModel?.insert(con)
                    mAlertDialog.dismiss()
                }
                else{
                    Toast.makeText(applicationContext, "Please enter contact fields", Toast.LENGTH_SHORT).show()
                }

            }

            //cancel button
            mDialogView.cancel_button_add_contact.setOnClickListener {
                //dismiss dialog
                mAlertDialog.dismiss()
            }
        }
    }
}

