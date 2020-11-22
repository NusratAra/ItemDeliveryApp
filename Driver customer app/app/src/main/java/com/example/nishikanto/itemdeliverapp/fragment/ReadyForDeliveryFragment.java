package com.example.nishikanto.itemdeliverapp.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.nishikanto.itemdeliverapp.R;

public class ReadyForDeliveryFragment extends Fragment {
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private LinearLayout layout;
    private ImageView toolbar_image;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ready_for_delivery_layout, container, false);
        layout = view.findViewById(R.id.layout);

        toolbar_image = this.getActivity().findViewById(R.id.toolbar_image);
        Drawable image = getResources().getDrawable(R.drawable.delivery2);
        toolbar_image.setImageDrawable(image);
//
//        layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                fragmentManager = getFragmentManager();
//                fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.frame_layout, new DeliveryLocationFragment());
//                fragmentTransaction.commit();
//            }
//        });
        return view;

    }
}
