package com.example.nishikanto.itemdeliverapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.nishikanto.itemdeliverapp.customer.CustomerReportIssueActivity;
import com.example.nishikanto.itemdeliverapp.customer.DeliveryCancelActivity;
import com.example.nishikanto.itemdeliverapp.R;

public class DeliverySuccessFragment extends Fragment {
    private Button btn_issue;
    private Button btn_no_issue;
    private ImageView toolbar_image;
    private TextView toolbarTitle;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.delivery_success_layout, container, false);

        toolbar_image = this.getActivity().findViewById(R.id.toolbar_image);
        toolbarTitle = this.getActivity().findViewById(R.id.tvTitle);
        toolbarTitle.setText(R.string.delivered);

//        Drawable image = getResources().getDrawable(R.drawable.delivery4);
        toolbar_image.setImageResource(R.drawable.delivery4);

        btn_issue = view.findViewById(R.id.report_issue);
        btn_no_issue = view.findViewById(R.id.report_no_issue);

        btn_issue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getContext(), CustomerReportIssueActivity.class);
                startActivity(i);
            }
        });
        btn_no_issue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getContext(), DeliveryCancelActivity.class);
                startActivity(i);
            }
        });
        return view;
    }
}
