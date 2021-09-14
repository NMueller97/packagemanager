package de.salocin.utils

import javafx.beans.binding.ObjectBinding
import javafx.beans.value.ObservableValue

class SelectionBinding<S : Any?, T : Any?, V : ObservableValue<T>>(
    private val source: ObservableValue<S>,
    private val selector: (S) -> V
) : ObjectBinding<T>() {

    private var currentSelectedObservable: ObservableValue<T> = selector(source.value)

    init {
        bind(source)
        bind(currentSelectedObservable)
    }

    override fun onInvalidating() {
        unbind(currentSelectedObservable)
        currentSelectedObservable = selector(source.value)
        bind(currentSelectedObservable)
    }

    override fun computeValue(): T = currentSelectedObservable.value
}
