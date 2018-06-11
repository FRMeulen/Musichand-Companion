//Package
package com.example.falco.musichandcompanion;

//Imports
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;


public class ConnectionActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    String side;    //Right hand or Left hand

    //Fields to return
    String instrument;  //Stores previously selected instrument
    BluetoothDevice leftDevice; //Stores left device if selecting right
    BluetoothDevice rightDevice;    //Stores right device if selecting left
    String leftConnectionName;  //Stores name of left connection
    String rightConnectionName; //Stores name of right connection

    //Buttons
    Button onOffButton; //Enable or disable bluetooth
    Button discoverButton;  //Discover unpaired devices nearby
    Button selectButton;   //Connect to selected device

    //Text fields
    TextView btSelectedText;    //Text showing "Selected: "
    TextView btSelected;    //Text showing selected device

    //Bluetooth fields
    BluetoothDevice mBTDevice;  //Bluetooth device object
    BluetoothAdapter mBluetoothAdapter; //Device bluetooth adapter

    //Receiver booleans
    boolean state_changed_receiver = false;
    boolean found_receiver = false;
    boolean bond_state_changed_receiver = false;

    //UUID
    private static final UUID MY_UUID_INSECURE = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");   //Default SerialPortService ID

    //Tag for logging
    private static final String TAG = "ConnectionActivity";    //Tag for logs

    //Device list
    public ArrayList<BluetoothDevice> mBTDevices = new ArrayList<>();   //List of discovered devices
    public ArrayList<BluetoothDevice> mIgnoredDevices = new ArrayList<>();  //List of unwanted devices
    public DeviceListAdapter mDeviceListAdapter;    //Adapter for listed devices
    ListView deviceList;    //Listview for discovered devices

    //BroadcastReceiver for ACTION_STATE_CHANGED.
    private final BroadcastReceiver mBroadcastReceiverBTState = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action.equals(mBluetoothAdapter.ACTION_STATE_CHANGED)) {    //If bluetooth adapter state changed
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, mBluetoothAdapter.ERROR);  //Get adapter state

                switch(state){
                    case BluetoothAdapter.STATE_OFF:    //If bluetooth is off
                        Log.d(TAG, "bluetoothAdapter: State Off");  //Log
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:    //If bluetooth is turning off
                        Log.d(TAG, "bluetoothAdapter: State Turning Off");  //Log
                        break;
                    case BluetoothAdapter.STATE_ON: //If bluetooth is on
                        Log.d(TAG, "bluetoothAdapter: State On");   //Log
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON: //If bluetooth is turning on
                        Log.d(TAG, "bluetoothAdapter: State Turning On");   //Log
                        break;
                }
            }
        }
    };

    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver mBroadcastReceiverDeviceFound = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action.equals(BluetoothDevice.ACTION_FOUND)) {  //If action found on device
                //Scan for new devices
                BluetoothDevice device = intent.getParcelableExtra (BluetoothDevice.EXTRA_DEVICE);

                //Ignore duplicates
                if(mIgnoredDevices.contains(device)){
                    //Device will be ignored fully
                }
                else {
                    if(!mBTDevices.contains(device)){
                        if(device.getName() != null){
                            if(device.getName().equals("Music Hand Right")){
                                mBTDevices.add(device); //Add to connectable devices
                                mIgnoredDevices.add(device);    //Ignore when found again
                                Log.d(TAG, "onReceive: Listed " + device.getName() + " at " + device.getAddress() + ".");   //Log
                            }
                            else if(device.getName().equals("Music Hand Left")){
                                mBTDevices.add(device); //Add to connectable devices
                                mIgnoredDevices.add(device);    //Ignore when found again
                                Log.d(TAG, "onReceive: Listed " + device.getName() + " at " + device.getAddress() + ".");   //Log
                            }
                            else{
                                mIgnoredDevices.add(device);    //Ignore device
                                Log.d(TAG, "Unwanted device found, ignoring...");   //Log
                            }
                        }
                    }
                }
                mDeviceListAdapter = new DeviceListAdapter(context, R.layout.device_adapter_view, mBTDevices);  //Create adapter for listed devices
                deviceList.setAdapter(mDeviceListAdapter);  //Add adapter to listView
            }
        }
    };

    // Create a BroadcastReceiver for ACTION_BOND_STATE_CHANGED.
    private final BroadcastReceiver mBroadcastReceiverBondState = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) { //If bond state changed
                BluetoothDevice mDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                //If already bonded
                if(mDevice.getBondState() == BluetoothDevice.BOND_BONDED){
                    Log.d(TAG, "bondStateReceiver: BOND_BONDED");   //Log
                    mBTDevice = mDevice;    //Connect
                }

                //If in the process of bonding
                if(mDevice.getBondState() == BluetoothDevice.BOND_BONDING){
                    Log.d(TAG, "bondStateReceiver: BOND_BONDING");  //Log
                }

                //If no bond exists
                if(mDevice.getBondState() == BluetoothDevice.BOND_NONE){
                    Log.d(TAG, "bondStateReceiver: BOND_NONE"); //Log
                }
            }
        }
    };

    //When app is ended
    @Override
    protected void onDestroy(){
        Log.d(TAG, "onDestroy: called.");
        super.onDestroy();
        if(state_changed_receiver){
            unregisterReceiver(mBroadcastReceiverBTState);
        }

        if(found_receiver){
            unregisterReceiver(mBroadcastReceiverDeviceFound);
        }

        if(bond_state_changed_receiver){
            unregisterReceiver(mBroadcastReceiverBondState);
        }
    }

    //When app is started
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            side = extras.getString("side");
            instrument = extras.getString("instrument");
            rightDevice = extras.getParcelable("rightDevice");
            leftDevice = extras.getParcelable("leftDevice");
            rightConnectionName = extras.getString("rightConnectionName");
            leftConnectionName = extras.getString("leftConnectionName");

            Log.d(TAG, "Right name: "+rightConnectionName);
            Log.d(TAG, "Left name: "+leftConnectionName);

            if(side.equals("right")){
                mIgnoredDevices.add(leftDevice);
            }
            if(side.equals("left")){
                mIgnoredDevices.add(rightDevice);
            }
        }

        //Adapters
        mBluetoothAdapter = mBluetoothAdapter.getDefaultAdapter();

        //Buttons
        onOffButton = findViewById(R.id.onOffButton);
        discoverButton = findViewById(R.id.listButton);
        selectButton = findViewById(R.id.selectButton);

        //Misc
        deviceList = findViewById(R.id.deviceList); //ListView containing ArrayList contents
        mBTDevices = new ArrayList<>(); //ArrayList containing found devices

        //Text Fields
        btSelectedText = findViewById(R.id.bt_selected_text);
        btSelected = findViewById(R.id.bt_selected);

        //Filter for bond state changed
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(mBroadcastReceiverBondState, filter);
        bond_state_changed_receiver = true;

        //Listeners
        deviceList.setOnItemClickListener(ConnectionActivity.this);

        if(mBluetoothAdapter.isEnabled()){
            discoverButton.setVisibility(View.VISIBLE);
        }

        //On / Off button
        onOffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: enabling/disabling bluetooth.");   //Log
                if(!mBluetoothAdapter.isEnabled()){ //If bluetooth adapter is disabled upon click
                    discoverButton.setVisibility(View.VISIBLE); //Show discover button
                }
                else {  //If bluetooth adapter is enabled upon click
                    discoverButton.setVisibility(View.INVISIBLE);   //Hide discover button
                }
                enableDisableBluetooth();   //Switch adapter state
            }
        });

        //Discover button
        discoverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Discovering devices...");  //Log
                mBTDevices.clear(); //Empty device list to avoid duplicates
                selectButton.setVisibility(View.VISIBLE);  //Show connect button
                btSelectedText.setVisibility(View.VISIBLE); //Show "Selected: "
                btSelected.setVisibility(View.VISIBLE); //Show selected device
                deviceList.setVisibility(View.VISIBLE); //Show found devices
                discoverDevices();  //Search for bluetooth devices
            }
        });

        //Connect button
        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mBTDevice != null){
                    returnConnection();
                }
                else{
                    Toast noneSelectedToast = Toast.makeText(getApplicationContext(), "No device selected!", Toast.LENGTH_LONG);
                    noneSelectedToast.show();
                }
            }
        });
    }

    public void returnConnection(){
        Intent returnIntent = new Intent(this, MainActivity.class);
        returnIntent.putExtra("instrument", instrument);
        returnIntent.putExtra("rightDevice", rightDevice);
        returnIntent.putExtra("rightConnectionName", rightConnectionName);
        returnIntent.putExtra("leftDevice", leftDevice);
        returnIntent.putExtra("leftConnectionName", leftConnectionName);
        startActivity(returnIntent);
    }

    //Discovery
    public void discoverDevices(){
        Log.d(TAG, "discoverButton: Looking for unpaired devices...");  //Log

        if(mBluetoothAdapter.isDiscovering()){  //If discovery is on
            Log.d(TAG, "discoverButton: Canceling discovery."); //Log
            mBluetoothAdapter.cancelDiscovery();    //Cancel discovery

            checkBTPermissions();   //Check bluetooth permissions in manifest - REQUIRED

            Log.d(TAG, "discoverButton: Enabling discovery.");  //Log
            mBluetoothAdapter.startDiscovery(); //Restart discovery
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);    //Filter for device found
            registerReceiver(mBroadcastReceiverDeviceFound, discoverDevicesIntent); //Register receiver
            found_receiver = true;
        }

        if(!mBluetoothAdapter.isDiscovering()){ //If not discovering
            checkBTPermissions();   //Check bluetooth permissions in manifest - REQUIRED

            Log.d(TAG, "discoverButton: Enabling discovery.");  //Log
            mBluetoothAdapter.startDiscovery(); //Start discovery
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);    //Filter for device found
            registerReceiver(mBroadcastReceiverDeviceFound, discoverDevicesIntent); //Register receiver
            found_receiver = true;
        }
    }

    //Check bluetooth permissions in manifest
    @SuppressLint("NewApi")
    private void checkBTPermissions() {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){   //If phone uses android Lollipop or up
            int permissionCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION"); //Check fine location permission
            permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");  //Check coarse location permission
            if (permissionCheck != 0) { //If they are granted
                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Any number
            }
        }else{
            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");    //Log
        }
    }

    public void enableDisableBluetooth(){
        if(mBluetoothAdapter == null){  //If phone has no bluetooth adapter
            Log.d(TAG,"onOffButton: No bluetooth adapter.");    //Bluetooth not usable
        }

        if(!mBluetoothAdapter.isEnabled()){ //If adapter is not enabled
            Log.d(TAG, "onOffButton: enabling bluetooth."); //Enabling bluetooth
            Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);  //Intent for bluetooth enable
            startActivity(enableBluetoothIntent);   //Start activity

            IntentFilter bluetoothIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED); //Filter for adapter state changed
            registerReceiver(mBroadcastReceiverBTState, bluetoothIntent);   //Register receiver
            state_changed_receiver = true;
        }

        if(mBluetoothAdapter.isEnabled()){  //If adapter is enabled
            Log.d(TAG, "onOffButton: disabling bluetooth.");    //Disabling bluetooth
            mBluetoothAdapter.disable();    //Disable adapter

            IntentFilter bluetoothIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED); //Filter for adapter state changed
            registerReceiver(mBroadcastReceiverBTState, bluetoothIntent);   //Register receiver
            state_changed_receiver = true;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mBluetoothAdapter.cancelDiscovery();    //Cancel discovery to save memory
        Log.d(TAG, "onItemClick: Item Clicked!");   //Log

        String deviceName = mBTDevices.get(position).getName(); //Get device name
        String deviceAddress = mBTDevices.get(position).getAddress();   //Get device address
        btSelected.setText(deviceName); //Show selected device name
        Log.d(TAG, "onItemClick: deviceName = " + deviceName);  //Log
        Log.d(TAG, "onItemClick: deviceAddress = " + deviceAddress);    //Log

        //Create the bond
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) { //If device uses android Jelly Bean MR2 or up
            Log.d(TAG, "Trying to pair with " + deviceName);    //Log
            mBTDevices.get(position).createBond();  //Create bond

            if(side.equals("right")){
                rightDevice = mBTDevices.get(position);
                rightConnectionName = rightDevice.getName();
            }
            else if(side.equals("left")){
                leftDevice = mBTDevices.get(position);
                leftConnectionName = leftDevice.getName();
            }

            mBTDevice = mBTDevices.get(position);   //Store connected device in variable
            selectButton.setVisibility(View.VISIBLE);   //Show select button
            //Starting a connection service starts an AcceptThread
        }
    }
}
