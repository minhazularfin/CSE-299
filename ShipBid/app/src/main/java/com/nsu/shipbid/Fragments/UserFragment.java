package com.nsu.shipbid.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nsu.shipbid.Domains.Ride;
import com.nsu.shipbid.R;

import java.util.ArrayList;
import java.util.List;

public class UserFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private List<Ride> rides = new ArrayList<>();
    private RecyclerView recyclerView;


    private String mParam1;
    private String mParam2;


    public UserFragment() {
        // Required empty public constructor
    }

    public static UserFragment newInstance(String param1, String param2) {
        UserFragment fragment = new UserFragment();
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
        View view = inflater.inflate(R.layout.fragment_rides, container, false);
        recyclerView = view.findViewById(R.id.rides_list);
        prepareListView();
        retrieveRides();
        return view;
    }

    private void retrieveRides() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("/rides/");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                rides.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Ride ride = ds.getValue(Ride.class);
                    rides.add(ride);
                }
                RidesListAdapter adapter = (RidesListAdapter) recyclerView.getAdapter();
                assert adapter != null;
                adapter.setRides(rides);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void prepareListView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new RidesListAdapter(rides));
    }

    private class RidesListAdapter extends RecyclerView.Adapter<RidesListItemViewHolder>{

        private List<Ride> rides;
        RidesListAdapter(List<Ride> rides){
            this.rides = rides;
        }

        public void setRides(List<Ride> rides){
            this.rides = rides;
        }

        @NonNull
        @Override
        public RidesListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.row_ride, parent, false);
            return new RidesListItemViewHolder(view);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull RidesListItemViewHolder holder, int position) {
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

    private class RidesListItemViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private TextView car;
        private TextView time;
        private TextView leavingFrom;
        RidesListItemViewHolder(@NonNull View view){
            super(view);
            name = view.findViewById(R.id.driver_name);
            car = view.findViewById(R.id.ride_car);
            time = view.findViewById(R.id.ride_time);
            leavingFrom = view.findViewById(R.id.ride_location);
        }
    }
}
