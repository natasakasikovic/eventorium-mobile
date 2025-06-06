package com.eventorium.presentation.event.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eventorium.R;
import com.eventorium.data.auth.models.User;
import com.eventorium.data.category.models.Category;
import com.eventorium.data.event.models.CreateEventType;
import com.eventorium.data.event.models.EventType;
import com.eventorium.databinding.FragmentCreateEventTypeBinding;
import com.eventorium.presentation.category.viewmodels.CategoryViewModel;
import com.eventorium.presentation.event.viewmodels.EventTypeViewModel;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CreateEventTypeFragment extends Fragment {

    private FragmentCreateEventTypeBinding binding;
    private EditText nameTextEdit;
    private EditText descriptionTextEdit;
    private Uri selectedImageUri;
    private ActivityResultLauncher<Intent> pickImageLauncher;

    private EventTypeViewModel eventTypeViewModel;
    private CategoryViewModel categoryViewModel;

    private Category selectedCategory;

    private List<Category> selectedCategories = new ArrayList<>();
    private List<Category> allCategories = new ArrayList<>();

    public CreateEventTypeFragment() { }

    public static CreateEventTypeFragment newInstance() {
        return new CreateEventTypeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        eventTypeViewModel = new ViewModelProvider(this).get(EventTypeViewModel.class);

        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        binding.imageView.setImageURI(selectedImageUri);
                    }
                }
        );

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCreateEventTypeBinding.inflate(inflater, container, false);

        nameTextEdit = binding.etEventTypeName;
        descriptionTextEdit = binding.etEventTypeDescription;

        categoryViewModel.getCategories().observe(getViewLifecycleOwner(), categories -> {
            allCategories = categories;
            ArrayAdapter<Category> categoryAdapter = new ArrayAdapter<>(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    allCategories
            );
            binding.comboBoxCategories.setAdapter(categoryAdapter);
        });

        setupImagePicker();
        binding.btnAddCategory.setOnClickListener(v -> addCategoryToContainer());
        binding.createEventType.setOnClickListener(v -> createEventType());
        return binding.getRoot();
    }

    private void setupImagePicker() {
        binding.uploadButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickImageLauncher.launch(intent);
        });
    }

    private void addCategoryToContainer() {

        selectedCategory = (Category) binding.comboBoxCategories.getSelectedItem();

        if (!selectedCategories.contains(selectedCategory) && allCategories.contains(selectedCategory)) {
            selectedCategories.add(selectedCategory);

            LinearLayout itemLayout = new LinearLayout(getContext());
            itemLayout.setOrientation(LinearLayout.HORIZONTAL);
            itemLayout.setGravity(Gravity.CENTER_VERTICAL);
            itemLayout.setPadding(8, 8, 8, 8);

            TextView categoryName = new TextView(getContext());
            categoryName.setText(selectedCategory.getName());
            categoryName.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

            ImageButton btnRemove = new ImageButton(getContext());
            btnRemove.setImageResource(R.drawable.ic_close);
            btnRemove.setBackgroundColor(Color.TRANSPARENT);
            btnRemove.setTag(selectedCategory);

            btnRemove.setOnClickListener(v -> {
                Category categoryToRemove = (Category) v.getTag();
                removeCategoryFromContainer(categoryToRemove, itemLayout);
                selectedCategories.remove(categoryToRemove);
            });

            itemLayout.addView(categoryName);
            itemLayout.addView(btnRemove);
            binding.containerAddedItems.addView(itemLayout);
        }
    }

    private void uploadImage(EventType eventType) {
        if (selectedImageUri != null)
            eventTypeViewModel.uploadImage(eventType.getId(), getContext(), selectedImageUri)
                    .observe(getViewLifecycleOwner(), success -> {
                        if (!success) Toast.makeText(requireContext(),
                                R.string.image_has_not_been_added,
                                Toast.LENGTH_LONG).show();
                    });
    }

    private void removeCategoryFromContainer(Category category, LinearLayout itemLayout) {
        selectedCategories.remove(category);
        binding.containerAddedItems.removeView(itemLayout);
    }

    private void createEventType() {
        String name = binding.etEventTypeName.getText().toString();
        String description = binding.etEventTypeDescription.getText().toString();

        if (name.isEmpty() || description.isEmpty()) {
            Toast.makeText(
                    requireContext(),
                    R.string.please_fill_in_all_fields,
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        CreateEventType eventType = new CreateEventType(name, description, selectedCategories);
        eventTypeViewModel.createEventType(eventType).observe(getViewLifecycleOwner(), response -> {
            if (response != null) {
                Toast.makeText(
                        requireContext(),
                        "Event type created successfully",
                        Toast.LENGTH_SHORT
                ).show();
                uploadImage(response);
                NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
                navController.navigate(R.id.eventTypesFragment);
            } else {
                Toast.makeText(
                        requireContext(),
                        R.string.failed_to_create_category,
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}