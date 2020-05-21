package com.gmail.l2t45s7e9.lesson1;

import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
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
    private static People first = new People("Иван", "+7919 151 6321", "999", "1@gmail.com", "3@gmail.com", "Описание");
    private static People second = new People("Дима", "+7912 112 4577", "888", "2@gmail.com", "4@gmail.com", "Описание");
    static People[] people = {first, second};

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle(R.string.ContactListTitle);
        ArrayAdapter<People> contactArrayAdapter = new ArrayAdapter<People>(getActivity(), 0, people) {
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View listItem = convertView;
                if (listItem == null)
                    listItem = getLayoutInflater().inflate(R.layout.fragment_contact_list, null, false);
                People curMember = people[position];
                TextView name = listItem.findViewById(R.id.textViewName);
                name.setText(curMember.getName());
                TextView telephoneNumber = (TextView) listItem.findViewById(R.id.textViewTelephoneNumber);
                telephoneNumber.setText(curMember.getTelephoneNumber());
                return listItem;
            }
        };
        setListAdapter(contactArrayAdapter);
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
