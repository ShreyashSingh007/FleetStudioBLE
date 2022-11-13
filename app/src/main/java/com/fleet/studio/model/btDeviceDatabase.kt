package com.fleet.studio.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(BluetoothDeviceSchema::class), version = 1, exportSchema = false)
abstract class btDeviceDatabase : RoomDatabase() {

    abstract fun getbtDeviceDao(): btDeviceDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: btDeviceDatabase? = null

        fun getDatabase(context: Context): btDeviceDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    btDeviceDatabase::class.java,
                    "btDevice_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }


}