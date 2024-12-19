package com.eventorium.presentation.solution.adapters;

import android.annotation.SuppressLint;
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
import com.eventorium.presentation.solution.fragments.service.EditServiceFragment;
import com.eventorium.presentation.solution.fragments.service.ServiceDetailsFragment;
import com.eventorium.presentation.util.listeners.OnDeleteClickListener;
import com.eventorium.presentation.util.listeners.OnEditClickListener;

import java.util.List;

public class ManageableServiceAdapter extends BaseServiceAdapter<ManageableServiceAdapter.ManageableServiceViewHolder> {

    private final OnDeleteClickListener<ServiceSummary> deleteListener;
    public ManageableServiceAdapter(
            List<ServiceSummary> serviceSummaries,
            OnDeleteClickListener<ServiceSummary> listener
    ) {
        super(serviceSummaries);
        this.deleteListener = listener;
    }

    @NonNull
    @Override
    public ManageableServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manageable_service_card, parent, false);
        return new ManageableServiceViewHolder(view);
    }

    public void setServices(List<ServiceSummary> newServices) {
        serviceSummaries = newServices;
        notifyDataSetChanged();;
    }

    public class ManageableServiceViewHolder extends BaseServiceViewHolder {
        TextView nameTextView;
        TextView priceTextView;
        ImageView imageView;

        Button seeMoreButton;
        Button editButton;
        Button deleteButton;

        public ManageableServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.service_name);
            priceTextView = itemView.findViewById(R.id.service_price);
            imageView = itemView.findViewById(R.id.service_photo);
            seeMoreButton = itemView.findViewById(R.id.see_more_button);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void bind(ServiceSummary serviceSummary) {
            nameTextView.setText(serviceSummary.getName());
            priceTextView.setText(serviceSummary.getPrice().toString());
            imageView.setImageBitmap(serviceSummary.getImage());

            seeMoreButton.setOnClickListener(v -> {
                NavController navController = Navigation.findNavController(itemView);
                navController.navigate(R.id.action_manageService_to_serviceDetailsFragment,
                        ServiceDetailsFragment.newInstance(serviceSummary.getId()).getArguments());
            });
            editButton.setOnClickListener(v -> {
                NavController navController = Navigation.findNavController(itemView);
                navController.navigate(R.id.action_manageService_to_editService,
                        EditServiceFragment.newInstance(serviceSummary).getArguments());
            });

            deleteButton.setOnClickListener(v -> deleteListener.onDeleteClick(serviceSummary));
        }
    }
}
