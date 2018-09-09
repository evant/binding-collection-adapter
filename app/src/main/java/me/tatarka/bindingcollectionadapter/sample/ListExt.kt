package me.tatarka.bindingcollectionadapter.sample

fun <T> List<T>.replaceAt(index: Int, replace: (T) -> T): List<T> = ArrayList(this).apply {
    val item = this[index]
    this[index] = replace(item)
}
