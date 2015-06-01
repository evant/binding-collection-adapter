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

## Additonal ItemView Params

Some collection views may optionally take addiditonal params to control how an item is displayed.
For example, you can set a drop down layout res on the `BindingListViewAdapter`.

```java
  public final ItemView itemView = ItemView.of(BR.item, R.layout.item)
                                      .set(BindingListAdapter.DROP_DOWN_LAYOUT, R.layout.item_drop_down);
```

Currently, there are `BindingListAdpater.DROP_DOWN_LAYOUT`, `BindingListAdapter.ITEM_ID` and 
`BindingViewPagerAdapter.TITLE`. See the javadoc for more information.
