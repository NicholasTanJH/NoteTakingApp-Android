package com.example.notedatabase

import android.app.Application
import com.example.notedatabase.Repository.NoteRepository
import com.example.notedatabase.Room.NoteDataBase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

//manifest, name = ...o
class NoteApplication() : Application() {
    //when child coroutine failed, the other child coroutine still run
    private val applicationScope = CoroutineScope(SupervisorJob())

    //lateinit
    val database by lazy{ NoteDataBase.getNoteDataBase(this, applicationScope)}
    val repository by lazy{ NoteRepository(database.getNoteDao()) }
}