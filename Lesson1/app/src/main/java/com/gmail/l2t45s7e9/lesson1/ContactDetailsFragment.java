package com.gmail.l2t45s7e9.lesson1;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class ContactDetailsFragment extends Fragment {

    interface ContactDetails{
        void getContactDetails(People result);
    }
    ContactService contactService;
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
        final View view = inflater.inflate(R.layout.fragment_contact_details, container, false);
        ContactDetails callback = new ContactDetails() {
            @Override
            public void getContactDetails(People result) {
                final People people = result;
                final TextView name = view.findViewById(R.id.textViewName);
                final TextView telephoneNumber = view.findViewById(R.id.textViewTelephoneNumber);
                final TextView telephoneNumber2 = view.findViewById(R.id.textViewTelephoneNumber2);
                final TextView email = view.findViewById(R.id.textViewEmail);
                final TextView email2 = view.findViewById(R.id.textViewEmail2);
                final TextView description = view.findViewById(R.id.textViewDescription);

                view.post(new Runnable() {
                    @Override
                    public void run() {
                        getActivity().setTitle(getString(R.string.ContactDetailsTitle) +": "+ people.getName());
                        name.setText(people.getName());
                        telephoneNumber.setText(people.getTelephoneNumber());
                        telephoneNumber2.setText(people.getTelephoneNumber2());
                        email.setText(people.getEmail());
                        email2.setText(people.getEmail2());
                        description.setText(people.getDescription());
                    }
                });
            }
        };

        int id = this.getArguments().getInt("id",0);
        contactService.getContactDetails(callback,id);
        return view;
    }


}