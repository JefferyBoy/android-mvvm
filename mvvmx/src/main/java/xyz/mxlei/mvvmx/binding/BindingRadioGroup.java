package xyz.mxlei.mvvmx.binding;

import android.widget.RadioGroup;

import androidx.databinding.BindingAdapter;

import xyz.mxlei.mvvmx.binding.command.BindingCommand;

/**
 * @author mxlei
 * @date 2020/7/14
 */
public class BindingRadioGroup {

    @BindingAdapter(value = {"binding_onCheckedChange"}, requireAll = false)
    public static void setAdapter(RadioGroup radioGroup, final BindingCommand<Integer> command) {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                command.execute(checkedId);
            }
        });
    }
}
