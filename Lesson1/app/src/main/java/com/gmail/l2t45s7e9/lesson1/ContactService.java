package com.gmail.l2t45s7e9.lesson1;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class ContactService extends Service {

    interface Informator{
        ContactService getService();
    }
    private static People first = new People("Иван", "+7919 151 6321", "999", "1@gmail.com", "3@gmail.com", "Описание",new GregorianCalendar(1985, Calendar.JUNE,6));
    private static People second = new People("Дима", "+7912 112 4577", "888", "2@gmail.com", "4@gmail.com", "Описание",new GregorianCalendar(1985, Calendar.JUNE,7));
    static People[] people = {first, second};
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
                People result = people[id];
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
                People[] result = people;
                ContactListFragment.ContactList local = ref.get();
                if (local != null){
                    local.getContactList(result);
                }
            }
        }).start();
    }

}
