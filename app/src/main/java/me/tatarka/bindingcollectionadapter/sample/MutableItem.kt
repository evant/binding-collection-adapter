package me.tatarka.bindingcollectionadapter.sample

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class MutableItem(val index: Int, var checkable: Boolean = false) {
    private val mutChecked = MutableLiveData<Boolean>().apply { value = false }
    val checked: LiveData<Boolean> = mutChecked

    fun setChecked(value: Boolean) {
        if (!checkable) {
            return
        }
        mutChecked.value = value
    }

    @MainThread
    fun onToggleChecked(): Boolean {
        if (!checkable) {
            return false
        }
        mutChecked.value = !(mutChecked.value)!!
        return true
    }
}
