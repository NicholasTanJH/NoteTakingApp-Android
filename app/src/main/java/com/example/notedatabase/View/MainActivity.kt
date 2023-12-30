package com.example.notedatabase.View

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notedatabase.Adapter.NoteAdapter
import com.example.notedatabase.Model.Note
import com.example.notedatabase.NoteApplication
import com.example.notedatabase.R
import com.example.notedatabase.ViewModel.NoteViewModel
import com.example.notedatabase.ViewModel.NoteViewModelFactory

//Entity = Note
//DAO = methods to manipulate database
//database = include which entity, DAO, and can return database(itself!)
//repository = get DAO (get all those method), bridge between UI and getting database
//application = will alive all the time? So won't need to construct new database and repository every time
class MainActivity : AppCompatActivity() {
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var addActivityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var noteAdapter: NoteAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //ViewModel
        //if don't have para
//        noteViewModel = ViewModelProvider(this)[NoteViewModel::class.java]
        //have para, need to create ViewModelProvider.Factory class (in ViewModel)
        val noteViewModelFactory = NoteViewModelFactory((application as NoteApplication).repository)
        noteViewModel = ViewModelProvider(this, noteViewModelFactory)[NoteViewModel::class.java]


        //ActivityResultLauncher
        registerActivityResultLauncher()


        //recycleView
        val recyclerView = findViewById<RecyclerView>(R.id.rvNote)
        recyclerView.layoutManager = LinearLayoutManager(this)
        noteAdapter = NoteAdapter()
        recyclerView.adapter = noteAdapter

        //liveData of allNotes List<Note>
        noteViewModel.myAllNote.observe(this) { notes ->
            noteAdapter.setNote(notes)
        }

        //********************************Swipe ViewHolder****************************************

        //dragDirs is for drag and drop, x use, 0
        //onMove = drag and drop
        ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {

                val fromPosition = viewHolder.adapterPosition //started position
                val toPosition = target.adapterPosition //ended position

                val oriNote1 = noteAdapter.getNote(fromPosition) //note that are dragged
                val oriNote2 = noteAdapter.getNote(toPosition) //note that are affected
                val newNote1 = Note(oriNote1.id,oriNote1.title,oriNote1.description,toPosition)
                val newNote2 = Note(oriNote2.id,oriNote2.title,oriNote2.description,fromPosition)
                noteAdapter.isItemMoved() //setting up boiler code to disable notifyDataSetChanged()
                noteViewModel.update(newNote1,newNote2)
                noteAdapter.notifyItemMoved(fromPosition,toPosition)
                return true
            }


            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition // get current swiped position
                noteViewModel.delete(noteAdapter.getNote(position))

                Toast.makeText(this@MainActivity,"Deleted",Toast.LENGTH_SHORT).show()
            }

        }).attachToRecyclerView(recyclerView)
    }

    private fun registerActivityResultLauncher() {
        addActivityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
                ActivityResultCallback { resultAddNote ->
                    val resultCode = resultAddNote.resultCode
                    val data = resultAddNote.data //the returned intent, contain title & description
                    if (resultCode == RESULT_OK && data != null) {
                        val noteTitle = data.getStringExtra("title").toString()
                        val noteDescription = data.getStringExtra("description").toString()

                        val position = noteAdapter.getNoteSize() - 1
                        val note = Note(0,noteTitle, noteDescription,position)
                        noteViewModel.insert(note)
                    }
                })
    }

    //*******************************menu item ******************************************
    //at the top right of the action bar
    /*  1) create menu res folder
        2) create menu xml
        3) put in menu item
        4) id, name, logo,
        by default will show 3 dots, clicked will come down all the item, showAsAction = can set it to logo button
     */

    //create menu by inflating it
    //since is boolean, return true, i guess mean to show or not
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.new_menu, menu)
        return true
    }

    //add functionality to all of the option items
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add_note -> {
                val intent = Intent(this, NoteAddActivity::class.java)
                //start an activity like startActivity, but can...
                //using ActivityResultLauncher, can get back the title & description once the note added from the new activity
                //by setResult(RESULT_OK,intent.setExtra(...))
                addActivityResultLauncher.launch(intent)
            }
            R.id.item_delete_all_note -> {
                val totalNumNotes : Int = noteAdapter.getNoteSize()

                val dialogMessage = AlertDialog.Builder(this)
                    .setTitle("Delete All?")
                    .setMessage("If you want to just delete one note, you can swipe left or right to do so.\nThere are currently have total of $totalNumNotes note/s")
                    .setIcon(R.drawable.ic_baseline_delete_forever_24)
                    .setPositiveButton("Yes",DialogInterface.OnClickListener { dialog, which ->
                        noteViewModel.deleteAll()
                        Toast.makeText(this,"Deleted all", Toast.LENGTH_SHORT).show()
                    })
                    .setNegativeButton("No",DialogInterface.OnClickListener { dialog, which ->
                        dialog.cancel()
                    })
                    .show()
            }
        }

        return true
    }
}