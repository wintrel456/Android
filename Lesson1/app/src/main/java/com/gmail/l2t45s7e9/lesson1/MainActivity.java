package com.gmail.l2t45s7e9.lesson1;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class MainActivity extends AppCompatActivity implements ContactService.Informator {
    private static final int REQUEST_CODE_READ_CONTACTS=1;
    private static boolean READ_CONTACTS_GRANTED =false;
    ContactService contactService;
    boolean bound;
    ServiceConnection serviceConnection;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        final Bundle firstLaunch = savedInstanceState;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this,ContactService.class);
        int hasReadContactPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        // если устройство до API 23, устанавливаем разрешение
        if(hasReadContactPermission == PackageManager.PERMISSION_GRANTED){
            READ_CONTACTS_GRANTED = true;
        }
        else{
            // вызываем диалоговое окно для установки разрешений
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CODE_READ_CONTACTS);
        }
        // если разрешение установлено, загружаем контакты
        if (READ_CONTACTS_GRANTED){
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
