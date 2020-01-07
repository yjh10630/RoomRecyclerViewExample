package com.example.roomrecyclerviewexample.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.roomrecyclerviewexample.Person

@Database(entities = arrayOf(Person::class), version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun personDao(): PersonDAO
}