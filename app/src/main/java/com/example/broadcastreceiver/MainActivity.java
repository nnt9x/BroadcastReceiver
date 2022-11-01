package com.example.broadcastreceiver;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    private IntentFilter intentFilter;

    private BroadcastReceiver broadcastReceiver;

    private AlertDialog alertDialog = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Khai báo 1 lần => intent filter
        intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);

        // Khai báo trước receiver
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final String action = intent.getAction();

                if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                    final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                    switch (state) {
                        case BluetoothAdapter.STATE_OFF:
                            // Hiển thị Dialog
                            if(alertDialog == null){
                                // Tao alert dialog
                                alertDialog = new AlertDialog.Builder(MainActivity.this)
                                        .setTitle("Thông báo")
                                        .setMessage("Bạn có muốn bật bluetooth?")
                                        .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                                                if (mBluetoothAdapter != null) {
                                                    mBluetoothAdapter.enable();
                                                }
                                            }
                                        })
                                        .create();
                            }
                            alertDialog.show();
                            break;
                        case BluetoothAdapter.STATE_ON:
                            if(alertDialog  != null && alertDialog.isShowing()){
                                alertDialog.dismiss();
                            }
                          //  Toast.makeText(MainActivity.this, "Bluetooth ON", Toast.LENGTH_SHORT).show();
                            break;
                    }

                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }
}