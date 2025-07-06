package com.eventorium.presentation.event.fragments.eventtype;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.eventorium.R;
import com.eventorium.data.category.models.Category;
import com.eventorium.data.event.models.eventtype.EventType;
import com.eventorium.databinding.FragmentEditEventTypeBinding;
import com.eventorium.presentation.category.viewmodels.CategoryViewModel;
import com.eventorium.presentation.event.viewmodels.EventTypeViewModel;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EditEventTypeFragment extends Fragment {

    private FragmentEditEventTypeBinding binding;
    private static final String ARG_EVENT_TYPE = "eventType";
    private EventType eventType;
    private EventTypeViewModel eventTypeViewModel;
    private CategoryViewModel categoryViewModel;
    private Uri selectedImageUri = null;
    private ActivityResultLauncher<Intent> pickImageLauncher;

    private Category selectedCategory;
    private List<Category> selectedCategories = new ArrayList<>();
    private List<Category> allCategories = new ArrayList<>();

    public EditEventTypeFragment() {
    }

    public static EditEventTypeFragment newInstance(EventType eventType) {
        EditEventTypeFragment fragment = new EditEventTypeFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_EVENT_TYPE, eventType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            eventType = getArguments().getParcelable(ARG_EVENT_TYPE);
        ViewModelProvider provider = new ViewModelProvider(this);
        eventTypeViewModel = provider.get(EventTypeViewModel.class);
        categoryViewModel = provider.get(CategoryViewModel.class);

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditEventTypeBinding.inflate(inflater, container, false);
        setupImagePicker();
        loadCategories();
        loadImage(eventType.getId());
        binding.btnAddCategory.setOnClickListener(v -> addCategoryToContainer());
        binding.btnSaveChanges.setOnClickListener(v -> saveChanges());
        return binding.getRoot();
    }

    private void loadImage(Long id) {
        eventTypeViewModel.getImage(id).observe(getViewLifecycleOwner(), image -> {
            if (image != null)
                binding.imageView.setImageBitmap(image);
            else
                binding.imageView.setImageResource(R.drawable.profile_photo);
        });
    }

    private void setupImagePicker() {
        binding.uploadButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickImageLauncher.launch(intent);
        });
    }

    private void loadCategories() {
        categoryViewModel.getCategories().observe(getViewLifecycleOwner(), categories -> {
            allCategories = categories;
            ArrayAdapter<Category> categoryAdapter = new ArrayAdapter<>(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    allCategories
            );
            binding.comboBoxCategories.setAdapter(categoryAdapter);
        });

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fillForm();
    }

    private void addCategoryToContainer(Category category) {
        if (!selectedCategories.contains(category)) {
            selectedCategories.add(category);

            LinearLayout itemLayout = new LinearLayout(getContext());
            itemLayout.setOrientation(LinearLayout.HORIZONTAL);
            itemLayout.setGravity(Gravity.CENTER_VERTICAL);
            itemLayout.setPadding(8, 8, 8, 8);

            TextView categoryName = new TextView(getContext());
            categoryName.setText(category.getName());
            categoryName.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            Typeface customFont = ResourcesCompat.getFont(requireContext(), R.font.jejumyeongjo);
            categoryName.setTypeface(customFont);

            ImageButton btnRemove = new ImageButton(getContext());
            btnRemove.setImageResource(R.drawable.ic_close);
            btnRemove.setBackgroundColor(Color.TRANSPARENT);
            btnRemove.setTag(category);

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

    private void addCategoryToContainer() {
        selectedCategory = (Category) binding.comboBoxCategories.getSelectedItem();
        addCategoryToContainer(selectedCategory);
    }

    private void removeCategoryFromContainer(Category category, LinearLayout itemLayout) {
        selectedCategories.remove(category);
        binding.containerAddedItems.removeView(itemLayout);
    }

    private void saveChanges() {
        String description = binding.etEventTypeDescription.getText().toString();
        if (description.isEmpty()) {
            Toast.makeText(requireContext(), R.string.please_fill_in_all_fields, Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedCategories.isEmpty()) {
            Toast.makeText(requireContext(), R.string.select_at_least_one_category, Toast.LENGTH_SHORT).show();
            return;
        }

        eventType.setDescription(description);
        eventType.setSuggestedCategories(selectedCategories);
        eventTypeViewModel.update(eventType).observe(getViewLifecycleOwner(), result -> {
            if (result.getError() == null) {
                Toast.makeText(requireContext(), "Success!", Toast.LENGTH_SHORT).show();
                if (selectedImageUri != null) uploadImage();
                NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
                navController.navigateUp();
            } else {
                Toast.makeText(requireContext(), result.getError(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadImage() {
        eventTypeViewModel.updateImage(eventType.getId(), getContext(), selectedImageUri)
                .observe(getViewLifecycleOwner(), success -> {
                    if (!success) Toast.makeText(requireContext(),
                            R.string.image_has_not_been_added,
                            Toast.LENGTH_LONG).show();
                });
    }

    private void fillForm() {
        binding.eventTypeName.setText(eventType.getName());
        binding.etEventTypeDescription.setText(eventType.getDescription());
        for (Category cat : eventType.getSuggestedCategories()) {
            addCategoryToContainer(cat);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}