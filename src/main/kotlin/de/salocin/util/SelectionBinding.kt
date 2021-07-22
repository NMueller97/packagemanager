package de.salocin.util

import javafx.beans.binding.ObjectBinding
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.collections.FXCollections
import javafx.collections.ObservableList

class SelectionBinding<S, T>(
    private val source: ObservableValue<S>,
    private val selector: (S) -> ObservableValue<T>
) : ObjectBinding<T>() {

    private val sourceObserver = SourceObserver()
    private var currentSelection = selector(source.value)
    private var dependencies: ObservableList<ObservableValue<*>> =
        FXCollections.observableArrayList(source, currentSelection)

    init {
        source.addListener(sourceObserver)
    }

    override fun dispose() {
        super.dispose()
        source.removeListener(sourceObserver)
    }

    override fun computeValue(): T = currentSelection.value

    override fun getDependencies(): ObservableList<*> {
        return dependencies
    }

    inner class SourceObserver : ChangeListener<S> {

        override fun changed(observable: ObservableValue<out S>?, oldValue: S, newValue: S) {
            if (oldValue != newValue) {
                currentSelection = selector(newValue)
                dependencies.removeLast()
                dependencies.add(currentSelection)
                invalidate()
            }
        }

    }

}
