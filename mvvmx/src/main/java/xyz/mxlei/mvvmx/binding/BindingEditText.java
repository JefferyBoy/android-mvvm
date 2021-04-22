package xyz.mxlei.mvvmx.binding;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.databinding.BindingAdapter;

/**
 * @author mxlei
 * @date 2020/7/13
 */
public class BindingEditText {

    @BindingAdapter(value = {"binding_beforeTextChanged", "binding_onTextChanged", "binding_afterTextChanged"}, requireAll = false)
    public static void setAdapter(final EditText editText, final BindingCommand<CharSequence> before, final BindingCommand<CharSequence> on, final BindingCommand<CharSequence> after) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (before != null) {
                    before.call(editText, s);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (on != null) {
                    on.call(editText, s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (after != null) {
                    after.call(editText, s);
                }
            }
        });
    }
}
