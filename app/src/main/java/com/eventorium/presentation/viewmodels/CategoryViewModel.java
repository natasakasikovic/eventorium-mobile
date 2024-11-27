package com.eventorium.presentation.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.eventorium.data.models.Category;

import java.util.List;

public class CategoryViewModel extends ViewModel {
    private final MutableLiveData<List<Category>> categories = new MutableLiveData<>();
    private final MutableLiveData<Category> selectedCategory = new MutableLiveData<>();

    public CategoryViewModel() {
        categories.setValue(List.of(
            new Category("Electronics", "Devices and gadgets such as smartphones, laptops, and accessories."),
            new Category("Clothing", "Apparel and fashion items for men, women, and children."),
            new Category("Food & Beverages", "Groceries, packaged foods, and drinks."),
            new Category("Home Appliances", "Appliances and tools for home use, such as refrigerators, washing machines, etc."),
            new Category("Health & Wellness", "Products related to fitness, medicine, and personal well-being."),
            new Category("Automotive", "Cars, motorcycles, spare parts, and automotive accessories.")
        ));
    }

    public MutableLiveData<List<Category>> getCategories() {
        return categories;
    }

    public MutableLiveData<Category> getSelectedCategory() {
        return selectedCategory;
    }

    public void setCategories(List<Category> categories) {
        this.categories.setValue(categories);
    }

    public void setSelectedCategory(Category category) {
        selectedCategory.setValue(category);
    }

    public void updateCategory(Category updateCategory) {

    }
}
