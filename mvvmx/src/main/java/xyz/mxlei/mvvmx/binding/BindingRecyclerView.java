package xyz.mxlei.mvvmx.binding;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import xyz.mxlei.mvvmx.binding.command.BindingCommand;

/**
 * @author mxlei
 * @date 2020/7/15
 */
public class BindingRecyclerView {

    @BindingAdapter("binding_lineManager")
    public static void setLineManager(RecyclerView recyclerView, LineManagers.LineManagerFactory lineManagerFactory) {
        recyclerView.addItemDecoration(lineManagerFactory.create(recyclerView));
    }

    @BindingAdapter(value = {"binding_onScrollChange", "binding_onScrollStateChanged"}, requireAll = false)
    public static void setAdapter(final RecyclerView recyclerView, final BindingCommand<ScrollDataWrapper> onScrollChange,
                                  final BindingCommand<Integer> onScrollStateChange) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int state;

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                state = newState;
                if (onScrollStateChange != null) {
                    onScrollStateChange.execute(newState);
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (onScrollChange != null) {
                    onScrollChange.execute(new ScrollDataWrapper(dx, dy, state));
                }
            }
        });
    }

    @BindingAdapter("binding_itemAnimator")
    public static void setItemAnimator(RecyclerView recyclerView, RecyclerView.ItemAnimator animator) {
        recyclerView.setItemAnimator(animator);
    }

    public static class ScrollDataWrapper {
        public float scrollX;
        public float scrollY;
        public int state;

        public ScrollDataWrapper(float scrollX, float scrollY, int state) {
            this.scrollX = scrollX;
            this.scrollY = scrollY;
            this.state = state;
        }
    }
}
