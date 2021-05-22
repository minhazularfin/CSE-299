package com.nsu.shipbid.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nsu.shipbid.Domains.Request;
import com.nsu.shipbid.Domains.Ride;
import com.nsu.shipbid.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class UserInboxFragment extends Fragment {

    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private List<Ride> matchedRides = new ArrayList<>();

    public UserInboxFragment() {
        // Required empty public constructor
    }

    public static UserInboxFragment newInstance(String param1, String param2) {
        UserInboxFragment fragment = new UserInboxFragment();
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
        View view = inflater.inflate(R.layout.fragment_user_inbox, container, false);
        recyclerView = view.findViewById(R.id.matched_drivers);
        prepareListView();
        retrieveRides();
        return view;
    }

    private void retrieveRides() {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("/requests/");
        final DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("/rides/");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                matchedRides.clear();
                for (final DataSnapshot ds : dataSnapshot.getChildren()){
                    final Request request = ds.getValue(Request.class);
                    String mail = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
                    assert mail != null;
                    assert request != null;
                    if(mail.equals(request.getEmail())) {
                        databaseReference1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                List<Ride> x = new ArrayList<>();
                                for (Ride ride: matchedRides){
                                    if(request.match(ride)){
                                        x.add(ride);
                                    }
                                }
                                matchedRides.removeAll(x);
                                for (DataSnapshot ds2 : dataSnapshot.getChildren()) {
                                    Ride ride = ds2.getValue(Ride.class);
                                    assert ride != null;
                                    Log.e("<<<>>>", ride.toString() + " " + request.toString() + " " + request.match(ride));
                                    if (request.match(ride)) {
                                        matchedRides.add(ride);
                                    }
                                }
                                RideListAdapter adapter = (RideListAdapter) recyclerView.getAdapter();
                                assert adapter != null;
                                adapter.setRides(matchedRides);
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void prepareListView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new RideListAdapter(matchedRides));
    }

    private class RideListAdapter extends RecyclerView.Adapter<RideListItemViewHolder>{

        private List<Ride> rides;
        RideListAdapter(List<Ride> rides){
            this.rides = rides;
        }

        public void setRides(List<Ride> rides){
            this.rides = rides;
        }

        @NonNull
        @Override
        public RideListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.row_ride, parent, false);
            return new RideListItemViewHolder(view);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull RideListItemViewHolder holder, int position) {
            Ride ride = rides.get(position);
            holder.name.setText("Name: "+ride.getName());
            holder.car.setText("Car Number: "+ride.getCarNumber());
            holder.time.setText("Ride Time: "+ride.getTime());
            holder.leavingFrom.setText("Leaving From: "+ride.getLeavingFrom());
        }

        @Override
        public int getItemCount() {
            return rides.size();
        }
    }

    private class RideListItemViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private TextView car;
        private TextView time;
        private TextView leavingFrom;
        RideListItemViewHolder(@NonNull View view){
            super(view);
            name = view.findViewById(R.id.driver_name);
            car = view.findViewById(R.id.ride_car);
            time = view.findViewById(R.id.ride_time);
            leavingFrom = view.findViewById(R.id.ride_location);
        }
    }
}
