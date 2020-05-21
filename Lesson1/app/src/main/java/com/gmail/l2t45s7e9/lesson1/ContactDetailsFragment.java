package com.gmail.l2t45s7e9.lesson1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class ContactDetailsFragment extends Fragment {
    static ContactDetailsFragment newInstance(int id) {
        ContactDetailsFragment contactDetailsFragment = new ContactDetailsFragment();
        Bundle args = new Bundle();
        args.putInt("id",id);
        contactDetailsFragment.setArguments(args);
        return contactDetailsFragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_details, container, false);

        int id = this.getArguments().getInt("id",0);
        getActivity().setTitle(getString(R.string.ContactDetailsTitle) +": "+ ContactListFragment.people[id].getName());
        TextView name = view.findViewById(R.id.textViewName);
        name.setText(ContactListFragment.people[id].getName());
        TextView telephoneNumber = view.findViewById(R.id.textViewTelephoneNumber);
        telephoneNumber.setText(ContactListFragment.people[id].getTelephoneNumber());
        TextView telephoneNumber2 = view.findViewById(R.id.textViewTelephoneNumber2);
        telephoneNumber2.setText(ContactListFragment.people[id].getTelephoneNumber2());
        TextView email = view.findViewById(R.id.textViewEmail);
        email.setText(ContactListFragment.people[id].getEmail());
        TextView email2 = view.findViewById(R.id.textViewEmail2);
        email2.setText(ContactListFragment.people[id].getEmail2());
        TextView description = view.findViewById(R.id.textViewDescription);
        description.setText(ContactListFragment.people[id].getDescription());
        return view;
    }
}