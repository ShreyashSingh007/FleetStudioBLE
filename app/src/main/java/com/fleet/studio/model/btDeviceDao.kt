package com.fleet.studio.model

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface btDeviceDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(bluetoothDeviceSchema : BluetoothDeviceSchema)

    @Update
    suspend fun update(bluetoothDeviceSchema: BluetoothDeviceSchema)

    @Delete
    suspend fun delete(bluetoothDeviceSchema: BluetoothDeviceSchema)

    @Query("Select * from btDeviceTable order by id ASC")
    fun getAllbtDevice(): LiveData<List<BluetoothDeviceSchema>>


}