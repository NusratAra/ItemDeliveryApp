package com.example.nishikanto.itemdeliverapp.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.nishikanto.itemdeliverapp.R;

public class DeliveryLocationFragment extends Fragment {
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private LinearLayout layout;
    private ImageView toolbar_image;
    private TextView toolbarTitle;
    private ConstraintLayout constraintLayout;
    private Toolbar toolbar;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.delivery_location_layout, container, false);
        layout = view.findViewById(R.id.layout);

        toolbar_image = this.getActivity().findViewById(R.id.toolbar_image);
        toolbarTitle = this.getActivity().findViewById(R.id.tvTitle);

        toolbarTitle.setText(R.string.in_delivery);

        Drawable image = getResources().getDrawable(R.drawable.delivery3);
        toolbar_image.setImageDrawable(image);



        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, new DeliverySuccessFragment());
                fragmentTransaction.commit();
            }
        });
        return view;
    }
}
