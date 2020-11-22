package com.example.nishikanto.itemdeliverapp.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.nishikanto.itemdeliverapp.ItemDeliveryApplication;
import com.example.nishikanto.itemdeliverapp.R;
import com.example.nishikanto.itemdeliverapp.model.SingleTrip;
import com.example.nishikanto.itemdeliverapp.services.RetrofitInstance;
import com.example.nishikanto.itemdeliverapp.services.TripAuthenticationService;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScheduleTimeFragment extends Fragment {
    private static final String TAG = ScheduleTimeFragment.class.getSimpleName();
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private LinearLayout setTimeLayout;
    private LinearLayout morningLayout;
    private LinearLayout afternoonLayout;
    private LinearLayout eveningLayout;
    private Bundle bundle;
    private SingleTrip singleTrip;

    public ScheduleTimeFragment(Bundle bundle) {
        this.bundle = bundle;
        Log.d(TAG, "ScheduleTimeFragment: "+ bundle.getString("dateKey"));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.schedule_time_layout, container, false);

        final String[] slot = new String[1];
        setTimeLayout = view.findViewById(R.id.set_time_layout);
        morningLayout = view.findViewById(R.id.morning_layout);
        afternoonLayout = view.findViewById(R.id.afternoon_layout);
        eveningLayout = view.findViewById(R.id.evening_layout);

        morningLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slot[0] = "morning";
                rescheduleCall(bundle.getString("dateKey"), slot[0]);
            }
        });
        afternoonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slot[0] = "afternoon";
                rescheduleCall(bundle.getString("dateKey"), slot[0]);
            }
        });
        eveningLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slot[0] = "evening";
                rescheduleCall(bundle.getString("dateKey"), slot[0]);
            }
        });

//        setTimeLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                fragmentManager = getFragmentManager();
//                fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.frame_schedule, new ScheduleSuccessFragment());
//                fragmentTransaction.commit();
//            }
//        });
        return view;
    }

    private void rescheduleCall(String date, String time) {

//        Log.d(TAG, "TrackTrip: "+ new GsonBuilder().setPrettyPrinting().create().toJson(((ItemDeliveryApplication) getActivity().getApplicationContext()).getTrip()));
        int id = ((ItemDeliveryApplication) getActivity().getApplicationContext()).getTrip().getId();
        Log.d(TAG, "rescheduleCall: "+id+"+"+ date +"+"+ time);
        TripAuthenticationService tripAuthenticationService = RetrofitInstance.getServiceCall(getContext());
        Call<SingleTrip> tripCall = tripAuthenticationService.rescheduleTrack(id, date, time);
        tripCall.enqueue(new Callback<SingleTrip>() {
            @Override
            public void onResponse(Call<SingleTrip> call, Response<SingleTrip> response) {

                if(response.body() != null){
                    Log.d(TAG, "TrackTrip: "+ new GsonBuilder().setPrettyPrinting().create().toJson(response.body().getTrip()));

                    fragmentManager = getFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_schedule, new ScheduleSuccessFragment());
                    fragmentTransaction.commit();
                }
            }

            @Override
            public void onFailure(Call<SingleTrip> call, Throwable t) {
                Log.d(TAG, "onFailure: "+ t.getMessage());
            }
        });

    }
}
