package com.eventorium.presentation.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.eventorium.data.models.Category;

import java.util.List;

public class CategoryProposalViewModel extends ViewModel {
    private final MutableLiveData<List<Category>> categoryProposals = new MutableLiveData<>();
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
    }

    public MutableLiveData<List<Category>> getCategoryProposals() {
        return categoryProposals;
    }

    public MutableLiveData<Category> getSelectedCategory() {
        return selectedCategory;
    }

    public void setCategoryProposals(List<Category> categoriesProposals) {
        this.categoryProposals.setValue(categoriesProposals);
    }

    public void setSelectedCategory(Category category) {
        selectedCategory.setValue(category);
    }

    public void updateCategory(Category updateCategory) {

    }
}
