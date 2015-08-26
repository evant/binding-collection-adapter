# BindingCollectionAdapter
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/me.tatarka/bindingcollectionadapter/badge.svg?style=flat)](https://maven-badges.herokuapp.com/maven-central/me.tatarka/bindingcollectionadapter)

Easy way to bind collections to listviews and recyclerviews with the new [Android Data Binding framework](https://developer.android.com/tools/data-binding/guide.html).

## Download

```groovy
compile 'me.tatarka:bindingcollectionadapter:0.12'
```

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
      app:layoutManager="@{LayoutManagers.linear()}"
      app:items="@{viewModel.items}"
      app:itemView="@{viewModel.itemView}"/>
      
    <android.support.v4.view.ViewPager
      android:layout_width="match_parent"
      android:layout_height="match_parent"
      app:items="@{viewModel.items}"
      app:itemView="@{viewModel.itemView}"/>
      
    <Spinner
      android:layout_width="match_parent"
      android:layout_height="match_parent"
      app:items="@{viewModel.items}"
      app:itemView="@{viewModel.itemView}"
      app:dropDownItemView="@{viewModel.dropDownItemView}"/>
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
      android:id="@+id/text"
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
        itemView.set(BR.item, position == 0 ? R.layout.item_header : R.layout.item);
    }
    
    // This is only needed if you are using a BindingListViewAdapter
    @Override
    public int viewTypeCount() {
      return 2;
    }
};
```

Note that `select` is called many times so you should not do any complex processing in there. If you don't need to bind an item at a specific position (a static footer for example) you can use `ItemView.BINDING_VARIABLE_NONE` as the binding varibale.

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

## Directly manipulationg views

Data binding is awesome and all, but you may run into a case where you simply need to manipulate the views directly. You can do this without throwing away the whole of databinding by subclassing an exisiting `BindingCollectionAdapter`. You can then bind `adapter` in your layout to your subclass's class name to have it use that instead. Instead of overriding the nomral adapter methods, you should override `onCreateBinding()` or `onBindBinding()` and call `super` allowing you to run code before and after those events and get access to the item view's binding.

```java
public class MyRecyclerViewAdapter<T> extends BindingRecyclerViewAdapter<T> {
    public LoggingRecyclerViewAdapter(@NonNull ItemViewArg<T> arg) {
      super(arg);
    }
    
    public LoggingRecyclerViewAdapter(@NonNull ItemView itemView) {
        super(itemView);
    }

    public LoggingRecyclerViewAdapter(@NonNull ItemViewSelector<T> selector) {
        super(selector);
    }

    @Override
    public ViewDataBinding onCreateBinding(LayoutInflater inflater, @LayoutRes int layoutId, ViewGroup viewGroup) {
        ViewDataBinding binding = super.onCreateBinding(inflater, layoutId, viewGroup);
        Log.d(TAG, "created binding: " + binding);
        return binding;
    }

    @Override
    public void onBindBinding(ViewDataBinding binding, int bindingVariable, @LayoutRes int layoutRes, int position, T item) {
        super.onBindBinding(binding, bindingVariable, layoutRes, position, item);
        Log.d(TAG, "bound binding: " + binding + " at position: " + position);
    }
}
```

```xml
<android.support.v7.widget.RecyclerView
  android:layout_width="match_parent"
  android:layout_height="match_parent"
  app:layoutManager="{@LayoutManagers.linear()}"
  app:items="@{viewModel.items}"
  app:itemView="@{viewModel.itemView}"
  app:adapter='@{"com.example.MyRecyclerViewAdapter"}'/>
```

You can also use a factory instead of the class name. This allows you to not have reflection and gives you more control over it's construction.

```java
public static final BindingRecyclerViewAdapterFactory MY_FACTORY = new BindingRecyclerViewAdapterFactory() {
  @Override
  public <T> BindingRecyclerViewAdapter<T> create(RecyclerView recyclerView, ItemViewArg<T> arg) {
    return new MyRecyclerViewAdapter<>(arg);
  }
}
```

```xml
<android.support.v7.widget.RecyclerView
  android:layout_width="match_parent"
  android:layout_height="match_parent"
  app:layoutManager="{@LayoutManagers.linear()}"
  app:items="@{viewModel.items}"
  app:itemView="@{viewModel.itemView}"
  app:adapter="@{MY_FACTORY}"/>
```

## Known Issues

### Cannot Resolve the libraries `@BindingAdapter`'s

This is likely because you are using the [android-apt](https://bitbucket.org/hvisser/android-apt) plugin which [broke](https://bitbucket.org/hvisser/android-apt/issues/45/breaks-declaring-bindingadapter-in-a) this in previous versions. Update to `1.6+` to fix it.

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
