package com.fleet.studio.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.companion.AssociationRequest
import android.companion.BluetoothDeviceFilter
import android.companion.CompanionDeviceManager
import android.content.*
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.location.LocationManager
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.fleet.studio.databinding.ActivityMainBinding
import com.fleet.studio.databinding.ContentMainBinding
import com.fleet.studio.viewmodel.MainActivityViewModel
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import org.json.JSONObject


private const val SELECT_DEVICE_REQUEST_CODE = 0

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var includedView: ContentMainBinding
    private lateinit var viewModal: MainActivityViewModel

    private var mSettingsClient: SettingsClient? = null
    private var mLocationSettingsRequest: LocationSettingsRequest? = null
    private val REQUEST_CHECK_SETTINGS = 214
    private val REQUEST_ENABLE_GPS = 516
    private val TAG: String = "!!Bluetooth Broadcast!!"
    var obj = JSONObject()



    @RequiresApi(33)
    val Permission_S_UP = arrayOf(
        Manifest.permission.BLUETOOTH_CONNECT,
        Manifest.permission.POST_NOTIFICATIONS,
        Manifest.permission.BLUETOOTH,
        Manifest.permission.BLUETOOTH_ADMIN,
        Manifest.permission.BLUETOOTH_SCAN,
        Manifest.permission.BLUETOOTH_ADVERTISE,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,

        )
    val Permission_S_DOWN = arrayOf(
        Manifest.permission.BLUETOOTH,
        Manifest.permission.BLUETOOTH_ADMIN,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
    )

    private val deviceManager: CompanionDeviceManager by lazy {
        getSystemService(Context.COMPANION_DEVICE_SERVICE) as CompanionDeviceManager
    }


