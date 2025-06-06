package com.eventorium.presentation.event.fragments.eventtype;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eventorium.R;
import com.eventorium.data.category.models.Category;
import com.eventorium.data.event.models.EventType;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditEventTypeBinding.inflate(inflater, container, false);
        categoryViewModel.getCategories().observe(getViewLifecycleOwner(), categories -> {
            allCategories = categories;
            ArrayAdapter<Category> categoryAdapter = new ArrayAdapter<>(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    allCategories
            );
            binding.comboBoxCategories.setAdapter(categoryAdapter);
        });

        binding.btnAddCategory.setOnClickListener(v -> addCategoryToContainer());
        binding.btnSaveChanges.setOnClickListener(v -> saveChanges());
        return binding.getRoot();
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
                NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
                navController.navigateUp();
            } else {
                Toast.makeText(requireContext(), result.getError(), Toast.LENGTH_SHORT).show();
            }
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