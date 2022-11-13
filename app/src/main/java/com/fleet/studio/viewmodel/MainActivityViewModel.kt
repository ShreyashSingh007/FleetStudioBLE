package com.fleet.studio.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.fleet.studio.model.BluetoothDeviceSchema
import com.fleet.studio.model.btDeviceDatabase
import com.fleet.studio.model.btDeviceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

 class MainActivityViewModel(application: Application) : AndroidViewModel(application) {
//    val allbtDevice: LiveData<List<BluetoothDeviceSchema>>
//    val repository: btDeviceRepository
//
//    init {
//        val dao = btDeviceDatabase.getDatabase(application).getbtDeviceDao()
//        repository = btDeviceRepository(dao)
//        allbtDevice = repository.allbtDevice
//    }
//    fun addbtDevice(btDevice: BluetoothDeviceSchema) = viewModelScope.launch(Dispatchers.IO) {
//        repository.insert(btDevice)
//    }
}