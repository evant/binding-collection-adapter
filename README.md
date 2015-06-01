# BindingCollectionAdapter
Easy way to bind collections to listviews and recyclerviews with the new [Android Data Binding framework](https://developer.android.com/tools/data-binding/guide.html).

```java
public class ViewModel {
  public final ObservableList<String> items = new ObservableArrayList<>();
  public final ItemView itemView = ItemView.of(BR.item, R.layout.item);
}
```

```xml
<!-- layout.xml -->
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto">
    <data>
      <variable name="viewModel" type="com.example.ViewModel"/> 
    </data>
    
    <ListView
      android:layout_width="match_parent"
      android:layout_height="match_parent"
      app:items="@{viewModel.items}"
      app:itemView="@{viewModel.itemView}"/>
</layout>
```

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
