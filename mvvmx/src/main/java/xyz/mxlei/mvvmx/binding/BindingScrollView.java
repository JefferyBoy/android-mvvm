package xyz.mxlei.mvvmx.binding;

import android.view.ViewTreeObserver;
import android.widget.ScrollView;

import androidx.core.widget.NestedScrollView;
import androidx.databinding.BindingAdapter;

import xyz.mxlei.mvvmx.binding.command.BindingCommand;

/**
 * @author mxlei
 * @date 2020/7/14
 */
public class BindingScrollView {

    @BindingAdapter(value = {"binding_onScrollChange"}, requireAll = false)
    public static void setAdapter(final ScrollView scrollView, final BindingCommand<ScrollDataWrapper> command) {
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                command.execute(new ScrollDataWrapper(scrollView.getScrollX(), scrollView.getScrollY()));
            }
        });
    }

    @BindingAdapter(value = {"binding_onScrollChange"}, requireAll = false)
    public static void setAdapter(final NestedScrollView scrollView, final BindingCommand<ScrollDataWrapper> command) {
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                command.execute(new ScrollDataWrapper(scrollView.getScrollX(), scrollView.getScrollY()));
            }
        });
    }

    public static class ScrollDataWrapper {
        public float scrollX;
        public float scrollY;

        public ScrollDataWrapper(float scrollX, float scrollY) {
            this.scrollX = scrollX;
            this.scrollY = scrollY;
        }
    }
}
