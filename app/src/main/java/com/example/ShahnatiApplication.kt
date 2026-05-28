package com.example

import android.app.Application
import com.example.data.AppDatabase
import com.example.data.TransportRepository

class ShahnatiApplication : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { TransportRepository(database.transportDao()) }
}
