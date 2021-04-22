package xyz.mxlei.mvvmx.binding;

import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.databinding.BindingAdapter;

/**
 * @author mxlei
 * @date 2020/7/14
 */
public class BindingListView {

    @BindingAdapter(value = {"binding_onScrollChange", "binding_onScrollStateChange"}, requireAll = false)
    public static void setAdapter(ListView listView, final BindingCommand<ListViewScrollDataWrapper> onScrollChange, final BindingCommand<Integer> onScrollStateChange) {
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int scrollState;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                this.scrollState = scrollState;
                if (onScrollStateChange != null) {
                    onScrollStateChange.call(view, scrollState);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (onScrollChange != null) {
                    onScrollChange.call(view, new ListViewScrollDataWrapper(scrollState, firstVisibleItem, visibleItemCount, totalItemCount));
                }
            }
        });
    }

    @BindingAdapter(value = {"binding_onItemClick", "binding_onItemLongClick"}, requireAll = false)
    public static void setAdapter2(final ListView listView, final BindingCommand<Integer> click, final BindingCommand<Integer> longClick) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (click != null) {
                    click.call(view, position);
                }
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (longClick != null) {
                    try {
                        longClick.call(listView, position);
                        return true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });
    }

    public static class ListViewScrollDataWrapper {
        public int firstVisibleItem;
        public int visibleItemCount;
        public int totalItemCount;
        public int scrollState;

        public ListViewScrollDataWrapper(int scrollState, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            this.firstVisibleItem = firstVisibleItem;
            this.visibleItemCount = visibleItemCount;
            this.totalItemCount = totalItemCount;
            this.scrollState = scrollState;
        }
    }
}
