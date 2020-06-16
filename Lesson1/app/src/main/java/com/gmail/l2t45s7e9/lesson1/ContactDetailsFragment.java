package com.gmail.l2t45s7e9.lesson1;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

public class ContactDetailsFragment extends Fragment {

	interface ContactDetails{
        void getContactDetails(People result);
    }
    private View view;
    private ContactService contactService;
    private TextView name;
    private TextView telephoneNumber;
    private TextView telephoneNumber2;
    private TextView email;
    private TextView email2;
    private TextView description;
    private TextView birthDate;
    private Switch nSwitch;
    private People people;
    int id;
    static ContactDetailsFragment newInstance(int id) {
        ContactDetailsFragment contactDetailsFragment = new ContactDetailsFragment();
        Bundle args = new Bundle();
        args.putInt("id",id);
        contactDetailsFragment.setArguments(args);
        return contactDetailsFragment;
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        contactService = ((ContactService.Informator)context).getService();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_contact_details, container, false);
        name = view.findViewById(R.id.textViewName);
        telephoneNumber = view.findViewById(R.id.textViewTelephoneNumber);
        telephoneNumber2 = view.findViewById(R.id.textViewTelephoneNumber2);
        email = view.findViewById(R.id.textViewEmail);
        email2 = view.findViewById(R.id.textViewEmail2);
        description = view.findViewById(R.id.textViewDescription);
        birthDate = view.findViewById(R.id.textViewBirthDate);
        id = this.getArguments().getInt("id",0);
        contactService.getContactDetails(callback,id);
        nSwitch = view.findViewById(R.id.notificationSwitch);

        boolean alarmUp = (PendingIntent.getBroadcast(getContext(), 0, new Intent(String.valueOf(getContext())), PendingIntent.FLAG_NO_CREATE) != null);
        if(alarmUp){
            nSwitch.setChecked(false);
            Toast.makeText(getContext().getApplicationContext(),
                    "Выключено", Toast.LENGTH_SHORT).show();
        }
        else {
            nSwitch.setChecked(true);
            Toast.makeText(getContext().getApplicationContext(),
                    "Включено", Toast.LENGTH_SHORT).show();
        }
        nSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Intent intent = new Intent(getContext(), NotificationReceiver.class);
                AlarmManager alarmManager = (AlarmManager)getContext().getSystemService(Context.ALARM_SERVICE);
                intent.putExtra("birthDateName", people.getName());
                PendingIntent alarmIntent = PendingIntent.getBroadcast(getContext(),0,intent,0);
                if(isChecked){
                    Calendar calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
                    calendar.set(Calendar.DATE,people.getBirthDate().get(Calendar.DATE));
                    System.out.println(people.getBirthDate().get(Calendar.DATE));
                    calendar.set(Calendar.MONTH,people.getBirthDate().get(Calendar.MONTH));
                    if (System.currentTimeMillis() > calendar.getTimeInMillis()){

                        calendar.add(Calendar.YEAR,1);
                    }

                    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),alarmIntent);
                }
                else{
                    alarmManager.cancel(alarmIntent);
                }

            }
        });

        return view;
    }
    ContactDetails callback = new ContactDetails() {
        @Override
        public void getContactDetails(People result) {
            people = result;
            if(view!=null){
                view.post(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {
                        if(getActivity()!=null){
                            getActivity().setTitle(getString(R.string.ContactDetailsTitle) +": "+ people.getName());
                        }
						if(name!=null){
							name.setText(people.getName());
							telephoneNumber.setText(people.getTelephoneNumber());
							telephoneNumber2.setText(people.getTelephoneNumber2());
							email.setText(people.getEmail());
							email2.setText(people.getEmail2());
							description.setText(people.getDescription());
                            birthDate.setText("День рождения: "+(people.getBirthDate()).get(Calendar.DATE)+" "+(people.getBirthDate()).getDisplayName(Calendar.MONTH,Calendar.LONG, new Locale("Ru")));
						}
                        
                    }
                });
            }

        }
    };


    @Override
	public void onDestroyView(){
		super.onDestroyView();
		name=null;
		telephoneNumber=null;
		telephoneNumber2=null;
		email=null;
		email2=null;
		description=null;
		view=null;
		birthDate = null;
		nSwitch=null;
	}
}