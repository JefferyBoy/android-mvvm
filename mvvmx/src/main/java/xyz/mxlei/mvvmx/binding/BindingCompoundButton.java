package xyz.mxlei.mvvmx.binding;

import android.widget.CompoundButton;

import androidx.databinding.BindingAdapter;

import xyz.mxlei.mvvmx.binding.command.BindingCommand;

/**
 * @author mxlei
 * @date 2020/7/14
 */
public class BindingCompoundButton {

    @BindingAdapter(value = {"binding_onCheckedChange"}, requireAll = false)
    public static void setAdapter(CompoundButton buttonView, final BindingCommand<Boolean> command) {
        buttonView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                command.execute(isChecked);
            }
        });
    }
}
