package com.example.nishikanto.itemdeliverapp.adapter.holder;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nishikanto.itemdeliverapp.R;
import com.example.nishikanto.itemdeliverapp.customer.IssueWritingActivity;
import com.example.nishikanto.itemdeliverapp.model.Issue;

import butterknife.BindView;
import butterknife.ButterKnife;


public class IssueReportViewHolder extends RecyclerView.ViewHolder {
    private static final String TAG = IssueReportViewHolder.class.getSimpleName();

    private Activity activity;
    @BindView(R.id.issue_title)
    TextView issueTitle;

    private Issue issue;


    public IssueReportViewHolder(Activity activity, @NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.activity = activity;

    }

    public void setData(Issue issue){
        this.issue = issue;
        issueTitle.setText(issue.getType());
        issueTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleIssueCall(issue.getId());

            }
        });
       // issueTitle.setText();

    }

    private void singleIssueCall(int id) {
        Log.d(TAG, "singleIssueCall: "+ id);
        Intent i=new Intent(activity.getBaseContext(), IssueWritingActivity.class);
        i.putExtra("issue", this.issue);
        activity.startActivity(i);
        activity.finish();
//        TripAuthenticationService tripAuthenticationService = RetrofitInstance.getServiceCall(activity);
//        Call<SingleTripIssue> call = tripAuthenticationService.getSingleTripIssues(id);
//        call.enqueue(new Callback<SingleTripIssue>() {
//            @Override
//            public void onResponse(Call<SingleTripIssue> call, Response<SingleTripIssue> response) {
//                if(response.body() != null){
////                    Log.d(TAG, "SingleIssue: "+ new GsonBuilder().setPrettyPrinting().create().toJson((response.body())));
//
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<SingleTripIssue> call, Throwable t) {
//                Log.d(TAG, "onFailure: "+ t.getMessage());
//            }
//        });
    }
}
