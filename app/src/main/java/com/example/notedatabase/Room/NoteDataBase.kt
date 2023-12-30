package com.example.notedatabase.Room

import android.content.Context
import android.provider.ContactsContract.Data
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.notedatabase.Model.Note
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

//If got other entities, [Note::class,SecondEntity::class]
@Database(entities = [Note::class], version = 1)
abstract class NoteDataBase : RoomDatabase() {

    abstract fun getNoteDao(): NoteDAO

    //singleton, just one instance
    companion object {
        //can be exposed to all thread
        @Volatile
        private var INSTANCE: NoteDataBase? = null

        fun getNoteDataBase(context: Context, scope: CoroutineScope): NoteDataBase {
            //elvin operation ?: = if null then....
            //if null, create a new DataBase
            //synchronized = run only on one thread
            return INSTANCE ?: synchronized(this) {
                val instance =
                    Room.databaseBuilder(
                        context.applicationContext,
                        NoteDataBase::class.java,
                        "note_database"
                    )
                        .addCallback(NoteDatabaseCallBack(scope))
                        .build()

                INSTANCE = instance

                instance //return this
            }
        }
    }

    //to add data for the database before the database even created, so the database has at least something
    private class NoteDatabaseCallBack(private val scope: CoroutineScope) :
        RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            //if not null
            INSTANCE?.let { database ->
                scope.launch {
                    val noteDao = database.getNoteDao()
                    noteDao.insert(
                        Note(
                            0,
                            "How to delete?",
                            "Swipe me to left or right to delete note",
                            0
                        )
                    )
                }
            }
        }
    }
}