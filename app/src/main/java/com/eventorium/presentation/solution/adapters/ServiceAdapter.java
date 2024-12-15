package com.eventorium.presentation.solution.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.eventorium.R;
import com.eventorium.data.solution.models.ServiceSummary;
import com.eventorium.presentation.solution.fragments.service.ServiceDetailsFragment;

import java.util.List;
import java.util.Objects;

public class ServiceAdapter extends BaseServiceAdapter<ServiceAdapter.ServiceViewHolder> {

    public ServiceAdapter(List<ServiceSummary> serviceSummaries) {
        super(serviceSummaries);
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_card, parent, false);
        return new ServiceViewHolder(view);
    }

    public static class ServiceViewHolder extends BaseServiceViewHolder {
        TextView nameTextView;
        TextView priceTextView;
        ImageView photoImageview;
        Button seeMoreButton;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.service_name);
            priceTextView = itemView.findViewById(R.id.service_price);
            photoImageview = itemView.findViewById(R.id.service_photo);
            seeMoreButton = itemView.findViewById(R.id.see_more_button);
        }

        @Override
        public void bind(ServiceSummary serviceSummary) {
            nameTextView.setText(serviceSummary.getName());
            priceTextView.setText(serviceSummary.getPrice().toString());
            photoImageview.setImageResource(serviceSummary.getPhoto());
            seeMoreButton.setOnClickListener(v -> {
                NavController navController = Navigation.findNavController(itemView);
                int currentId = Objects.requireNonNull(navController.getCurrentDestination()).getId();
                int actionId = 0;

                if (currentId == R.id.homepageFragment) {
                    actionId = R.id.action_home_to_service_details;
                } else if (currentId == R.id.serviceOverviewFragment) {
                    actionId = R.id.action_serviceOverview_to_service_details;
                } else if (currentId == R.id.favourites) {
                    actionId = R.id.action_favServices_to_serviceDetailsFragment;
                } else {
                    throw new IllegalStateException("Unreachable...");
                }

                navController.navigate(actionId,
                        ServiceDetailsFragment.newInstance().getArguments());
            });
        }
    }
}
