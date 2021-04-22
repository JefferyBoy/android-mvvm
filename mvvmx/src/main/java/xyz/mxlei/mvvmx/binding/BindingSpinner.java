package xyz.mxlei.mvvmx.binding;

import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.databinding.BindingAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mxlei
 * @date 2020/7/14
 */
public class BindingSpinner {


    @BindingAdapter(value = {"binding_items", "binding_items_selected_index", "binding_items_onSelected", "binding_items_layout", "binding_items_dropdownlayout"}, requireAll = false)
    public static void setAdapter(final Spinner spinner, final List<Pair> items, int selectedIndex, int itemLayout, int dropdownLayout, final BindingCommand<Pair> command) {
        if (items == null) {
            throw new NullPointerException("The items cannot be null");
        }
        List<Object> firsts = new ArrayList<>();
        for (Pair pair : items) {
            firsts.add(pair.first);
        }
        if (itemLayout == 0) {
            itemLayout = android.R.layout.simple_spinner_item;
        }
        if (dropdownLayout == 0) {
            dropdownLayout = android.R.layout.simple_spinner_dropdown_item;
        }
        ArrayAdapter adapter = new ArrayAdapter<>(spinner.getContext(), itemLayout, 0, firsts);
        adapter.setDropDownViewResource(dropdownLayout);
        spinner.setAdapter(adapter);
        if (command != null) {
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    command.call(spinner, items.get(position));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        //设置默认选中位置
        if (selectedIndex < 0) {
            selectedIndex = 0;
        }
        if (selectedIndex > items.size() - 1) {
            selectedIndex = items.size() - 1;
        }
        spinner.setSelection(selectedIndex);
    }
}
