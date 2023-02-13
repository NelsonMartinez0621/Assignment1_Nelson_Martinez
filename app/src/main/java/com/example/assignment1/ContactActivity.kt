package com.example.assignment1

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.Settings
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.assignment1.databinding.ActivityContactBinding
import com.google.android.material.snackbar.Snackbar

private const val REQUEST_CODE_READ_CONTACTS = 1
class ContactActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContactBinding
    private lateinit var contactNames: ListView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        contactNames = findViewById(R.id.contact_names)

        val hasReadContactsPermission = ContextCompat.checkSelfPermission(this,
            Manifest.permission.READ_CONTACTS
        )

        if (hasReadContactsPermission == PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS), REQUEST_CODE_READ_CONTACTS)
        }

        binding.fab.setOnClickListener { view ->
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_CONTACTS
                ) == PackageManager.PERMISSION_GRANTED) {
                val projection = arrayOf(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY);

                val cursor = contentResolver.query(
                    ContactsContract.Contacts.CONTENT_URI,
                    projection,
                    null,
                    null,
                    ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)

                val contacts = ArrayList<String>()
                cursor?.use {
                    while (it.moveToNext()) {
                        contacts.add(it.getString(it.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)))
                    }
                }

                val adapter = ArrayAdapter(this, R.layout.layout_details, R.id.name, contacts)
                contactNames.adapter = adapter
            } else {
                Snackbar.make(view, R.string.access_request, Snackbar.LENGTH_LONG).setAction(R.string.access_granted) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.READ_CONTACTS
                        )) {
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.READ_CONTACTS),
                            REQUEST_CODE_READ_CONTACTS
                        )
                    } else {
                        val intent = Intent()
                        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        val uri = Uri.fromParts("package", this.packageName, null)
                        intent.data = uri
                        this.startActivity(intent)
                    }
                }.show()
            }
        }
    }
}