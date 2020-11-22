package com.example.nishikanto.itemdeliverapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.nishikanto.itemdeliverapp.ItemDeliveryApplication;
import com.example.nishikanto.itemdeliverapp.R;
import com.example.nishikanto.itemdeliverapp.customer.SetScheduleActivity;
import com.example.nishikanto.itemdeliverapp.model.SingleTrip;
import com.example.nishikanto.itemdeliverapp.services.NoConnectivityException;
import com.example.nishikanto.itemdeliverapp.services.RetrofitInstance;
import com.example.nishikanto.itemdeliverapp.services.TripAuthenticationService;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmDeliveryFragment extends Fragment {
    private static final String TAG = ConfirmDeliveryFragment.class.getSimpleName();
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private TextView confirm;
    private TextView toolbarTitle;
    private TextView cancel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.confirm_delivery_layout, container, false);
        confirm = view.findViewById(R.id.confirm);
        cancel = view.findViewById(R.id.cancel);
        toolbarTitle = this.getActivity().findViewById(R.id.tvTitle);
        toolbarTitle.setText(R.string.ready_to_delivery);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptTripCall();

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getContext(), SetScheduleActivity.class);
                startActivity(i);
            }
        });
        return view;
    }

    private void acceptTripCall() {
        int id = ((ItemDeliveryApplication) getActivity().getApplicationContext()).getTrip().getId();
        Log.d(TAG, "acceptTripCall: "+ id);
        TripAuthenticationService tripAuthenticationService = RetrofitInstance.getServiceCall(getContext());
        Call<SingleTrip> call = tripAuthenticationService.acceptByCustomer(id);
        call.enqueue(new Callback<SingleTrip>() {
            @Override
            public void onResponse(Call<SingleTrip> call, Response<SingleTrip> response) {
                Log.d(TAG, "TrackTrip: "+ new GsonBuilder().setPrettyPrinting().create().toJson(response.body()));

                if(response.body() != null){

                    fragmentManager = getFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_layout, new ReadyForDeliveryFragment());
                    fragmentTransaction.commit();
                }

                if (response.errorBody() != null){
                    Log.d(TAG, "TrackTripError: "+ new GsonBuilder().setPrettyPrinting().create().toJson(response.errorBody()));
                    Toast.makeText(getContext(), "Please try again later!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SingleTrip> call, Throwable t) {
                if (t instanceof NoConnectivityException) {
                    Log.e(TAG, "onFailureThrowEx: " + t.getMessage());
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Check your internet connection.", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Server Error!", Toast.LENGTH_SHORT);
                    toast.show();
                    Log.d(TAG, "onFailure: " + t.getMessage());
                }

            }
        });
    }
}
