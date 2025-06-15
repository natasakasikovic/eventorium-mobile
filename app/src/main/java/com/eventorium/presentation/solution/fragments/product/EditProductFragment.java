package com.eventorium.presentation.solution.fragments.product;

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
import com.eventorium.data.shared.utils.ImageUpload;
import com.eventorium.data.solution.models.product.ProductSummary;
import com.eventorium.data.solution.models.product.UpdateProduct;
import com.eventorium.databinding.FragmentEditProductBinding;
import com.eventorium.presentation.event.viewmodels.EventTypeViewModel;
import com.eventorium.presentation.shared.adapters.ChecklistAdapter;
import com.eventorium.presentation.shared.adapters.ImageAdapter;
import com.eventorium.presentation.shared.models.ImageItem;
import com.eventorium.presentation.shared.models.RemoveImageRequest;
import com.eventorium.presentation.solution.viewmodels.ProductViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EditProductFragment extends Fragment {

    private FragmentEditProductBinding binding;
    private ProductViewModel viewModel;
    private EventTypeViewModel eventTypeViewModel;
    private NavController navController;
    private static final String ARG_PRODUCT = "productSummary";
    private ProductSummary productSummary;

    private ImageUpload imageUpload;
    private ImageAdapter newImagesAdapter;
    private ImageAdapter existingImagesAdapter;
    private final List<Uri> imageUris = new ArrayList<>();
    private final List<RemoveImageRequest> removedImages = new ArrayList<>();
    private ChecklistAdapter<EventType> adapter;

    public EditProductFragment() { }

    public static EditProductFragment newInstance(ProductSummary productSummary) {
        EditProductFragment fragment = new EditProductFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PRODUCT, productSummary);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            productSummary = getArguments().getParcelable(ARG_PRODUCT);
        ViewModelProvider provider = new ViewModelProvider(this);
        viewModel = provider.get(ProductViewModel.class);
        eventTypeViewModel = provider.get(EventTypeViewModel.class);
        navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditProductBinding.inflate(inflater, container, false);
        setupImagePicker();
        existingImagesAdapter = new ImageAdapter(new ArrayList<>(), imageItem -> {
            removedImages.add(new RemoveImageRequest(imageItem.getId()));
            if (existingImagesAdapter.getItemCount() == 1) binding.images.setVisibility(View.GONE);
        });
        binding.images.setAdapter(existingImagesAdapter);
        binding.editProductButton.setOnClickListener(v -> updateProduct());
        binding.newImagesCard.setVisibility(View.GONE);
        loadEventTypes();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupImageUpload();
    }

    private void setupImageUpload() {
        imageUpload = new ImageUpload(this, imageUris -> {
            newImagesAdapter.insert(imageUris.stream()
                    .map(uri -> {
                        try {
                            Bitmap bitmap = ImageDecoder.decodeBitmap(
                                    ImageDecoder.createSource(requireContext().getContentResolver(), uri)
                            );
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

    private void setupImagePicker() {
        newImagesAdapter = new ImageAdapter(new ArrayList<>(), imageItem -> {
            imageUris.remove(imageItem.getUri());
            if (imageUris.isEmpty()) this.binding.newImagesCard.setVisibility(View.GONE);
        });
        binding.uploadButton.setOnClickListener(v -> imageUpload.openGallery(true));
        binding.newImages.setAdapter(newImagesAdapter);
    }

    private void loadEventTypes() {
        eventTypeViewModel.getEventTypes().observe(getViewLifecycleOwner(), eventTypes -> {
            adapter = new ChecklistAdapter<>(eventTypes);
            binding.eventTypeRecycleView.setAdapter(adapter);
            fillForm();
        });
    }

    @SuppressLint("SetTextI18n")
    private void fillForm() {
        viewModel.getProduct(productSummary.getId()).observe(getViewLifecycleOwner(), service -> {
            binding.productNameEditText.setText(service.getName());
            binding.productDescriptionText.setText(service.getDescription());
            binding.productDiscountText.setText(service.getDiscount().toString());
            binding.productPriceText.setText(service.getPrice().toString());
            binding.visibilityBox.setChecked(service.getVisible());
            binding.availabilityBox.setChecked(service.getAvailable());
            for(EventType eventType : service.getEventTypes()) {
                adapter.selectItem(eventType.getName());
            }
            loadImages();
        });
    }

    private void loadImages() {
        viewModel.getProductImages(productSummary.getId()).observe(getViewLifecycleOwner(), result -> {
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

    private void updateProduct() {
        UpdateProduct request = loadFormData();
        if (request == null) return;

        viewModel.updateProduct(productSummary.getId(), loadFormData())
                .observe(getViewLifecycleOwner(), result -> {
                    if (result.getData() != null) {
                        Toast.makeText(requireContext(), R.string.success, Toast.LENGTH_SHORT).show();
                        removeImages();
                        uploadNewImages();
                    } else {
                        Toast.makeText(requireContext(), result.getError(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    void removeImages() {
        if (!removedImages.isEmpty()) {
            viewModel.removeImages(productSummary.getId(), removedImages).observe(getViewLifecycleOwner(), result -> {
                if (result.getError() != null)
                    Toast.makeText(requireContext(), result.getError(), Toast.LENGTH_SHORT).show();
            });
        } else {
            if (imageUris.isEmpty()) navController.navigateUp();
        }
    }

    void uploadNewImages() {
        if (!imageUris.isEmpty())
            viewModel.uploadImages(productSummary.getId(), requireContext(), imageUris).observe(getViewLifecycleOwner(), result -> {
                if (result.getError() != null)
                    Toast.makeText(requireContext(), result.getError(), Toast.LENGTH_SHORT).show();
                navController.navigateUp();
            });
        else navController.navigateUp();
    }

    private UpdateProduct loadFormData() {
        return UpdateProduct.builder()
            .name(String.valueOf(binding.productNameEditText.getText()))
            .description(String.valueOf(binding.productDescriptionText.getText()))
            .price(Double.parseDouble(String.valueOf(binding.productPriceText.getText())))
            .discount(Double.parseDouble(String.valueOf(binding.productDiscountText.getText())))
            .available(binding.availabilityBox.isChecked())
            .visible(binding.visibilityBox.isChecked())
            .eventTypesIds(((ChecklistAdapter<EventType>)
                    (Objects.requireNonNull(binding.eventTypeRecycleView.getAdapter())))
                    .getSelectedItems().stream()
                    .map(EventType::getId)
                    .collect(toList()))
            .build();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}