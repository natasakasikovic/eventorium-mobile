package com.eventorium.presentation.user.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eventorium.R;
import com.eventorium.data.auth.models.UserReportResponse;
import com.eventorium.presentation.shared.listeners.OnSeeMoreClick;

import java.util.List;

public class UserReportAdapter extends RecyclerView.Adapter<UserReportAdapter.ReportViewHolder> {

    private List<UserReportResponse> reports;
    private final OnSeeMoreClick<UserReportResponse> listener;

    public UserReportAdapter(List<UserReportResponse> reports, OnSeeMoreClick<UserReportResponse> listener) {
        this.reports = reports;
        this.listener = listener;
    }

    public void setData(List<UserReportResponse> reports){
        this.reports = reports;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_card, parent, false);
        return new ReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserReportAdapter.ReportViewHolder holder, int position) {
        UserReportResponse report = reports.get(position);
        holder.bind(report);
    }

    @Override
    public int getItemCount() {
        return reports.size();
    }

    public class ReportViewHolder extends RecyclerView.ViewHolder {
        TextView offenderTextView;
        TextView reporterTextView;
        Button seeMoreButton;

        public ReportViewHolder(View itemView) {
            super(itemView);
            offenderTextView = itemView.findViewById(R.id.offender);
            reporterTextView = itemView.findViewById(R.id.reporter);
            seeMoreButton = itemView.findViewById(R.id.see_more_button);
        }

        public void bind(UserReportResponse report) {
            reporterTextView.setText(report.getReporter());
            offenderTextView.setText(report.getOffender());
            seeMoreButton.setOnClickListener(v -> listener.navigateToDetails(report));
        }
    }
}