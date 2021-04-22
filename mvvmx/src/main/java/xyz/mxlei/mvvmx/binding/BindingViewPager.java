package xyz.mxlei.mvvmx.binding;

import androidx.databinding.BindingAdapter;
import androidx.viewpager.widget.ViewPager;

/**
 * @author mxlei
 * @date 2020/7/15
 */
public class BindingViewPager {

    public static class ViewPagerDataWrapper {
        public float positionOffset;
        public float position;
        public int positionOffsetPixels;
        public int state;

        public ViewPagerDataWrapper(float position, float positionOffset, int positionOffsetPixels, int state) {
            this.positionOffset = positionOffset;
            this.position = position;
            this.positionOffsetPixels = positionOffsetPixels;
            this.state = state;
        }
    }

    @BindingAdapter(value = {"binding_onPageScrolled", "binding_onPageSelected", "binding_onPageScrollStateChanged"}, requireAll = false)
    public static void setAdapter(final ViewPager viewPager, final BindingCommand<ViewPagerDataWrapper> onPageScroll,
                                  final BindingCommand<Integer> onPageSelected,
                                  final BindingCommand<Integer> onPageScrollStateChange) {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            private int state;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (onPageScroll != null) {
                    onPageScroll.call(viewPager, new ViewPagerDataWrapper(position, positionOffset, positionOffsetPixels, state));
                }
            }

            @Override
            public void onPageSelected(int position) {
                if (onPageSelected != null) {
                    onPageSelected.call(viewPager, position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                this.state = state;
                if (onPageScrollStateChange != null) {
                    onPageScrollStateChange.call(viewPager, state);
                }
            }
        });
    }
}
