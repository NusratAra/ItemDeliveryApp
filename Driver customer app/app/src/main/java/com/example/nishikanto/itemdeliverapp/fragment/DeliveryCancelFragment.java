package com.example.nishikanto.itemdeliverapp.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.nishikanto.itemdeliverapp.R;

public class DeliveryCancelFragment extends Fragment {
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private TextView toolbarTitle;
    private ImageView toolbarImage;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.delivery_cancel_layout, container, false);

        toolbarTitle = this.getActivity().findViewById(R.id.tvTitle);
        toolbarImage = this.getActivity().findViewById(R.id.toolbar_image);

        toolbarTitle.setText("");
        Drawable image = getResources().getDrawable(R.drawable.all_gray_b);
        toolbarImage.setImageDrawable(image);

        return view;
    }
}
