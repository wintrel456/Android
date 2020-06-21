package com.gmail.l2t45s7e9.lesson1;

import android.Manifest;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;

public class ContactService extends Service {

    interface Informator{
        ContactService getService();
    }
    ArrayList<People> contacts = new ArrayList<People>();


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate() {
        super.onCreate();
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Data.CONTENT_URI,
                new String[] {ContactsContract.Contacts.HAS_PHONE_NUMBER,
                        ContactsContract.Data.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Email.ADDRESS,
                        ContactsContract.CommonDataKinds.Phone.NUMBER},
                null,
                null,
                ContactsContract.Data.DISPLAY_NAME);

        if(cursor!=null){

            while (cursor.moveToNext()){
                String hasNumber = cursor.getString(
                        cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                String email = cursor.getString(
                        cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
                String name = cursor.getString(
                        cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String number = cursor.getString(
                        cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                if(hasNumber.equalsIgnoreCase("1") && !name.equalsIgnoreCase(number)){
                        People people = new People(name,number,number,email ,email,getString(R.string.description) , new GregorianCalendar(1985, Calendar.JUNE,18) );
                        contacts.add(people);
                    }
                }
            }
            cursor.close();
    }

    IBinder binder = new LocalBinder();

    class LocalBinder extends Binder{
        ContactService getService(){
            return ContactService.this;
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }


    public void getContactDetails(ContactDetailsFragment.ContactDetails callback, int idContact){
        final WeakReference<ContactDetailsFragment.ContactDetails> ref = new WeakReference<>(callback);
        final int id = idContact;
        new Thread(new Runnable() {
            @Override
            public void run() {
                People result = contacts.get(id);
                ContactDetailsFragment.ContactDetails local = ref.get();
                if (local != null){
                    local.getContactDetails(result);
                }
            }
        }).start();

    }

    public void getContactList(ContactListFragment.ContactList callback){
        final WeakReference<ContactListFragment.ContactList> ref = new WeakReference<>(callback);
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<People> result = contacts;
                ContactListFragment.ContactList local = ref.get();
                if (local != null){
                    local.getContactList(result);
                }
            }
        }).start();
    }

}
