package com.fleet.studio.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "btDeviceTable")
class BluetoothDeviceSchema(
    @ColumnInfo(name = "btDeviceName") val btDeviceName: String,
    @ColumnInfo(name = "btDeviceAdd") val btDeviceAdd: String,
) {
    @PrimaryKey(autoGenerate = true)
    var id = 0

}