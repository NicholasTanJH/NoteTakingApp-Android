package com.example.notedatabase.Repository

import androidx.annotation.WorkerThread
import com.example.notedatabase.Model.Note
import com.example.notedatabase.Room.NoteDAO
import kotlinx.coroutines.flow.Flow

//repository = bridge to connect database and activity
//not a requirement to create repository, better practice

class NoteRepository(private val noteDao: NoteDAO) {
    val myAllNotes: Flow<List<Note>> = noteDao.getAllNotes()

    //@WorkerTread will run in a single thread, will not give error if don't use
    @WorkerThread
    suspend fun insert(note: Note) {
        noteDao.insert(note)
    }

    @WorkerThread
    suspend fun update(note: Note) {
        noteDao.update(note)
    }

    @WorkerThread
    suspend fun update(note1: Note, note2: Note) {
        noteDao.update(note1, note2)
    }

    @WorkerThread
    suspend fun delete(note: Note) {
        noteDao.delete(note)
    }

    @WorkerThread
    fun deleteAll() {
        noteDao.deleteAll()
    }
}