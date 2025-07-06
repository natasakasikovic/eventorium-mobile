package com.eventorium.presentation.solution.fragments.service;

import static java.util.stream.Collectors.toList;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eventorium.R;
import com.eventorium.data.event.models.eventtype.EventType;
import com.eventorium.data.shared.utils.FileUtil;
import com.eventorium.data.shared.utils.ImageUpload;
import com.eventorium.data.solution.models.service.UpdateService;
import com.eventorium.data.solution.models.service.ServiceSummary;
import com.eventorium.data.solution.models.service.ReservationType;
import com.eventorium.databinding.FragmentEditServiceBinding;
import com.eventorium.presentation.event.viewmodels.EventTypeViewModel;
import com.eventorium.presentation.shared.adapters.ImageAdapter;
import com.eventorium.presentation.shared.models.ImageItem;
import com.eventorium.presentation.shared.models.RemoveImageRequest;
import com.eventorium.presentation.solution.viewmodels.ServiceViewModel;
import com.eventorium.presentation.shared.adapters.ChecklistAdapter;

import java.io.IOException;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EditServiceFragment extends Fragment {

    private FragmentEditServiceBinding binding;

    private ServiceViewModel serviceViewModel;
    private ChecklistAdapter<EventType> adapter;

    private static final String ARG_SERVICE = "serviceSummary";
    private ServiceSummary serviceSummary;
    private EventTypeViewModel eventTypeViewModel;
    private ImageUpload imageUpload;
    private final List<Uri> imageUris = new ArrayList<>();
    private NavController navController;

    private ImageAdapter newImagesAdapter;
    private final List<RemoveImageRequest> removedImages = new ArrayList<>();

    private ImageAdapter existingImagesAdapter;

    public EditServiceFragment() {
    }

    public static EditServiceFragment newInstance(ServiceSummary serviceSummary) {
        EditServiceFragment fragment = new EditServiceFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_SERVICE, serviceSummary);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            serviceSummary = getArguments().getParcelable(ARG_SERVICE);
        }
        ViewModelProvider provider = new ViewModelProvider(this);
        serviceViewModel = provider.get(ServiceViewModel.class);
        eventTypeViewModel = provider.get(EventTypeViewModel.class);
        navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditServiceBinding.inflate(inflater, container, false);
        setupImagePicker();
        existingImagesAdapter = new ImageAdapter(new ArrayList<>(), imageItem -> {
            removedImages.add(new RemoveImageRequest(imageItem.getId()));
            if (existingImagesAdapter.getItemCount() == 1) binding.images.setVisibility(View.GONE);
        });
        binding.images.setAdapter(existingImagesAdapter);
        binding.editServiceButton.setOnClickListener(v -> updateService());
        binding.newImagesCard.setVisibility(View.GONE);
        loadEventTypes();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupImageUpload();
    }

    private void setupImagePicker() {
        newImagesAdapter = new ImageAdapter(new ArrayList<>(), imageItem -> {
            imageUris.remove(imageItem.getUri());
            if (imageUris.isEmpty()) this.binding.newImagesCard.setVisibility(View.GONE);
        });
        binding.uploadButton.setOnClickListener(v -> imageUpload.openGallery(true));
        binding.newImages.setAdapter(newImagesAdapter);
    }

    private void setupImageUpload() {
        imageUpload = new ImageUpload(this, imageUris -> {
            newImagesAdapter.insert(imageUris.stream()
                    .map(uri -> {
                        try {
                            Bitmap bitmap = FileUtil.convertToBitmap(requireContext(), uri);
                            return new ImageItem(bitmap, uri);
                        } catch (IOException e) {
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(toList()));
            this.imageUris.addAll(imageUris);
            this.binding.newImagesCard.setVisibility(View.VISIBLE);
        });
    }

    private void updateService() {
        UpdateService dto = loadFormData();
        if(dto != null) {
            serviceViewModel.updateService(serviceSummary.getId(), loadFormData())
                    .observe(getViewLifecycleOwner(), service -> {
                        if (service.getError() == null) {
                            Toast.makeText(
                                    requireContext(),
                                    R.string.service_updated_successfully,
                                    Toast.LENGTH_SHORT
                            ).show();
                            if (removedImages.isEmpty() && imageUris.isEmpty()) {
                                navController.navigateUp();
                            } else {
                                removeImages();
                                uploadNewImages();
                            }
                        } else {
                            Toast.makeText(
                                    requireContext(),
                                    service.getError(),
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    });
        }
    }

    private UpdateService loadFormData() {
        try {
            ReservationType type = binding.manualChecked.isChecked()
                    ? ReservationType.MANUAL
                    : ReservationType.AUTOMATIC;

            List<Float> duration = binding.serviceDuration.getValues();

            return UpdateService.builder()
                    .name(String.valueOf(binding.serviceNameEditText.getText()))
                    .description(String.valueOf(binding.serviceDescriptionText.getText()))
                    .price(Double.parseDouble(String.valueOf(binding.servicePriceText.getText())))
                    .discount(Double.parseDouble(String.valueOf(binding.serviceDiscountText.getText())))
                    .specialties(String.valueOf(binding.serviceSpecificitiesText.getText()))
                    .cancellationDeadline(Integer.valueOf(String.valueOf(binding.serviceCancellationDeadlineText.getText())))
                    .available(binding.availabilityBox.isChecked())
                    .visible(binding.visibilityBox.isChecked())
                    .reservationDeadline(Integer.valueOf(String.valueOf(binding.serviceReservationDeadlineText.getText())))
                    .minDuration(duration.get(0).intValue())
                    .maxDuration(duration.get(1).intValue())
                    .type(type)
                    .eventTypesIds(((ChecklistAdapter<EventType>)
                            (Objects.requireNonNull(binding.eventTypeRecycleView.getAdapter())))
                            .getSelectedItems().stream()
                            .map(EventType::getId)
                            .collect(toList()))
                    .build();
        } catch (NullPointerException | NumberFormatException | DateTimeParseException exception) {
            Toast.makeText(
                    requireContext(),
                    R.string.please_fill_in_all_fields,
                    Toast.LENGTH_SHORT
            ).show();
            return null;
        }
    }

    @SuppressLint("SetTextI18n")
    private void fillForm() {
        serviceViewModel.getService(serviceSummary.getId()).observe(getViewLifecycleOwner(), service -> {
            binding.serviceNameEditText.setText(service.getName());
            binding.serviceDescriptionText.setText(service.getDescription());
            binding.serviceDiscountText.setText(service.getDiscount().toString());
            binding.serviceSpecificitiesText.setText(service.getSpecialties());
            binding.serviceReservationDeadlineText.setText(service.getReservationDeadline().toString());
            binding.serviceCancellationDeadlineText.setText(service.getCancellationDeadline().toString());
            binding.servicePriceText.setText(service.getPrice().toString());
            binding.visibilityBox.setChecked(service.getVisible());
            binding.availabilityBox.setChecked(service.getAvailable());
            binding.serviceDuration.setValues(List.of((float)service.getMinDuration(), (float)service.getMaxDuration()));
            if(service.getType() == ReservationType.AUTOMATIC) {
                binding.manualChecked.setChecked(true);
            } else {
                binding.automaticChecked.setChecked(true);
            }
            for(EventType eventType : service.getEventTypes()) {
                adapter.selectItem(eventType.getName());
            }
            loadImages();
        });
    }

    private void loadEventTypes() {
        eventTypeViewModel.getEventTypes().observe(getViewLifecycleOwner(), eventTypes -> {
            adapter = new ChecklistAdapter<>(eventTypes);
            binding.eventTypeRecycleView.setAdapter(adapter);
            fillForm();
        });
    }

    private void loadImages() {
        serviceViewModel.getServiceImages(serviceSummary.getId()).observe(getViewLifecycleOwner(), result -> {
            if (result.getError() == null) {
                List<ImageItem> images = result.getData();
                if (images.isEmpty()) this.binding.images.setVisibility(View.GONE);
                else existingImagesAdapter.insert(images);
                binding.loader.setVisibility(View.GONE);
            } else {
                Toast.makeText(requireContext(), result.getError(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    void removeImages() {
        if (!removedImages.isEmpty()) {
            serviceViewModel.removeImages(serviceSummary.getId(), removedImages).observe(getViewLifecycleOwner(), result -> {
                if (result.getError() != null)
                    Toast.makeText(requireContext(), result.getError(), Toast.LENGTH_SHORT).show();
            });
        } else {
            if (imageUris.isEmpty()) navController.navigateUp();
        }
    }

    void uploadNewImages() {
        if (!imageUris.isEmpty())
            serviceViewModel.uploadImages(serviceSummary.getId(), requireContext(), imageUris).observe(getViewLifecycleOwner(), success -> {
                if (!success)
                    Toast.makeText(requireContext(), "Error while uploading images", Toast.LENGTH_SHORT).show();
                navController.navigateUp();
            });
        else navController.navigateUp();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}