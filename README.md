# BindingCollectionAdapter
Easy way to bind collections to listviews and recyclerviews with the new [Android Data Binding framework](https://developer.android.com/tools/data-binding/guide.html).

## Usage

You need to provied your items and an `ItemView` to bind to the layout. You should use an
`ObservableList` to automaticaly update your view based on list changes. However, you can
use any `Collection` if you don't need that functionality.

```java
public class ViewModel {
  public final ObservableList<String> items = new ObservableArrayList<>();
  public final ItemView itemView = ItemView.of(BR.item, R.layout.item);
}
```

Then bind it to the collection view with `app:items` and `app:itemView`. There are also some 
convience factories to attach a `LayoutManager` to a `RecyclerView` with `app:layoutManager`.

```xml
<!-- layout.xml -->
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto">
    <data>
      <variable name="viewModel" type="com.example.ViewModel"/> 
      <import type="me.tatarka.bindingcollectionadapter.LayoutManagers" />
    </data>
    
    <ListView
      android:layout_width="match_parent"
      android:layout_height="match_parent"
      app:items="@{viewModel.items}"
      app:itemView="@{viewModel.itemView}"/>
      
    <android.support.v7.widget.RecyclerView
      android:layout_width="match_parent"
      android:layout_height="match_parent"
      app:layoutManager="{@LayoutManagers.linear()}"
      app:items="@{viewModel.items}"
      app:itemView="@{viewModel.itemView}"/>
      
    <android.support.v4.view.ViewPager
      android:layout_width="match_parent"
      android:layout_height="match_parent"
      app:items="@{viewModel.items}"
      app:itemView="@{viewModel.itemView}"/>
</layout>
```

In your item layout, the collection item will be bound to the variable with the
name you passed into the `ItemView`

```xml
<!-- item.xml -->
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto">
    <data>
      <variable name="item" type="String"/> 
    </data>
    
    <TextView
      android:layout_width="match_parent"
      android:layout_height="wrap_content"
      android:text="@{item}"/>
</layout>
```

## Multiple View Types

You can use multiple view types by using a `ItemViewSelector` instead. You can still bind
it to the view with `app:itemView`.

```java
public final ItemViewSelector<String> itemView = new BaseItemViewSelector<String>() {
    @Override
    public void select(ItemView itemView, int position, String item) {
        itemView.setBindingVariable(BR.item)
                .setLayoutRes(position == 0 ? R.layout.item_header : R.layout.item);
    }
    
    // This is only needed if you are using a BindingListViewAdapter
    @Override
    public int viewTypeCount() {
      return 2;
    }
};
```

Note that `select` is called many times so you should not do any complex processing in there.

## Additonal Adapter Configuration

### BindingListViewAdapter

You can set a callback to give an id for each item in the list with

```java
adapter.setItemIds(new BindingListViewAdapter.ItemIds<T>() {
    @Override
    public long getItemId(int position, T item) {
        return // Calculate item id.
    }
});
```
or by defining `app:itemIds="@{itemIds}"` in the `ListView` in your layout file.

Setting this will make `hasStableIds` return true which can increase performace of data changes.

You can also define a different dropdown view layout by setting

```java
itemView.setLayoutRes(BindingListViewAdapter.DROP_DOWN_LAYOUT, R.layout.item_dropdown);
```

### BindingViewPagerAdapter

You can set a callback to give a page title for each item in the list with

```java
adapter.setPageTitles(new PageTitles<T>() {
    @Override
    public CharSequence getPageTitle(int position, T item) {
        return "Page Title";
    }
});
```
or by defining `app:pageTitles="@{pageTitles}"` in the `ViewPager` in your layout file.

## Known Issues

### Cannot Resolve the libraries `@BindingAdapter`'s

This is likely because you are using the [android-apt](https://bitbucket.org/hvisser/android-apt) plugin which breaks this for some reason. To workaround, you can define all the adapters in a class in your project and delegate to the library ones.

```java
import android.databinding.BindingAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;

import java.util.Collection;

import me.tatarka.bindingcollectionadapter.BindingCollectionAdapters;
import me.tatarka.bindingcollectionadapter.BindingListViewAdapter;
import me.tatarka.bindingcollectionadapter.BindingViewPagerAdapter;
import me.tatarka.bindingcollectionadapter.ItemView;
import me.tatarka.bindingcollectionadapter.ItemViewSelector;
import me.tatarka.bindingcollectionadapter.LayoutManagers;

/**
 * BindingAdapters from BindingCollectionAdapter, used to work around android-apt issue.
 */
public class CopiedBindingCollectionAdapters {
    @BindingAdapter("items")
    public static <T> void setItems(RecyclerView recyclerView, Collection<T> items) {
        BindingCollectionAdapters.setItems(recyclerView, items);
    }

    @BindingAdapter("itemView")
    public static <T> void setItemView(RecyclerView recyclerView, ItemView itemView) {
        BindingCollectionAdapters.setItemView(recyclerView, itemView);
    }

    @BindingAdapter("itemView")
    public static <T> void setItemViewSelector(RecyclerView recyclerView, ItemViewSelector<T> selector) {
        BindingCollectionAdapters.setItemViewSelector(recyclerView, selector);
    }

    @BindingAdapter("layoutManager")
    public static void setLayoutManager(RecyclerView recyclerView, LayoutManagers.LayoutManagerFactory layoutManagerFactory) {
        BindingCollectionAdapters.setLayoutManager(recyclerView, layoutManagerFactory);
    }

    @BindingAdapter("items")
    public static <T> void setItems(ListView listView, Collection<T> items) {
        BindingCollectionAdapters.setItems(listView, items);
    }

    @BindingAdapter("itemIds")
    public static <T> void setItemIds(ListView listView, BindingListViewAdapter.ItemIds<T> itemIds) {
        BindingCollectionAdapters.setItemIds(listView, itemIds);
    }

    @BindingAdapter("itemView")
    public static <T> void setItemView(ListView listView, ItemView itemView) {
        BindingCollectionAdapters.setItemView(listView, itemView);
    }

    @BindingAdapter("itemView")
    public static <T> void setItemViewSelector(ListView listView, ItemViewSelector<T> selector) {
        BindingCollectionAdapters.setItemViewSelector(listView, selector);
    }

    @BindingAdapter("items")
    public static <T> void setItems(ViewPager viewPager, Collection<T> items) {
        BindingCollectionAdapters.setItems(viewPager, items);
    }

    @BindingAdapter("pageTitles")
    public static <T> void setPageTitles(ViewPager viewPager, BindingViewPagerAdapter.PageTitles<T> pageTitles) {
        BindingCollectionAdapters.setPageTitles(viewPager, pageTitles);
    }

    @BindingAdapter("itemView")
    public static <T> void setItemView(ViewPager viewPager, ItemView itemView) {
        BindingCollectionAdapters.setItemView(viewPager, itemView);
    }

    @BindingAdapter("itemView")
    public static <T> void setItemViewSelector(ViewPager viewPager, ItemViewSelector<T> selector) {
        BindingCollectionAdapters.setItemViewSelector(viewPager, selector);
    }
}
```

## License

    Copyright 2015 Evan Tatarka
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
       http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
