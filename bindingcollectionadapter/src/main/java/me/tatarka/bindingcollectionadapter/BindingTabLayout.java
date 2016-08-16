package me.tatarka.bindingcollectionadapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * A TabLayout that doesn't reset the ViewPager's position when calling {@link #setupWithViewPager}.
 */
public class BindingTabLayout extends TabLayout {

    private final TabLayoutOnPageChangeListener onPageChangeListener =
            new TabLayoutOnPageChangeListener(this);

    public BindingTabLayout(Context context) {
        super(context);
    }

    public BindingTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BindingTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setupWithViewPager(@NonNull ViewPager viewPager) {
        final PagerAdapter adapter = viewPager.getAdapter();
        if (adapter == null) {
            throw new IllegalArgumentException("ViewPager does not have a PagerAdapter set");
        }

        // make sure our page change listener is already added to the ViewPager
        viewPager.removeOnPageChangeListener(onPageChangeListener);
        // Now we'll add our page change listener to the ViewPager
        viewPager.addOnPageChangeListener(new TabLayoutOnPageChangeListener(this));

        // Now we'll set our tab selected listener to set ViewPager's current item
        // unlike with ViewPager, TabLayout can only have one listener
        setOnTabSelectedListener(new ViewPagerOnTabSelectedListener(viewPager));

        // update tabs keeping current selected position, if it still exists
        removeAllTabs();
        for (int i = 0; i < adapter.getCount() ; i++) {
            addTab(newTab().setText(adapter.getPageTitle(i)),
                    i == viewPager.getCurrentItem()
            );
        }
    }
}
