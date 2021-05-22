package com.nsu.shipbid.Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nsu.shipbid.Activities.MapsActivity;
import com.nsu.shipbid.Domains.Profile;
import com.nsu.shipbid.Domains.Ride;
import com.nsu.shipbid.R;

import java.util.Objects;

public class FindUserFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private EditText leavingFrom;
    private Button currentLocation;
    private Button cont;
    private Spinner spinner;
    private ProgressDialog progressDialog;

    private String TAG = FindUserFragment.class.getSimpleName();
    private String mParam2;

    public FindUserFragment() {
        // Required empty public constructor
    }

    public static FindUserFragment newInstance(String param1, String param2) {
        FindUserFragment fragment = new FindUserFragment();
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
        View view = inflater.inflate(R.layout.fragment_find_customer, container, false);
        bindWidgets(view);
        bindListeners();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity())
                .getSharedPreferences("shared_preferences", Context.MODE_PRIVATE);
        String location = sharedPreferences.getString("address", null);
        if(location != null){
            leavingFrom.setText(location);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity())
                .getSharedPreferences("shared_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("address",null);
        editor.apply();
    }

    private void bindWidgets(View view){
        leavingFrom = view.findViewById(R.id.driver_leaving);
        currentLocation = view.findViewById(R.id.driver_use_current_location);
        cont = view.findViewById(R.id.driver_find_rider);
        spinner = view.findViewById(R.id.time_select);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Creating ride");
    }

    private void bindListeners() {
        currentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkGps()){
                    startActivity(new Intent(getActivity(), MapsActivity.class));
                }
                else{
                    new AlertDialog.Builder(getActivity())
                            .setTitle("GPS disabled!")
                            .setMessage("Please turn on device location to use this feature")
                            .setPositiveButton("Ok",null)
                            .show();
                }
            }
        });

        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createRide();
            }
        });
    }

    private void createRide(){
        progressDialog.show();
        progressDialog.setCancelable(false);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;
        final String mail = firebaseUser.getEmail();
        DatabaseReference databaseReferences = FirebaseDatabase.getInstance().getReference("/users/");
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("/rides/");
        databaseReferences.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Profile profile = ds.getValue(Profile.class);
                    assert profile != null;
                    assert mail != null;
                    if(mail.equals(profile.getEmail())){
                        Ride ride = new Ride(profile.getName(),profile.getCarNumber(),
                                leavingFrom.getText().toString(), getSpinnerTime(),mail);
                        String code = Integer.toString(ride.hashCode());
                        databaseReference.child(code).child("name").setValue(ride.getName());
                        databaseReference.child(code).child("carNumber").setValue(ride.getCarNumber());
                        databaseReference.child(code).child("leavingFrom").setValue(ride.getLeavingFrom());
                        databaseReference.child(code).child("time").setValue(ride.getTime());
                        databaseReference.child(code).child("email").setValue(mail);
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(),"Ride Created!",Toast.LENGTH_LONG).show();
                        break;
                    }
                }
            }

            private String getSpinnerTime() {
                return spinner.getSelectedItem().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private boolean checkGps(){
        LocationManager locationManager = (LocationManager) Objects.requireNonNull(getActivity()).getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
}
