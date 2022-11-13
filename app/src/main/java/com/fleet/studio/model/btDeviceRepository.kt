package com.fleet.studio.model
import androidx.lifecycle.LiveData

class btDeviceRepository(private val btDeviceDao: btDeviceDao) {


    val allbtDevice: LiveData<List<BluetoothDeviceSchema>> = btDeviceDao.getAllbtDevice()

    suspend fun insert(bluetoothDeviceSchema: BluetoothDeviceSchema) {
        btDeviceDao.insert(bluetoothDeviceSchema)
    }
    suspend fun delete(bluetoothDeviceSchema: BluetoothDeviceSchema){
        btDeviceDao.delete(bluetoothDeviceSchema)
    }

    suspend fun update(bluetoothDeviceSchema: BluetoothDeviceSchema){
        btDeviceDao.update(bluetoothDeviceSchema)
    }
}