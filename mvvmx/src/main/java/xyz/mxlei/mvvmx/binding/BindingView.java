package xyz.mxlei.mvvmx.binding;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;

import androidx.databinding.BindingAdapter;

/**
 * @author mxlei
 * @date 2020/7/12
 */
public class BindingView {

    /**
     * View的点击事件监听器，可以防重复点击
     */
    private static abstract class ClickListener implements View.OnClickListener {
        //是否防重复点击
        private final boolean isThrottleFirst;
        private long clickTime;
        //放重复点击间隔
        private static final int CLICK_INTERVAL = 1000;

        public ClickListener() {
            this(false);
        }

        public ClickListener(boolean isThrottleFirst) {
            this.isThrottleFirst = isThrottleFirst;
        }

        @Override
        public void onClick(View v) {
            if (isThrottleFirst) {
                if (System.currentTimeMillis() - clickTime > CLICK_INTERVAL) {
                    clickTime = System.currentTimeMillis();
                    click(v);
                }
            } else {
                clickTime = System.currentTimeMillis();
                click(v);
            }
        }

        public abstract void click(View v);
    }

    @BindingAdapter(value = {"binding_onClick", "binding_isThrottleFirst"}, requireAll = false)
    public static void setAdapter(final View view, final BindingCommand command, final boolean isThrottleFirst) {
        view.setOnClickListener(new ClickListener(isThrottleFirst) {
            @Override
            public void click(View v) {
                command.call(view, null);
            }
        });
    }

    @BindingAdapter(value = {"binding_onLongClick"}, requireAll = false)
    public static void setAdapter2(final View view, final BindingCommand command) {
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                command.call(view, null);
                return false;
            }
        });
    }

    @BindingAdapter(value = {"binding_onFocusChanged"}, requireAll = false)
    public static void setAdapter3(final View view, final BindingCommand<Boolean> command) {
        view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                command.call(view, hasFocus);
            }
        });
    }

    @BindingAdapter(value = {"binding_onTouch"}, requireAll = false)
    public static void setAdapter4(final View view, final BindingCommand3<MotionEvent, Boolean> command) {
        view.setClickable(true);
        view.setFocusable(true);
        view.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                try {
                    return command.call(view, event);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
    }
}
