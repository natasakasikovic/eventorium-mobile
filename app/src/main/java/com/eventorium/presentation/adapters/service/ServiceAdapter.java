package com.eventorium.presentation.adapters.service;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.eventorium.R;
import com.eventorium.data.models.Service;
import com.eventorium.presentation.fragments.service.ServiceDetailsFragment;

import java.util.List;

public class ServiceAdapter extends BaseServiceAdapter<ServiceAdapter.ServiceViewHolder> {

    public ServiceAdapter(List<Service> services) {
        super(services);
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
        public void bind(Service service) {
            nameTextView.setText(service.getName());
            priceTextView.setText(service.getPrice().toString());
            photoImageview.setImageResource(service.getPhoto());
            Log.i("NAVIGATION", "BINDING?");
            seeMoreButton.setOnClickListener(v -> {
                NavController navController = Navigation.findNavController(itemView);
                navController.navigate(R.id.action_home_to_service_details,
                        ServiceDetailsFragment.newInstance(service).getArguments());
            });
        }
    }
}
