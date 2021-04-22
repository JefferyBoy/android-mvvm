package xyz.mxlei.mvvmx.binding;

import android.widget.RadioGroup;

import androidx.databinding.BindingAdapter;

/**
 * @author mxlei
 * @date 2020/7/14
 */
public class BindingRadioGroup {

    @BindingAdapter(value = {"binding_onCheckedChange"}, requireAll = false)
    public static void setAdapter(final RadioGroup radioGroup, final BindingCommand<Integer> command) {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                command.call(radioGroup, checkedId);
            }
        });
    }
}
