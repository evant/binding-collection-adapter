package me.tatarka.bindingcollectionadapter.sample

interface ImmutableListeners : Listeners {
    fun onToggleChecked(index: Int): Boolean
}
