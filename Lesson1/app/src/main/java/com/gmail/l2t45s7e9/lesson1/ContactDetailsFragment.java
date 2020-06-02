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
    View view;
    ContactService contactService;
    TextView name;
    TextView telephoneNumber;
    TextView telephoneNumber2;
    TextView email;
    TextView email2;
    TextView description;

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
        int id = this.getArguments().getInt("id",0);
        contactService.getContactDetails(callback,id);
        return view;
    }
    ContactDetails callback = new ContactDetails() {
        @Override
        public void getContactDetails(People result) {
            final People people = result;
            if(view!=null){
                view.post(new Runnable() {
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
	}
}