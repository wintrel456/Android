package com.gmail.l2t45s7e9.lesson1;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class MainActivity extends AppCompatActivity implements ContactService.Informator {
    ContactService contactService;
    boolean bound;
    ServiceConnection serviceConnection;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        final Bundle firstLaunch = savedInstanceState;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this,ContactService.class);
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
}
