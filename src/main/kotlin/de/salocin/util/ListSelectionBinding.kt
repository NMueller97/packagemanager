package de.salocin.util

import javafx.beans.binding.ListBinding
import javafx.beans.value.ObservableValue
import javafx.collections.ObservableList

class ListSelectionBinding<S, T>(
    private val source: ObservableValue<S>,
    private val selector: (S) -> ObservableList<T>
) : ListBinding<T>() {

    init {
        bind(source)
        source.addListener { _, _, newValue ->
            selector(newValue)
        }
    }

    override fun computeValue(): ObservableList<T> = selector(source.value)
}
