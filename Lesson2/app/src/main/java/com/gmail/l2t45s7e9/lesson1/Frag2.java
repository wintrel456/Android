package com.gmail.l2t45s7e9.lesson1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class Frag2 extends Fragment {
    static Frag2 newInstance(int id) {
        Frag2 frag2 = new Frag2();
        Bundle args = new Bundle();
        args.putInt("id",id);
        frag2.setArguments(args);
        return frag2;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment2, container, false);

        int id = this.getArguments().getInt("id",0);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Детали контакта: "+Frag1.people[id].getName());
        TextView name = view.findViewById(R.id.textView6);
        name.setText(Frag1.people[id].getName());
        TextView telephoneNumber = view.findViewById(R.id.textView7);
        telephoneNumber.setText(Frag1.people[id].getTelephoneNumber());
        TextView telephoneNumber2 = view.findViewById(R.id.textView8);
        telephoneNumber2.setText(Frag1.people[id].getTelephoneNumber2());
        TextView email = view.findViewById(R.id.textView9);
        email.setText(Frag1.people[id].getEmail());
        TextView email2 = (TextView)view.findViewById(R.id.textView10);
        email2.setText(Frag1.people[id].getEmail2());
        TextView description = view.findViewById(R.id.textView11);
        description.setText(Frag1.people[id].getDescription());
        return view;
    }
}