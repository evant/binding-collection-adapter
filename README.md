# BindingCollectionAdapter
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/me.tatarka.bindingcollectionadapter2/bindingcollectionadapter/badge.svg?style=flat)](https://maven-badges.herokuapp.com/maven-central/me.tatarka.bindingcollectionadapter2/bindingcollectionadapter)

Easy way to bind collections to listviews and recyclerviews with the new [Android Data Binding framework](https://developer.android.com/tools/data-binding/guide.html).

## Download

If you are using androidx use version `4.0.0`, this also uses databinding v2

```groovy
implementation 'me.tatarka.bindingcollectionadapter2:bindingcollectionadapter:4.0.0'
implementation 'me.tatarka.bindingcollectionadapter2:bindingcollectionadapter-recyclerview:4.0.0'
implementation 'me.tatarka.bindingcollectionadapter2:bindingcollectionadapter-viewpager2:4.0.0'
```

or use the previous stable version

```groovy
implementation 'me.tatarka.bindingcollectionadapter2:bindingcollectionadapter:2.2.0'
implementation 'me.tatarka.bindingcollectionadapter2:bindingcollectionadapter-recyclerview:2.2.0'
```

## Usage

You need to provide your items and an `ItemBinding` to bind to the layout. You should use an
`ObservableList` to automatically update your view based on list changes. However, you can
use any `List` if you don't need that functionality.

```java
public class ViewModel {
  public final ObservableList<String> items = new ObservableArrayList<>();
  public final ItemBinding<String> itemBinding = ItemBinding.of(BR.item, R.layout.item);
}
```

Then bind it to the collection view with `app:items` and `app:itemBinding`.

```xml
<!-- layout.xml -->
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto">
    <data>
      <import type="com.example.R" />
      <variable name="viewModel" type="com.example.ViewModel"/>
    </data>

    <ListView
      android:layout_width="match_parent"
      android:layout_height="match_parent"
      app:items="@{viewModel.items}"
      app:itemBinding="@{viewModel.itemBinding}"/>

    <androidx.recyclerview.widget.RecyclerView
      android:layout_width="match_parent"
      android:layout_height="match_parent"
      app:layoutManager="androidx.recyclerview.widget.LinearLayoutManager"
      app:items="@{viewModel.items}"
      app:itemBinding="@{viewModel.itemBinding}"/>

    <androidx.viewpager.widget.ViewPager
      android:layout_width="match_parent"
      android:layout_height="match_parent"
      app:items="@{viewModel.items}"
      app:itemBinding="@{viewModel.itemBinding}"/>

    <Spinner
      android:layout_width="match_parent"
      android:layout_height="match_parent"
      app:items="@{viewModel.items}"
      app:itemBinding="@{viewModel.itemBinding}"
      app:itemDropDownLayout="@{R.layout.item_dropdown}"/>
</layout>
```

In your item layout, the collection item will be bound to the variable with the
name you passed into the `ItemBinding`.

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

Note: if `app:itemBinding` is null, then the adapter will be set to null. This is useful if you
don't have an `itemBinding` right away (ex: need to wait till you load data). If you aren't seeing
any views, make sure you have `itemBinding` defined!

## Multiple View Types

You can use multiple view types by using `OnItemBind` instead. You can still bind
it to the view with `app:itemBinding`.

```java
public final OnItemBind<String> onItemBind = new OnItemBind<String>() {
  @Override
  public void onItemBind(ItemBinding itemBinding, int position, String item) {
    itemBinding.set(BR.item, position == 0 ? R.layout.item_header : R.layout.item);
  }
};
```

If you are binding to a ListView, you must also provide the number of item types you have with
`app:itemTypeCount="@{2}`.

Note that `onItemBind` is called many times so you should not do any complex processing in there. If 
you don't need to bind an item at a specific position (a static footer for example) you can use
`ItemBinding.VAR_NONE` as the variable id.

## Bind Extra Variables

You can bind additional variables to items in the list with `itemBinding.bindExtra(BR.extra, value)`.
This is useful for components that you don't want the items themselves to care about. For example, 
you can implement an item click listener as such

```java
public interface OnItemClickListener {
    void onItemClick(String item);
}

OnItemClickListener listener = ...;
ItemBinding<Item> itemBinding = ItemBinding.<Item>of(BR.item, R.layout.item)
    .bindExtra(BR.listener, listener);
```

```xml
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto">
    <data>
      <variable name="item" type="String"/>
      <variable name="listener" type="OnItemClickListener"/>
    </data>

    <TextView
      android:id="@+id/text"
      android:layout_width="match_parent"
      android:layout_height="wrap_content"
      android:onClick="@{() -> listener.onItemClick(item)}"
      android:text="@{item}"/>
</layout>
```

## Additional Adapter Configuration

### ListView

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
Setting this will make `hasStableIds` return true which can increase performance of data changes.

You can set a callback for `isEnabled()` as well with
```java
adapter.setItemEnabled(new BindingListViewAdapter.ItemEnabled<T>() {
  @Override
  public boolean isEnabled(int position, T item) {
    return // Calculate if item is enabled.
  }
});
```
or by defining `app:itemEnabled="@{itemEnabled}"`in the `ListView` in you layout file.

### ViewPager

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

### RecyclerView

You can construct custom view holders with

```java
adapter.setViewHolderFactory(new ViewHolderFactory() {
  @Override
  public RecyclerView.ViewHolder createViewHolder(ViewDataBinding binding) {
    return new MyCustomViewHolder(binding.getRoot());
  }
});
```
or by defining `app:viewHolder="@{viewHolderFactory}"` in the `RecyclerView` in your layout file.

## Directly manipulating views

Data binding is awesome and all, but you may run into a case where you simply need to manipulate the
views directly. You can do this without throwing away the whole of databinding by subclassing an
existing `BindingCollectionAdapter`. You can then bind `adapter` in your layout to your subclass's
class name to have it use that instead. Instead of overriding the normal adapter methods, you should
override `onCreateBinding()` or `onBindBinding()` and call `super` allowing you to run code before
and after those events and get access to the item view's binding.

```java
public class MyRecyclerViewAdapter<T> extends BindingRecyclerViewAdapter<T> {

  @Override
  public ViewDataBinding onCreateBinding(LayoutInflater inflater, @LayoutRes int layoutId, ViewGroup viewGroup) {
    ViewDataBinding binding = super.onCreateBinding(inflater, layoutId, viewGroup);
    Log.d(TAG, "created binding: " + binding);
    return binding;
  }

  @Override
  public void onBindBinding(ViewDataBinding binding, int bindingVariable, @LayoutRes int layoutId, int position, T item) {
    super.onBindBinding(binding, bindingVariable, layoutId, position, item);
    Log.d(TAG, "bound binding: " + binding + " at position: " + position);
  }
}
```

```xml
<androidx.recyclerview.widget.RecyclerView
  android:layout_width="match_parent"
  android:layout_height="match_parent"
  app:layoutManager="androidx.recyclerview.widget.LinearLayoutManager"
  app:items="@{viewModel.items}"
  app:itemBinding="@{viewModel.itemBinding}"
  app:adapter="@{viewModel.adapter}"/>
```

Note: databinding will re-evaluate expressions in your layout each time there is a data source 
change. If you are using a custom adapter you should ensure you are returning the same instance each 
time or your scroll position etc will not be preserved.


## OnItemBind helpers

There are a few classes to help with common implementations of `OnItemBind`.

`OnItemBindClass` binds an item based on the class of the item in the list.

```java
itemBind = new OnItemBindClass<>()
  .map(String.class, BR.name, R.layout.item_name)
  .map(Footer.class, ItemBinding.VAR_NONE, R.layout.item_footer)
  .map(Item.class, new OnItemBind<Item>() {
                       @Override
                       public void onItemBind(ItemBinding itemBinding, int position, Item item) {
                         itemBinding.clearExtras()
                                    .set(BR.item, position == 0 ? R.layout.item_header : R.layout.item)
                                    .bindExtra(BR.extra, (list.size() - 1) == position);
                       }
                     })
  .map(Object.class, ItemBinding.VAR_NONE, R.layout.item_other);
```

`OnItemBindModel` delegates to the items in the list themselves to determine the binding.

```java
itemBind = new OnItemBindModel<Model>();

public class Model implements ItemBindingModel {
  @Override
  public void onItemBind(ItemBinding itemBinding) {
    itemBinding.set(BR.name, R.layout.item_name);
  }
}
```

## MergeObservableList

There are many times you want to merge multiple data sources together. This can be as simple as
adding headers and footers or as complex as concatenating multiple data sources. It is hard to
manage these lists yourself since you have to take into account all items when updating a subset.

`MergeObservableList` solves this by giving you a "merged" view of your data sources.

```java
ObservableList<String> data = new ObservableArrayList<>();
MergeObservableList<String> list = new MergeObservableList<>()
  .insertItem("Header")
  .insertList(data)
  .insertItem("Footer");

data.addAll(Arrays.asList("One", "Two"));
// list => ["Header", "One", "Two", "Footer"]
data.remove("One");
// list => ["Header", "Two", "Footer"]
```

## DiffObservableList

Say you want to update list 'a' to list 'b' and you don't want to calculate what has changed between
the two manually.

`DiffObservableList` builds off of [DiffUtil](https://developer.android.com/reference/android/support/v7/util/DiffUtil.html)
to automatically calculate the changes between two lists.

```java
DiffObservableList<Item> list = new DiffObservableList(new DiffObservableList.Callback<Item>() {
    @Override
    public boolean areItemsTheSame(Item oldItem, Item newItem) {
        return oldItem.id.equals(newItem.id);
    }

    @Override
    public boolean areContentsTheSame(Item oldItem, Item newItem) {
        return oldItem.value.equals(newItem.value);
    }
});

list.update(Arrays.asList(new Item("1", "a"), new Item("2", "b1")));
list.update(Arrays.asList(new Item("2", "b2"), new Item("3", "c"), new Item("4", "d"));
```

With large lists diffing might be too costly to run on the main thread. In that case you can
calculate the diff on a background thread.

```java
DiffObservableList<Item> list = new DiffObservableList(...);

// On background thread:
DiffUtil.DiffResult diffResult = list.calculateDiff(newItems);

// On main thread:
list.update(newItems, diffResult);
```

## Paging

Paging is supported through the `bindingcollectionadapter-paging` artifact. First add it to your project

```groovy
implementation 'me.tatarka.bindingcollectionadapter2:bindingcollectionadapter-paging:3.1.1'
```

Then bind a `PagedList` and `DiffUtil.ItemCallback`.

```java
LiveData<PagedList<Item>> pagedList = new LivePagedListBuilder<>(..., 20);
DiffUtil.ItemCallback<Item> diff = ...;
```

```xml
<androidx.recyclerview.widget.RecyclerView
  android:layout_width="match_parent"
  android:layout_height="match_parent"
  app:layoutManager="androidx.recyclerview.widget.LinearLayoutManager"
  app:items="@{pagedList}"
  app:itemBinding="@{itemBinding}"
  app:diffConfig="@{diff}" />
```

## Known Issues

### Cannot Resolve the libraries `@BindingAdapter`'s

This is likely because you are using the [android-apt](https://bitbucket.org/hvisser/android-apt)
plugin which [broke](https://bitbucket.org/hvisser/android-apt/issues/45/breaks-declaring-bindingadapter-in-a)
this in previous versions. Update to `1.6+` to fix it.

### View's adapter is null

If you attempt to retrieve an adapter from a view right after binding it you may find it is null.
This is because databinding waits for the next draw pass to run to batch up changes. You can force
it to run immediately by calling `binding.executePendingBindings()`.

### LiveData not working

Live data support has been added in `2.3.0-beta3` and `3.0.0-beta3` (androidx). For most cases it
should 'just work'. However, it uses a bit of reflection under the hood and you'll have to call
`adapter.setLifecycleOwner(owner)` if your containing view does not use databinding. This will be
fixed whenever [this issue](https://issuetracker.google.com/issues/112929938) gets resolved.

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
