package com.gmail.l2t45s7e9.lesson1;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
    private String id;

    static ContactDetailsFragment newInstance(int id) {
        ContactDetailsFragment contactDetailsFragment = new ContactDetailsFragment();
        Bundle args = new Bundle();
        args.putInt("id", id);
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
        assert this.getArguments() != null;
        id = this.getArguments().getString("id","0");
        contactService.getContactDetails(callback,id);
        nSwitch = view.findViewById(R.id.notificationSwitch);
        boolean alarmUp = (PendingIntent.getBroadcast(getContext(), Integer.parseInt(id), new Intent("com.gmail.l2t45s7e9.lesson1"), PendingIntent.FLAG_NO_CREATE) != null);
        nSwitch.setChecked(alarmUp);
        switcher();
        return view;
    }
    private ContactDetails callback = new ContactDetails() {
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
							birthDate.setText(R.string.ContactBirthDate);
                            birthDate.append(" " + people.getBirthDate());
						}
                    }
                });
            }

        }
    };

    private void switcher(){
        nSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Intent intent = new Intent("com.gmail.l2t45s7e9.lesson1");
                AlarmManager alarmManager = (AlarmManager) Objects.requireNonNull(getContext()).getSystemService(Context.ALARM_SERVICE);
                intent.putExtra("birthDateName", people.getName());
                PendingIntent alarmIntent = PendingIntent.getBroadcast(getContext(), Integer.parseInt(id),intent,0);
                if(isChecked){
                    GregorianCalendar calendar = (GregorianCalendar) GregorianCalendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
                    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
                    Calendar calendarFromString = Calendar.getInstance();
                    try {
                        calendarFromString.setTime(Objects.requireNonNull(sdf.parse(people.getBirthDate())));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    calendar.set(Calendar.DATE,calendarFromString.get(Calendar.DATE) );
                    calendar.set(Calendar.MONTH,calendarFromString.get(Calendar.MONTH));
                    calendar.set(Calendar.YEAR,calendarFromString.get(Calendar.YEAR));
                    if (System.currentTimeMillis() > calendar.getTimeInMillis()){
                        if((!calendar.isLeapYear(calendar.get(Calendar.YEAR)+1)) && calendar.get(Calendar.MONTH)==Calendar.FEBRUARY && calendar.get(Calendar.DATE)==29){
                            calendar.roll(Calendar.YEAR,1);
                            calendar.roll(Calendar.DATE,-1);
                        }
                        else{
                            calendar.add(Calendar.YEAR,1);
                        }

                    }
                    else if(!calendar.isLeapYear(calendar.get(Calendar.YEAR))){
                        calendar.set(Calendar.DATE,calendarFromString.get(Calendar.DATE)-1);
                    }
                    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),alarmIntent);
                    Toast.makeText(Objects.requireNonNull(getContext()).getApplicationContext(),
                            R.string.on, Toast.LENGTH_SHORT).show();
                }
                else{
                    alarmManager.cancel(alarmIntent);
                    alarmIntent.cancel();
                    Toast.makeText(Objects.requireNonNull(getContext()).getApplicationContext(),
                            R.string.off, Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

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