package com.eventorium.presentation.solution.helpers;

import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.eventorium.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class SortBottomSheetDialogHelper {

    public String extractSortCriteria(BottomSheetDialog dialogView) {
        RadioGroup radioGroupSortField = dialogView.findViewById(R.id.radioGroupSortField);
        RadioGroup radioGroupOrder = dialogView.findViewById(R.id.radioGroupOrder);

        int selectedSortFieldId = radioGroupSortField.getCheckedRadioButtonId();
        int selectedOrderId = radioGroupOrder.getCheckedRadioButtonId();

        if (selectedSortFieldId == -1 || selectedOrderId == -1)
            return null;

        RadioButton selectedSortField = dialogView.findViewById(selectedSortFieldId);
        RadioButton selectedOrder = dialogView.findViewById(selectedOrderId);

        String sortField = getSortField(selectedSortField.getId());
        String sortDirection = getSortDirection(selectedOrder.getText().toString());

        return String.format("%s,%s", sortField, sortDirection);
    }

    private String getSortField(int radioButtonId) {
        if (radioButtonId == R.id.radioName)
            return "name";
        else if (radioButtonId == R.id.radioDescription)
            return "description";
        else if (radioButtonId == R.id.radioDiscount)
            return "discount";
        else
            return "name"; // default fallback
    }

    private String getSortDirection(String text) {
        text = text.toLowerCase();
        if (text.contains("asc"))
            return "asc";
        else if (text.contains("desc"))
            return "desc";
        else
            return "asc"; // default fallback
    }
}