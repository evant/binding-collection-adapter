### 0.8
- Reverted changes to 'BindingRecyclerViewAdapter.getItemViewType()` because it actually causes it
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
