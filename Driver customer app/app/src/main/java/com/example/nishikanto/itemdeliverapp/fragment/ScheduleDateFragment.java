package com.example.nishikanto.itemdeliverapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.nishikanto.itemdeliverapp.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ScheduleDateFragment extends Fragment{
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    LinearLayout setDateLayout;

    LinearLayout day1Layout;
    LinearLayout day2Layout;
    LinearLayout day3Layout;

    private TextView day1;
    private TextView date1;
    private TextView day2;
    private TextView date2;
    private TextView day3;
    private TextView date3;

    private Date nextDate1 = null;
    private Date nextDate2 = null;
    private Date nextDate3 = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.schedule_date_layout, container, false);

        initView(view);
        dateFormat();
        setClickListener();

//        setDateLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                fragmentManager = getFragmentManager();
//                fragmentTransaction = fragmentManager.beginTransaction();
//                Bundle bundle = new Bundle();
//                bundle.putString("dateKey", nextDate1.toString());
//                fragmentTransaction.replace(R.id.frame_schedule, new ScheduleTimeFragment(bundle));
//                fragmentTransaction.commit();
//            }
//        });
        return view;
    }

    private void setClickListener() {

        Bundle bundle = new Bundle();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


        day1Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("dateKey", sdf.format(nextDate1));
                getFragment(bundle);
            }
        });
        day2Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("dateKey", sdf.format(nextDate2));
                getFragment(bundle);
            }
        });
        day3Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("dateKey", sdf.format(nextDate3));
                getFragment(bundle);
            }
        });

    }

    private void getFragment(Bundle bundle) {
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_schedule, new ScheduleTimeFragment(bundle));
        fragmentTransaction.commit();
    }

    private void dateFormat() {


        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        nextDate1 = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        nextDate2 = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        nextDate3 = calendar.getTime();

        DateFormat outputFormatter1 = new SimpleDateFormat("MMMM dd, yyyy");
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");

        day1.setText(sdf.format(nextDate1));
        date1.setText(outputFormatter1.format(nextDate1));
        day2.setText(sdf.format(nextDate2));
        date2.setText(outputFormatter1.format(nextDate2));
        day3.setText(sdf.format(nextDate3));
        date3.setText(outputFormatter1.format(nextDate3));
    }

    private void initView(View view) {
        setDateLayout = view.findViewById(R.id.set_date_layout);

        day1Layout = view.findViewById(R.id.day1_layout);
        day2Layout = view.findViewById(R.id.day2_layout);
        day3Layout = view.findViewById(R.id.day3_layout);


        day1 = view.findViewById(R.id.day1);
        date1 = view.findViewById(R.id.date1);

        day2 = view.findViewById(R.id.day2);
        date2 = view.findViewById(R.id.date2);

        day3 = view.findViewById(R.id.day3);
        date3 = view.findViewById(R.id.date3);
    }
}
