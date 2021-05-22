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

public class DriverInboxFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private List<Request> matchedRequests = new ArrayList<>();

    public DriverInboxFragment() {
        // Required empty public constructor
    }

    public static DriverInboxFragment newInstance(String param1, String param2) {
        DriverInboxFragment fragment = new DriverInboxFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_driver_inbox, container, false);
        recyclerView = view.findViewById(R.id.matched_riders);
        prepareListView();
        retrieveRides();
        return view;
    }

    private void retrieveRides() {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("/rides/");
        final DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("/requests/");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                matchedRequests.clear();
                for (final DataSnapshot ds : dataSnapshot.getChildren()){
                    final Ride ride = ds.getValue(Ride.class);
                    String mail = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
                    assert mail != null;
                    assert ride != null;
                    if(mail.equals(ride.getEmail())) {
                        databaseReference1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                List<Request> x = new ArrayList<>();
                                for (Request request: matchedRequests){
                                    if(request.match(ride)){
                                        x.add(request);
                                    }
                                }
                                matchedRequests.removeAll(x);
                                for (DataSnapshot ds2 : dataSnapshot.getChildren()) {
                                    Request request = ds2.getValue(Request.class);
                                    assert request != null;
                                    Log.e("<<<>>>", ride.toString() + " " + request.toString() + " " + request.match(ride));
                                    if (request.match(ride)) {
                                        matchedRequests.add(request);
                                    }
                                }
                                RideListAdapter adapter = (RideListAdapter) recyclerView.getAdapter();
                                assert adapter != null;
                                adapter.setRides(matchedRequests);
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
        recyclerView.setAdapter(new RideListAdapter(matchedRequests));
    }

    private class RideListAdapter extends RecyclerView.Adapter<RideListItemViewHolder>{

        private List<Request> requests;
        RideListAdapter(List<Request> requests){
            this.requests = requests;
        }

        public void setRides(List<Request> requests){
            this.requests = requests;
        }

        @NonNull
        @Override
        public RideListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.row_request, parent, false);
            return new RideListItemViewHolder(view);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull RideListItemViewHolder holder, int position) {
            Request request = requests.get(position);
            holder.name.setText("Name: "+request.getName());
            holder.car.setText("Leaving From: "+request.getLeavingFrom());
            holder.time.setText("Destination: "+request.getDestination());
            holder.leavingFrom.setText("Time: "+request.getTime());
            holder.seats.setText("Seats: "+request.getSeats());
        }

        @Override
        public int getItemCount() {
            return requests.size();
        }
    }

    private class RideListItemViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private TextView car;
        private TextView time;
        private TextView leavingFrom;
        private TextView seats;
        RideListItemViewHolder(@NonNull View view){
            super(view);
            name = view.findViewById(R.id.driver_name2);
            car = view.findViewById(R.id.ride_car2);
            time = view.findViewById(R.id.ride_time2);
            leavingFrom = view.findViewById(R.id.ride_location2);
            seats = view.findViewById(R.id.seat_number);
        }
    }

}
