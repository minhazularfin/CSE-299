package com.nsu.shipbid.Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nsu.shipbid.Activities.MapsActivity;
import com.nsu.shipbid.Domains.Profile;
import com.nsu.shipbid.Domains.Request;
import com.nsu.shipbid.R;

import java.util.Objects;


public class FindShipperFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private EditText destination;
    private EditText leavingFrom;
    private Spinner spinner;
    private Button currentLocation;
    private Button cont;
    private ProgressDialog progressDialog;
    private Button plus, minus;
    private TextView seatNo;

    public FindShipperFragment() {
        // Required empty public constructor
    }

    public static FindShipperFragment newInstance(String param1, String param2) {
        FindShipperFragment fragment = new FindShipperFragment();
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
        View view =  inflater.inflate(R.layout.fragment_find_shipper, container, false);
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
        destination = view.findViewById(R.id.rider_destination);
        leavingFrom = view.findViewById(R.id.rider_pickup);
        spinner = view.findViewById(R.id.time_select2);
        cont = view.findViewById(R.id.rider_find_ride);
        currentLocation = view.findViewById(R.id.rider_use_current_location);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Posting request...");
        plus = view.findViewById(R.id.plus);
        minus = view.findViewById(R.id.minus);
        seatNo = view.findViewById(R.id.seats);
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

        plus.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                int x = Integer.parseInt(seatNo.getText().toString());
                x++;
                seatNo.setText(Integer.toString(x));
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                int x = Integer.parseInt(seatNo.getText().toString());
                if(x>0) x--;
                seatNo.setText(Integer.toString(x));
            }
        });

        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createRequest();
            }
        });
    }

    private void createRequest(){
        progressDialog.show();
        progressDialog.setCancelable(false);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;
        final String mail = firebaseUser.getEmail();
        DatabaseReference databaseReferences = FirebaseDatabase.getInstance().getReference("/users/");
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("/requests/");
        databaseReferences.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Profile profile = ds.getValue(Profile.class);
                    assert profile != null;
                    assert mail != null;
                    if(mail.equals(profile.getEmail())){
                        Request request = new Request(profile.getName(),getSpinnerTime(),
                                leavingFrom.getText().toString(),destination.getText().toString(),mail,Integer.parseInt(seatNo.getText().toString()));
                        String code = Integer.toString(request.hashCode());
                        databaseReference.child(code).child("name").setValue(request.getName());
                        databaseReference.child(code).child("destination").setValue(request.getDestination());
                        databaseReference.child(code).child("leavingFrom").setValue(request.getLeavingFrom());
                        databaseReference.child(code).child("time").setValue(request.getTime());
                        databaseReference.child(code).child("email").setValue(request.getEmail());
                        databaseReference.child(code).child("seats").setValue(request.getSeats());
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(),"Request has been posted for ride!\nPlease check inbox",Toast.LENGTH_LONG).show();
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
