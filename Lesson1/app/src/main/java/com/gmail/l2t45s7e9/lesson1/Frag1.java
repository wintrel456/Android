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

public class Frag1 extends ListFragment {
    private static People first = new People("Иван", "+7919 151 6321", "999", "1@gmail.com", "3@gmail.com", "Описание");
    private static People second = new People("Дима", "+7912 112 4577", "888", "2@gmail.com", "4@gmail.com", "Описание");
    static People[] people = {first, second};

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Список контактов");
        ArrayAdapter<People> contactArrayAdapter = new ArrayAdapter<People>(getActivity(), 0, people) {
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View listItem = convertView;
                if (listItem == null)
                    listItem = getLayoutInflater().inflate(R.layout.fragment1, null, false);
                People curMember = people[position];
                TextView name = (TextView) listItem.findViewById(R.id.textView6);
                name.setText(curMember.getName());
                TextView telephoneNumber = (TextView) listItem.findViewById(R.id.textView7);
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
        Frag2 frag2 = Frag2.newInstance(position);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.c1, frag2).addToBackStack(null).commit();
    }

}
