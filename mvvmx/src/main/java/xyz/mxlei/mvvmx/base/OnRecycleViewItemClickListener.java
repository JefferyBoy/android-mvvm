package xyz.mxlei.mvvmx.base;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GestureDetectorCompat;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author mxlei
 * @date 2020/11/24
 */
public abstract class OnRecycleViewItemClickListener implements RecyclerView.OnItemTouchListener {

    private GestureDetectorCompat gestureDetectorCompat;
    private boolean disallowIntercept = false;

    private GestureDetectorCompat getGestureDetectorCompat(@NonNull RecyclerView rv) {
        if (gestureDetectorCompat == null) {
            gestureDetectorCompat = new GestureDetectorCompat(
                    rv.getContext(),
                    new ItemTouchHelperGestureListener(rv)
            );
        }
        return gestureDetectorCompat;
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
        if (disallowIntercept) {
            return false;
        }
        return getGestureDetectorCompat(rv).onTouchEvent(e);
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
        getGestureDetectorCompat(rv).onTouchEvent(e);
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        this.disallowIntercept = disallowIntercept;
    }

    protected abstract boolean onItemClick(RecyclerView.ViewHolder holder, int position);

    protected void onItemLongClick(RecyclerView.ViewHolder holder, int position) {

    }

    private class ItemTouchHelperGestureListener extends GestureDetector.SimpleOnGestureListener {

        private final RecyclerView recyclerView;

        public ItemTouchHelperGestureListener(@NonNull RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (child != null) {
                RecyclerView.ViewHolder vh = recyclerView.getChildViewHolder(child);
                int position = recyclerView.getChildLayoutPosition(child);
                return onItemClick(vh, position);
            }
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
            View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (child != null) {
                RecyclerView.ViewHolder vh = recyclerView.getChildViewHolder(child);
                int position = recyclerView.getChildLayoutPosition(child);
                onItemLongClick(vh, position);
            }
        }
    }
}
