package com.example.notedatabase.Room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.notedatabase.Model.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDAO {

    @Insert
    suspend fun insert(note: Note)

    @Update
    suspend fun update(note: Note)

    @Update
    suspend fun update(note1: Note, note2: Note) //for drag and drop, change it at one time

    @Delete
    suspend fun delete(note: Note)

    @Query("DELETE FROM note_table")
    fun deleteAll()

    //* = all, ASC = Ascending
    //flow cuz want to get it when the data changes
    //no suspend cuz room already will does it in another thread
    //originally uses id ASC, since need to add drag and drop function
    @Query("SELECT * FROM note_table ORDER BY position ASC")
    fun getAllNotes(): Flow<List<Note>>
}