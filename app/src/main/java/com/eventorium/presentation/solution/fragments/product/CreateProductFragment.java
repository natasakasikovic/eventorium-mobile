package com.eventorium.presentation.solution.fragments.product;

import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.eventorium.R;
import com.eventorium.data.category.models.Category;
import com.eventorium.data.event.models.eventtype.EventType;
import com.eventorium.data.solution.models.product.CreateProduct;
import com.eventorium.databinding.FragmentCreateProductBinding;
import com.eventorium.presentation.category.viewmodels.CategoryViewModel;
import com.eventorium.presentation.event.viewmodels.EventTypeViewModel;
import com.eventorium.presentation.shared.adapters.ChecklistAdapter;
import com.eventorium.presentation.shared.adapters.ImageAdapter;
import com.eventorium.presentation.shared.models.ImageItem;
import com.eventorium.presentation.solution.viewmodels.ProductViewModel;
import com.eventorium.data.shared.utils.ImageUpload;

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
    ChecklistAdapter<EventType> eventTypeAdapter;

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
        Category suggestion = Category.builder().id(null).name("I would like to suggest category").build();
        categoryViewModel.getCategories().observe(getViewLifecycleOwner(), categories -> {
            categories.add(0, suggestion);
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
            eventTypeAdapter = new ChecklistAdapter<>(eventTypes);
            binding.eventTypeRecycleView.setAdapter(eventTypeAdapter);
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
        CreateProduct product = loadFormFields();
        productViewModel.createProduct(product).observe(getViewLifecycleOwner(), result -> {
            if (result.getData() != null) {
                if (imageUris.isEmpty()) navigateToProductOverview();
                else uploadImages(result.getData().getId());
            }
            else displayErrorMessage(result.getError());

        });
    }

    private void uploadImages(Long id) {
        productViewModel.uploadImages(id, requireContext(), imageUris).observe(getViewLifecycleOwner(), result -> {
            if (result.getError() == null) navigateToProductOverview();
            else displayErrorMessage(result.getError());
        });
    }

    private void displayErrorMessage(String error) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show();
    }

    private void navigateToProductOverview() {
        Toast.makeText(requireContext(), "Product successfully created", Toast.LENGTH_SHORT).show();
        NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
        navController.navigate(R.id.action_create_to_manage_services);
    }

    private CreateProduct loadFormFields() {
        return CreateProduct.builder()
                .name(binding.name.getText().toString())
                .description(binding.description.getText().toString())
                .price(Double.parseDouble(String.valueOf(binding.price.getText())))
                .discount(Double.parseDouble(String.valueOf(binding.discount.getText())))
                .category(getCategory())
                .eventTypes(eventTypeAdapter.getSelectedItems())
                .isAvailable(binding.availabilityBox.isChecked())
                .isAvailable(binding.visibilityBox.isChecked())
                .build();
    }

    private Category getCategory() {
        Category category = (Category) binding.categorySpinner.getSelectedItem();
        if (category.getId() == null) {
            category.setName(binding.suggestedCategoryName.getText().toString());
            category.setDescription(binding.suggestedCategoryDescription.getText().toString());
        }
        return category;
    }
}