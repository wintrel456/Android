package com.gmail.l2t45s7e9.lesson1;

import android.Manifest;
import android.annotation.SuppressLint;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class ContactService extends Service {

    interface Informator{
        ContactService getService();
    }

    @SuppressLint("Recycle")
    public ArrayList<People> onLoadContacts() {
        ArrayList<People> contacts = new ArrayList<People>();
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = null;
        try {
            cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
                    null,
                    null,
                    null,
                    ContactsContract.Contacts.DISPLAY_NAME_PRIMARY);

            if (cursor != null) {

                while (cursor.moveToNext()) {
                    String[] number = new String[2];
                    String id = cursor.getString(
                            cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cursor.getString(
                            cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    String hasnumber = cursor.getString(
                            cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                    if(hasnumber.equalsIgnoreCase("1")){
                        number = getContactNumbers(id);
                    }

                    People people = new People(name, number[0], null, null, null, getString(R.string.description), null);
                    contacts.add(people);
                }
            }
        } finally{
            if(cursor!=null){
                cursor.close();
            }
        }
        return contacts;
    }
    public People onLoadDetails(String id){
        People people = null;
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = null;
        try {
            cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
                    null,
                    ContactsContract.Contacts._ID+"="+id,
                    null,
                    ContactsContract.Contacts.DISPLAY_NAME_PRIMARY);

            if (cursor != null) {
                    cursor.moveToNext();
                    String[] number = new String[2];
                    String cursorId = cursor.getString(
                            cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cursor.getString(
                            cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    String hasnumber = cursor.getString(
                            cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                    if(hasnumber.equalsIgnoreCase("1")){
                        number = getContactNumbers(cursorId);
                    }
                    String[] email = getContactEmails(cursorId);
                    String birthday = cursor.getString(
                            cursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE));
                    people = new People(name, number[0], number[1], email[0], email[1], getString(R.string.description), birthday);

            }
        } finally{
            if(cursor!=null){
                cursor.close();
            }
        }
        return people;
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


    public void getContactDetails(ContactDetailsFragment.ContactDetails callback, final String id){
        final WeakReference<ContactDetailsFragment.ContactDetails> ref = new WeakReference<>(callback);
        new Thread(new Runnable() {
            @Override
            public void run() {
                People result = onLoadDetails(id);
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
                ArrayList<People> result = onLoadContacts();
                ContactListFragment.ContactList local = ref.get();
                if (local != null){
                    local.getContactList(result);
                }
            }
        }).start();
    }
    public String[] getContactNumbers(String id) {
        int countNumbers = 0;
        String[] numbers = new String[2];
        ContentResolver contentResolver = getContentResolver();
        Cursor cursorNumber = null;
        try {
            cursorNumber = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                    null,
                    null);
            if(cursorNumber!=null){
                while (cursorNumber.moveToNext()) {
                    if (countNumbers == 0 || countNumbers == 1){
                        numbers[countNumbers] = cursorNumber.getString(
                                cursorNumber.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                    }
                    countNumbers++;
                }
            }


        }finally {
            if (cursorNumber != null){
                cursorNumber.close();
            }
        }
        return numbers;
    }
    public String[] getContactEmails(String id) {
        int countEmails = 0;
        String[] emails = new String[2];
        ContentResolver contentResolver = getContentResolver();
        Cursor cursorEmail = null;
        try {
            cursorEmail = contentResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + id,
                    null,
                    null);
            if(cursorEmail!=null){
                while (cursorEmail.moveToNext()) {
                    if (countEmails == 0 || countEmails == 1){
                        emails[countEmails] = cursorEmail.getString(
                                cursorEmail.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA));

                    }
                    countEmails++;
                }
            }


        }finally {
            if (cursorEmail != null){
                cursorEmail.close();
            }
        }
        return emails;
    }

}
