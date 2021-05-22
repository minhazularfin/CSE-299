package com.nsu.shipbid.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nsu.shipbid.Activities.LoginActivity;
import com.nsu.shipbid.Domains.Profile;
import com.nsu.shipbid.R;

import java.util.Objects;


public class ProfileFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView fullName;
    private TextView email;
    private TextView phoneNumber;
    private TextView profileType;
    private TextView carNumber;
    private Button logOut, edit_button;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        fullName = view.findViewById(R.id.name_show);
        phoneNumber = view.findViewById(R.id.phone_show);
        email = view.findViewById(R.id.email_show);
        profileType = view.findViewById(R.id.type_show);
        carNumber = view.findViewById(R.id.car_show);
        logOut = view.findViewById(R.id.log_out);
        bindListeners();
        return view;
    }

    private void bindListeners() {
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        assert firebaseUser != null;
        final String mail = firebaseUser.getEmail();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("/users/");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Profile profile = ds.getValue(Profile.class);
                    if (Objects.requireNonNull(profile).getEmail().equals(mail)) {
                        fullName.setText(profile.getName());
                        phoneNumber.setText(profile.getPhoneNumber());
                        email.setText(profile.getEmail());
                        if(profile.getCarNumber() != null){
                            carNumber.setText(profile.getCarNumber());
                        }
                        profileType.setText(profile.getProfileType());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

}
