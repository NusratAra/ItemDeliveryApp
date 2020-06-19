package com.example.nishikanto.itemdeliverapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.nishikanto.itemdeliverapp.R;
import com.example.nishikanto.itemdeliverapp.customer.SetScheduleActivity;

public class ConfirmDeliveryFragment extends Fragment {
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
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, new ReadyForDeliveryFragment());
                fragmentTransaction.commit();
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
}
