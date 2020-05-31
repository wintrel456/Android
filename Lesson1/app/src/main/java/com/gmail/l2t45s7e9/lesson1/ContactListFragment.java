package com.gmail.l2t45s7e9.lesson1;

import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.ListFragment;

public class ContactListFragment extends ListFragment {

    interface ContactList{
        void getContactList(People[] people);
    }
    ContactService contactService;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        contactService = ((ContactService.Informator)context).getService();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = super.onCreateView(inflater, container, savedInstanceState);
        getActivity().setTitle(R.string.ContactListTitle);
        ContactList callback = new ContactList() {
            @Override
            public void getContactList(People[] result) {
                final People[] people = result;
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        ArrayAdapter<People> contactArrayAdapter = new ArrayAdapter<People>(getActivity(), 0, people) {
                            @Override
                            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                                View listItem = convertView;
                                if (listItem == null)
                                    listItem = getLayoutInflater().inflate(R.layout.fragment_contact_list, null, false);
                                final People curMember = people[position];
                                final TextView name = listItem.findViewById(R.id.textViewName);
                                final TextView telephoneNumber = listItem.findViewById(R.id.textViewTelephoneNumber);
                                name.setText(curMember.getName());
                                telephoneNumber.setText(curMember.getTelephoneNumber());
                                return listItem;
                            }
                        };
                        setListAdapter(contactArrayAdapter);
                    }
                });


            }
        };
        contactService.getContactList(callback);
        return view;
    }
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        showDetails(position);
    }
    private void showDetails(int position) {
        ContactDetailsFragment contactDetailsFragment = ContactDetailsFragment.newInstance(position);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.contactContainer, contactDetailsFragment).addToBackStack(null).commit();
    }


}
