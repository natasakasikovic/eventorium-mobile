package com.eventorium.presentation.solution.fragments.product;

import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.eventorium.R;
import com.eventorium.data.category.models.Category;
import com.eventorium.databinding.FragmentCreateProductBinding;
import com.eventorium.presentation.category.viewmodels.CategoryViewModel;
import com.eventorium.presentation.event.viewmodels.EventTypeViewModel;
import com.eventorium.presentation.shared.adapters.ChecklistAdapter;
import com.eventorium.presentation.shared.adapters.ImageAdapter;
import com.eventorium.presentation.shared.models.ImageItem;
import com.eventorium.presentation.solution.viewmodels.ProductViewModel;
import com.eventorium.presentation.util.ImageUpload;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CreateProductFragment extends Fragment {
    private FragmentCreateProductBinding binding;
    private final List<Uri> imageUris = new ArrayList<>();
    private ImageUpload imageUpload;
    private ImageAdapter imageAdapter;
    private ProductViewModel productViewModel;
    private EventTypeViewModel eventTypeViewModel;
    private CategoryViewModel categoryViewModel;

    public CreateProductFragment() { }

    public static CreateProductFragment newInstance() {
        return new CreateProductFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewModelProvider provider = new ViewModelProvider(this);
        productViewModel = provider.get(ProductViewModel.class);
        categoryViewModel = provider.get(CategoryViewModel.class);
        eventTypeViewModel = provider.get(EventTypeViewModel.class);
        setupImageUpload();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCreateProductBinding.inflate(inflater, container, false);
        loadCategories();
        loadEventTypes();
        setupImagePicker();
        binding.createProductBtn.setOnClickListener(v -> create());
        return binding.getRoot();
    }

    private void setupImagePicker() {
        imageAdapter = new ImageAdapter(new ArrayList<>(),
                imageItem -> imageUris.remove(imageItem.getUri()));
        binding.uploadButton.setOnClickListener(v -> imageUpload.openGallery(true));
        binding.newImages.setAdapter(imageAdapter);
    }

    private void loadCategories() {
        categoryViewModel.getCategories().observe(getViewLifecycleOwner(), categories -> {
            ArrayAdapter<Category> categoryAdapter = new ArrayAdapter<>(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    categories
            );
            binding.categorySpinner.setAdapter(categoryAdapter);
        });
    }

    private void loadEventTypes() {
        eventTypeViewModel.getEventTypes().observe(getViewLifecycleOwner(), eventTypes -> {
            binding.eventTypeRecycleView.setAdapter(new ChecklistAdapter<>(eventTypes));
        });
    }
    private void setupImageUpload() {
        imageUpload = new ImageUpload(this, imageUris -> {
            imageAdapter.insert(imageUris.stream()
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
                    .collect(Collectors.toList()));
            this.imageUris.addAll(imageUris);
        });
    }

    private void create() {

    }
}