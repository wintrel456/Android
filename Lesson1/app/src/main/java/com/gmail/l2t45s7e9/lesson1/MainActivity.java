package com.gmail.l2t45s7e9.lesson1;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class MainActivity extends AppCompatActivity implements ContactService.Informator {
    ContactService contactService;
    boolean bound;
    ServiceConnection serviceConnection;
    Intent intent;
    Bundle firstLaunch;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        firstLaunch = savedInstanceState;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intent = new Intent(this,ContactService.class);
        int hasReadContactPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        if(hasReadContactPermission == PackageManager.PERMISSION_GRANTED){
            serviceConnection();
        }
        else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 1);
        }

    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                serviceConnection();
            }
        }
        else{
            Toast.makeText(this, R.string.toastContactPermission, Toast.LENGTH_LONG).show();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        bound = false;
        unbindService(serviceConnection);
    }
    @Override
    public ContactService getService() {
        if (bound) return contactService;
        else return null;
    }
    public void serviceConnection(){
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                contactService = ((ContactService.LocalBinder)service).getService();
                bound = true;
                if (firstLaunch == null){
                    ContactListFragment contactListFragment = new ContactListFragment();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(R.id.contactContainer, contactListFragment).commit();
                }
            }
            @Override
            public void onServiceDisconnected(ComponentName name) {
                bound = false;
            }
        };
        bindService(intent,serviceConnection,BIND_AUTO_CREATE);
    }
}
