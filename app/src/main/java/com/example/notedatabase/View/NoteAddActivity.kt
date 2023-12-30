package com.example.notedatabase.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.notedatabase.R

class NoteAddActivity : AppCompatActivity() {
    lateinit var etTitle : EditText
    lateinit var etDescription : EditText
    lateinit var btnCancel : Button
    lateinit var btnAdd : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_add)

        etTitle = findViewById(R.id.etNoteTitle)
        etDescription = findViewById(R.id.etNoteDescription)
        btnCancel = findViewById(R.id.btnCancel)
        btnAdd = findViewById(R.id.btnAdd)

        btnCancel.setOnClickListener {
            Toast.makeText(applicationContext,"Cancelled adding note",Toast.LENGTH_SHORT).show()
            finish() //quit this app
        }

        btnAdd.setOnClickListener {
            val title = etTitle.text.toString()
            val description = etDescription.text.toString()

            val intent = Intent()
            intent.putExtra("title",title)
            intent.putExtra("description",description)
            setResult(RESULT_OK,intent) //sending intent back to main, via the ActivityResult

            Toast.makeText(applicationContext,"note added",Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}