//    // Create a BroadcastReceiver for ACTION_FOUND.
//    private val receiver = object : BroadcastReceiver() {
//
//        @SuppressLint("MissingPermission")
//        override fun onReceive(context: Context, intent: Intent) {
//            val action: String? = intent.action
//            Log.e("Broadcast",action!!)
//            when(action) {
//                BluetoothDevice.ACTION_FOUND -> {
//                    // Discovery has found a device. Get the BluetoothDevice
//                    // object and its info from the Intent.
//                    val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
//                    val deviceName = device?.name
//                    val deviceHardwareAddress = device?.address // MAC address
//                    Log.e("Broadcast",action)
//                }
//            }
//        }
//    }


    // Create a BroadcastReceiver for ACTION_FOUND
    @SuppressLint("MissingPermission")
    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            val action = intent.action
            // When discovery finds a device
            if (action == BluetoothAdapter.ACTION_STATE_CHANGED) {
                val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)
                when (state) {
                    BluetoothAdapter.STATE_OFF -> Log.e(TAG, "onReceive: STATE OFF")
                    BluetoothAdapter.STATE_TURNING_OFF -> Log.e(
                        TAG,
                        "mBroadcastReceiver1: STATE TURNING OFF"
                    )
                    BluetoothAdapter.STATE_ON -> Log.e(TAG, "mBroadcastReceiver1: STATE ON")
                    BluetoothAdapter.STATE_TURNING_ON -> Log.e(
                        TAG,
                        "mBroadcastReceiver1: STATE TURNING ON"
                    )
                }
            }
            if (action == BluetoothAdapter.ACTION_SCAN_MODE_CHANGED) {
                val mode =
                    intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.ERROR)
                when (mode) {
                    BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE -> Log.e(
                        TAG,
                        "mBroadcastReceiver2: Discoverability Enabled."
                    )
                    BluetoothAdapter.SCAN_MODE_CONNECTABLE -> Log.e(
                        TAG,
                        "mBroadcastReceiver2: Discoverability Disabled. Able to receive connections."
                    )
                    BluetoothAdapter.SCAN_MODE_NONE -> Log.e(
                        TAG,
                        "mBroadcastReceiver2: Discoverability Disabled. Not able to receive connections."
                    )
                    BluetoothAdapter.STATE_CONNECTING -> Log.e(
                        TAG,
                        "mBroadcastReceiver2: Connecting...."
                    )
                    BluetoothAdapter.STATE_CONNECTED -> Log.e(
                        TAG,
                        "mBroadcastReceiver2: Connected."
                    )
                }
            }
            if (action == BluetoothDevice.ACTION_FOUND) {
                val device: BluetoothDevice? =
                    intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                if (device != null) {
                    Toast.makeText(
                        this@MainActivity,
                        "Found " + device.name,
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e(TAG, "onReceive: " + device.name + ": " + device.address)
                }
            }

            if (action == BluetoothDevice.ACTION_BOND_STATE_CHANGED) {
                val mDevice =
                    intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                //3 cases:
                //case1: bonded already
                if (mDevice!!.bondState == BluetoothDevice.BOND_BONDED) {
                    includedView.progressBar.visibility = View.GONE
                    Snackbar.make(
                        includedView.root,
                        "Paired with " + mDevice.name,
                        Snackbar.LENGTH_LONG
                    ).show()
                    includedView.btnGetData.isEnabled = true
                }
                //case2: creating a bond
                if (mDevice.bondState == BluetoothDevice.BOND_BONDING) {
                    Log.e(TAG, "BroadcastReceiver: BOND_BONDING.")
                    Snackbar.make(
                        includedView.root,
                        "Connecting to " + mDevice.name,
                        Snackbar.LENGTH_LONG
                    ).show()
                    includedView.progressBar.visibility = View.VISIBLE
                }
                //case3: breaking a bond
                if (mDevice.bondState == BluetoothDevice.BOND_NONE) {
                    Log.e(TAG, "BroadcastReceiver: BOND_NONE.")
                    includedView.progressBar.visibility = View.GONE
                    Snackbar.make(
                        includedView.root,
                        "Failed! Device not in paring mode",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }
    }


    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModal = ViewModelProvider(this)[MainActivityViewModel::class.java]
//        viewModal = ViewModelProvider(
//            this,
//            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
//        )[MainActivityViewModel::class.java]
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        includedView = binding.contentContainer
        if (Build.VERSION.SDK_INT >= 33) {
            requestMultiplePermissions.launch(Permission_S_UP)
        } else {
            requestMultiplePermissions.launch(Permission_S_DOWN)
        }
        val filter = IntentFilter()
        filter.addAction(BluetoothDevice.ACTION_FOUND)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        filter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED)
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
        filter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED)
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
        registerReceiver(receiver, IntentFilter(filter))

        includedView.btnBTON.setOnClickListener {
            locationDialog()
            val permission: Boolean = if (Build.VERSION.SDK_INT >= 33) {
                hasPermissions(this, permissions = Permission_S_UP)
            } else {
                hasPermissions(this, permissions = Permission_S_DOWN)
            }
            if (permission) {
                Log.e("bt", "has permission")
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                requestBluetooth.launch(enableBtIntent)
            } else {
                Log.e("bt", "no permission")
                showDialog()
            }
        }
        includedView.btnScanBT.setOnClickListener {


            // To skip filters based on names and supported feature flags (UUIDs),
            // omit calls to setNamePattern() and addServiceUuid()
            // respectively, as shown in the following  Bluetooth example.
            val deviceFilter: BluetoothDeviceFilter = BluetoothDeviceFilter.Builder()
                .build()

            // The argument provided in setSingleDevice() determines whether a single
            // device name or a list of them appears.
            val pairingRequest: AssociationRequest = AssociationRequest.Builder()
                .addDeviceFilter(deviceFilter)
                .setSingleDevice(false)
                .build()

            // When the app tries to pair with a Bluetooth device, show the
            // corresponding dialog box to the user.
            deviceManager.associate(
                pairingRequest,
                object : CompanionDeviceManager.Callback() {

                    override fun onDeviceFound(chooserLauncher: IntentSender) {
                        startIntentSenderForResult(
                            chooserLauncher,
                            SELECT_DEVICE_REQUEST_CODE, null, 0, 0, 0
                        )
                        Log.e("BTState", "success")
                    }

                    override fun onFailure(error: CharSequence?) {
                        // Handle the failure.
                    }
                }, null
            )

        }
        includedView.btnGetData.setOnClickListener {
            val bluetoothManager: BluetoothManager = getSystemService(BluetoothManager::class.java)
            val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
            if (bluetoothAdapter == null) {
                // Device doesn't support Bluetooth
            }
            val sharedPreference =  getSharedPreferences("BluetoothSP",Context.MODE_PRIVATE)
            var editor = sharedPreference.edit()
            val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
            pairedDevices?.forEach { device ->
                val deviceName = device.name
                val deviceHardwareAddress = device.address // MAC address
                Log.e("BTLOG", deviceName + "-" + deviceHardwareAddress)
                    obj.put("Device Name",deviceName)
                    obj.put("Device Address",deviceHardwareAddress)
            }
            includedView.jsonView.setJson(obj)
            editor.putString("data",obj.toString())
            editor.apply()
            var temp: String? = sharedPreference.getString("data","N.A")
            Snackbar.make(
                includedView.root,
                "Data Stored in DB",
                Snackbar.LENGTH_LONG
            ).show()
            includedView.btnSendData.isEnabled=true
            Log.e("saved data",temp.toString())
        }
        includedView.btnSendData.setOnClickListener {
            val db = FirebaseFirestore.getInstance()
            val user: MutableMap<String, Any> = HashMap()
            user["data"] = obj.toString()
            db.collection("btData")
                .add(user)
                .addOnSuccessListener { documentReference ->
                    Log.e(
                        TAG,
                        "DocumentSnapshot added with ID: " + documentReference.id
                    )
                    includedView.jsonView.visibility=View.GONE
                    Snackbar.make(
                        includedView.root,
                        "Data Uploaded to server successfully!",
                        Snackbar.LENGTH_LONG
                    ).show()
                    this@MainActivity.getSharedPreferences("BluetoothSP", 0).edit().clear().apply();
                    val reqCode = 1
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    showNotification(
                        this,
                        "Data Received On Sever",
                        "Your data has been saved to firebase DB",
                        intent,
                        reqCode
                    )
                }
                .addOnFailureListener { e -> Log.w(TAG, "Error adding document", e) }
        }
    }


    private fun locationDialog() {
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY))
        builder.setAlwaysShow(true)
        mLocationSettingsRequest = builder.build()

        mSettingsClient = LocationServices.getSettingsClient(this@MainActivity)

        mSettingsClient?.checkLocationSettings(mLocationSettingsRequest!!)?.addOnSuccessListener {
            //Success Perform Task Here
        }?.addOnFailureListener { e ->
            val statusCode = (e as ApiException).statusCode
            when (statusCode) {
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                    val rae = e as ResolvableApiException
                    rae.startResolutionForResult(this@MainActivity, REQUEST_CHECK_SETTINGS)
                } catch (sie: SendIntentException) {
                    Log.e("GPS", "Unable to execute request.")
                }
                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> Log.e(
                    "GPS",
                    "Location settings are inadequate, and cannot be fixed here. Fix in Settings."
                )
            }
        }?.addOnCanceledListener { Log.e("GPS", "checkLocationSettings -> onCanceled") }
    }

    @SuppressLint("MissingPermission")
    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            when (resultCode) {
                RESULT_OK -> {}
                RESULT_CANCELED -> {
                    Log.e("GPS", "User denied to access location")
                    openGpsEnableSetting()
                }
            }
        } else if (requestCode == REQUEST_ENABLE_GPS) {
            val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
            val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            if (!isGpsEnabled) {
                openGpsEnableSetting()
            } else {
                Log.e("dsfdsfdsfdsf", "gps enabled!")
            }
        }
        when (requestCode) {
            SELECT_DEVICE_REQUEST_CODE -> when (resultCode) {
                Activity.RESULT_OK -> {
                    // The user chose to pair the app with a Bluetooth device.
                    val deviceToPair: BluetoothDevice? =
                        data?.getParcelableExtra(CompanionDeviceManager.EXTRA_DEVICE)
                    deviceToPair?.let { device ->
                        device.createBond()
                        Log.e("BONDIONG", "Clicked!")
                    }
                }
            }
            else -> {
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    private fun openGpsEnableSetting() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivityForResult(intent, REQUEST_ENABLE_GPS)
    }

    private var requestBluetooth =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                Log.e("PERMISSION", "GRANTED")
                includedView.btnScanBT.isEnabled = true
            } else {
                showDialog()
            }
        }
    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                Log.e("test006", "${it.key} = ${it.value}")
                //check all true and unblock ui
            }
        }

    fun hasPermissions(context: Context, vararg permissions: String): Boolean = permissions.all {
        ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun showDialog() {
        val builder = AlertDialog.Builder(this)
        //set title for alert dialog
        builder.setTitle("Permissions Not Granted")
        //set message for alert dialog
        builder.setMessage("Please manually allow permissions for app to run or reset app permissions")
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        //performing negative action
        builder.setNegativeButton("Exit App") { dialogInterface, which ->
            finishAndRemoveTask()
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
    }


    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this)
            .unregisterReceiver(receiver)
    }
    fun showNotification(
        context: Context,
        title: String?,
        message: String?,
        intent: Intent?,
        reqCode: Int
    ) {
        val pendingIntent =
            PendingIntent.getActivity(context, reqCode, intent, PendingIntent.FLAG_IMMUTABLE)

        val CHANNEL_ID = "fleet_ble" // The id of the channel.
        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.stat_sys_data_bluetooth)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent)
        val notificationManager =
            context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val name: CharSequence = "Fleet BLE" // The user-visible name of the channel.
        val importance = NotificationManager.IMPORTANCE_HIGH
        val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
        notificationManager.createNotificationChannel(mChannel)
        notificationManager.notify(
            reqCode,
            notificationBuilder.build()
        ) // 0 is the request code, it should be unique id
        Log.d("showNotification", "showNotification: $reqCode")
    }
}