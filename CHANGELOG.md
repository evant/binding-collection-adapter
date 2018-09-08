### Unreleased
- Removed `LayoutManagers`. RecyclerView has support setting the LayoutManager in xml using it's
class name that doesn't rely on databinding.
- Added `AsyncDiffObservableList` which is based on `AsyncListDiffer`. You should prefer this over
`DiffObservableList` as it handles running the diff in a background thread for you.
- Added a `app:diffConfig` binding for `RecyclerView`. If set, changes to the list will 
automatically be diffed using `DiffObservableList`. This works nicely with `LiveData<List<Item>>`.

### 3.0.0-beta3
- Compile with AGP `3.2.0-rc02` for better compatibility.

### 3.0.0-beta2
- LiveData support. This currently has a limitation that you need to call `setLifecycleOwner` on the
adapter if the host view is not using databinding.

### 3.0.0-beta1
- Converted to use androidx, requires databinding v2.

### 2.3.0-beta3
- Compile with AGP `3.2.0-rc02` for better compatibility.

### 2.3.0-beta2
- LiveData support. This currently has a limitation that you need to call `setLifecycleOwner` on the
adapter if the host view is not using databinding.

### 2.3.0-beta1
- Compiled with [databinding v2](https://developer.android.com/topic/libraries/data-binding/start#preview-compiler).

### 2.2.0
- Added `MergeObservableList#removeAll()`.
- Better method for getting the BR variable id in error messages.

### 2.1.0
- Easily allow extras in `OnItemBindClass`.

### 2.0.1
- Fixed package of DiffObservableList

### 2.0.0
- Changed package name to not conflict with 1.x
- Fixed potential synchronization issue in DiffObservableList

### 2.0.0-beta3
- Fixed crash when binding a ListView with headers and/or footers.

### 2.0.0-beta2
-  Added DiffObservableList. This wraps DiffUtil allowing you to easily update your list with a new
version and it will automatically find the change updates for animation.

### 2.0.0-beta1
This is a complete refactor to simplify and improve naming of components.
- Replaced `ItemView`, `ItemViewSelector`, and `ItemViewArg` with `ItemBinding` and `OnItemBind`.
Everything is just an `ItemBinding` so 2 different classes don't have to be juggled. You can pass an
`OnItemBind` callback into an `ItemBinding` for dynamic selection. There is a convenience conversion
so you can just provide an `OnItemBind` to `app:itemBinding` in your layout.
- `ItemView.BINDING_VARIABLE_NONE` is now `ItemBinding.VAR_NONE`. Before, forgetting this would lead
to subtle bugs.
- An exception will be thrown if nothing is set on an itemBinding in `onItemBind`.
- Knowledge of `itemTypeCount` is only present in `BindingListViewAdapter`. You can pass it to the
constructor or set it with `app:itemTypeCount="@{count}"` in your layout.
- `setItemBinding()` is added to the `BindingCollectionAdapter` instead of it being a required
constructor arg.
- Factories for custom adapters are removed, just pass in the instance directly to `app:adapter`.
- Dynamically changing a bound adapter is supported. Note: this means that if it is not intended to
change, you must provide the same instance each time.
- Constructing adapters from a class name is no longer supported.
-
```java
selector = ItemViewClassSelector.builder()
  .put(String.class, BR.name, R.layout.item_name)
  .put(Footer.class, ItemView.BINDING_VARIABLE_NONE, R.layout.item_footer)
  .build();
```
is now
```java
itemBind = new OnItemBindClass<>()
  .map(String.class, BR.name, R.layout.item_name)
  .map(Footer.class, ItemBinding.VAR_NONE, R.layout.item_footer);
```
-
```java
selector = new ItemViewModelSelector<Model>();

public class Model implements ItemViewModel {
  @Override
  public void itemView(ItemView itemView) {
    itemView.set(BR.name, R.layout.item_name);
  }
}
```
is now
```java
itemBind = new OnItemBindModel<Model>();

public class Model implements ItemBindingModel {
  @Override
  public void onItemBind(ItemBinding itemBinding) {
    itemBinding.set(BR.name, R.layout.item_name);
  }
}
```

### 1.3.0
- Added way to construct your own custom view holder for a RecyclerView adapter.

### 1.2.0
- Updated databinding dependency to work around some of it's bugs.

### 1.2.0-beta1
- Added some utilities to help work with more complex item views and data sources.
  * MergeObservableList: create a 'merged' view of multiple ObservableLists and single items. Any
    changes to a backing list will be reflected in the merge one. Useful for headers, footers, and
    concatenating data sources.
  * ItemViewClassSelector: build an ItemViewSelector based on the classes of the items. Useful if
    you find yourself doing a bunch of instanceof checks in an ItemSelector.
  * ItemViewModelSelector: a selector that selects item views using list items that implement the
    ItemViewModel interface.
- Fixed bug where BindingRecyclerViewAdapter would not register for list changes if the items are
  set on the adapter before the adapter is set on the recycler view.
- Items are now set on adapters before they are added to the collection view. This should make it
  more likely to restore state (ex: scroll position) if the items are already populated when bound.

### 1.1.0
-  Better support for recyclerview item changes. This means better handling of layout changes and
item animations. Source: https://realm.io/news/data-binding-android-boyar-mount/

### 1.0.1
- Disabled support for dynamic item views. This was causing unnecessary adapter replacement. It may
 be added back in a later version if it can be done correctly.

### 1.0.0
- Officially stable release! The won't be any backwards-incompatible changes without a major version
bump.
- Changed group to `me.tatarka.bindingcolectionadapter`.
- Added `getItemViewArg()` to `BindingCollectionAdapter` so you can easily get the arg it was
constructed with.
- Added `ItemIsEnabled` to `BindingListViewAdapter` to easily configure the behavior of
`isEnabled(int)`. You can set with `setItemIsEnabled()` in code or `itemIsEnabled` in your layout.
- Support dynamically changing the `itemView` in a binding. This will replace the entire adapter.

### 0.16
- Updated to latest databinding, requites android plugin `1.5.0`
- Use `requireAll = false` to vastly simplify binding adapters.
- Fail with a more helpful error message when `itemView` is null.
- Removed deprecated `ItemView` methods.
- Invalid binding variable error can now show it's name even if the app's package and the BR package
do not match.

### 0.15
- Deprecated `ItemView.getBindingVariable()` and `ItemView.getLayoutRes()` in favor of
`ItemView.bindingVariable()` and `ItemView.layoutRes()`.
- Added public accessor methods to `ItemViewArg` to select and get item view info.
- Removed unused constant in `BindingListViewAdapter`
- Fixed `BindingViewPagerAdapter` possibly using the wrong binding variable in `onBindBinding()`.

### 0.14
- Removed deprecated `BindingCollectionAdapter.setItems(Collection<T>)`.
- Removed deprecated constructors on adapters.
- Removed deprecated methods on `ItemView`.
- Removed some more deprecated items in `BindingCollectionAdapters`.
- Removed ClassNameBindingCollectionAdapterFactories, though you can still use the class name in
binding.
- Split out RecyclerView bindings into a separate dependency. If you are using RecyclerView, you
should also include `compile 'me.tatarka:bindingcollectionadpater-recyclerview:0.14'`.

### 0.13
- Update databinding to rc2.
- Simplified and re-ordered BindingAdapters
- Removed support for modifying an `ObservableList` off the main thread. It will now throw an
exception.
- Removed deprecated `BindingCollectionAdapter.getItems()`.
- Removed a bunch of deprecated methods on `BindingCollectionAdapters` that should shouldn't be
using anyway and deprecated some more.
- Added `BindingCollectionAdapter.setItems(List<T>)` and deprecated
`BindingCollectionAdapter.setItems(Collection<T>)`.
**Warning!** This may cause a subtile change in behavior. The collection now always holds a
reference to the collection you pass it instead of copying it into it's own. You must now be careful
to only modify this collection on the main thread and call `notifyDataSetChanged()` or related if
you are not using an `ObservableList`.
- Added support for itemId on RecyclerView.

### 0.12
- Work around for `@BindingConversion` generics issue, fixes ItemViewSelector.
- Added support for specifying adapters with factories instead of a class name.

### 0.11
- Change ListView BindingAdapters to AdapterView to directly support spinners.
- Fixed crash when `getViewTypeCount()` is not called before `getItemViewType()`.
- Unified ItemView and ItemViewSelector
A new constructor that takes an ItemViewArg simplifies databinding since
you don't need two seperate methods for ItemView and ItemViewSelector.
a couple of BindingConversions keeps this backwards-compatible.

Note: If you are using a custom BindingCollectionAdapter, you should add
a cosntructor to create it with an ItemViewArg (the common case would
just be to call super). There is currently a fallback so that this
change is backwards-compatible but it will be removed in a future
update.
- Deprecated `itemView.setLayoutRes(DROP_DOWN_LAYOUT, int)` in favor of `app:dropDownItemView` or
`adapter.setDropDownItemView(itemViw)`. Original functionality is completely removed but that
shouldn't matter since you couldn't bind the adapter to the spinner anyway.
- Added warning for changing list off the main thread. This future doesn't carry it's weight and
will be removed from a future version.

### 0.10
- Deprecated `BindingCollectionAdapter.getItems()` and added
`BindingCollectionAdapter.getAdapterItem(int)`. See javadoc for reasoning.
- Show the binding variable name instead of int value when it fails to bind an item.

### 0.9
- Bumped data-binding dep to `1.0-rc1`.

### 0.8
- Reverted changes to `BindingRecyclerViewAdapter.getItemViewType()` because it actually causes it
to work incorrectly.

### 0.7
- Fixed bug when creating a custom adapter with an `ItemViewSelector`
- Changed `BindingRecyclerViewAdapter.getItemViewType()` to be safer to override.

### 0.6
- Changed the adapter subclass callback methods to `onCreateBinding()` and `onBindBinding()` to
give you more control over the superclass implementations.

### 0.5
- You now need to subclass the adapters to access the views, this simpilifes things as you don't
have to worry about setting the listener after it's been called. There is an additional binding
'adapter' that you can set to bind to your subclass.

- Changed binding adapters to use multiple variables instead of tags, makes them easier to
understand. If you copied the binding adapters yourself to get around apt plugin bug, you'll have to
redo that.

### 0.4
- Minor refactoring in how the binding adapters are created.

### 0.3
- Fail fast when a variable cannot be bound to a layout.
- Add CollectionBindingListener which allows you to get access to item binding and manipulate it
when it's created and when it's bound.

### 0.2
- Allow changes to the ObservableList to happen in a seperate thread.

### 0.1
- Initial Release.
