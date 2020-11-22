package com.example.nishikanto.itemdeliverapp.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nishikanto.itemdeliverapp.R;
import com.example.nishikanto.itemdeliverapp.adapter.holder.IssueReportViewHolder;
import com.example.nishikanto.itemdeliverapp.model.Issue;

import java.util.ArrayList;

public class IssueReportAdapter extends RecyclerView.Adapter<IssueReportViewHolder> {

    private static final String TAG = IssueReportAdapter.class.getSimpleName();
    private Activity activity;
    private ArrayList<Issue> arrayList;

    public IssueReportAdapter(Activity activity, ArrayList<Issue> arrayList){
        this.activity = activity;
        this.arrayList = arrayList;
    }
    

    @NonNull
    @Override
    public IssueReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new IssueReportViewHolder(activity,
                LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_listview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull IssueReportViewHolder holder, int position) {
        holder.setData(arrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
