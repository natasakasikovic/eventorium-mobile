package com.eventorium.presentation.viewmodels;

import android.os.Build;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.eventorium.data.models.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CategoryProposalViewModel extends ViewModel {
    private final MutableLiveData<List<Category>> categoryProposals = new MutableLiveData<>();
    private final MutableLiveData<List<Category>> existingCategories = new MutableLiveData<>();
    private final MutableLiveData<Category> selectedCategory = new MutableLiveData<>();

    public CategoryProposalViewModel() {
        categoryProposals.setValue(List.of(
                new Category("Electronics", "Devices and gadgets such as smartphones, laptops, and accessories."),
                new Category("Clothing", "Apparel and fashion items for men, women, and children."),
                new Category("Food & Beverages", "Groceries, packaged foods, and drinks."),
                new Category("Home Appliances", "Appliances and tools for home use, such as refrigerators, washing machines, etc."),
                new Category("Health & Wellness", "Products related to fitness, medicine, and personal well-being."),
                new Category("Automotive", "Cars, motorcycles, spare parts, and automotive accessories.")
        ));
        existingCategories.setValue(List.of(
                new Category("Electronics", "Devices and gadgets such as smartphones, laptops, and accessories."),
                new Category("Clothing", "Apparel and fashion items for men, women, and children."),
                new Category("Food & Beverages", "Groceries, packaged foods, and drinks."),
                new Category("Home Appliances", "Appliances and tools for home use, such as refrigerators, washing machines, etc."),
                new Category("Health & Wellness", "Products related to fitness, medicine, and personal well-being."),
                new Category("Automotive", "Cars, motorcycles, spare parts, and automotive accessories.")
        ));
    }

    public MutableLiveData<List<Category>> getCategoryProposals() {
        return categoryProposals;
    }

    public MutableLiveData<List<Category>> getExistingCategories() {
        return existingCategories;
    }

    public MutableLiveData<Category> getSelectedCategory() {
        return selectedCategory;
    }

    public void setCategoryProposals(List<Category> categoriesProposals) {
        this.categoryProposals.setValue(categoriesProposals);
    }

    public void setExistingCategories(List<Category> categories) {
        this.categoryProposals.setValue(categories);
    }

    public void setSelectedCategory(Category category) {
        selectedCategory.setValue(category);
    }

    public void updateCategory(Category updateCategory) {

    }

    public List<String> getExistingCategoriesName() {
        List<String> result = new ArrayList<>();
        result.add("");
        List<String> categoriesNames;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            categoriesNames = Objects.requireNonNull(existingCategories.getValue())
                    .stream()
                    .map(Category::getName)
                    .toList();
        } else {
            categoriesNames = Objects.requireNonNull(existingCategories.getValue())
                    .stream()
                    .map(Category::getName)
                    .collect(Collectors.toList());
        }
        result.addAll(categoriesNames);
        return result;
    }
}
