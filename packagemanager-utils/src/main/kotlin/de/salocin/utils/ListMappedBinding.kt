package de.salocin.utils

import javafx.beans.WeakListener
import javafx.collections.ListChangeListener
import javafx.collections.ObservableList
import java.lang.ref.WeakReference

class ListMappedBinding<S, T>(
    source: ObservableList<S>,
    target: MutableList<T>,
    private val mapper: (S) -> T
) : ListChangeListener<S>, WeakListener {

    private val weakReference: WeakReference<MutableList<T>> = WeakReference(target)

    init {
        source.addListener(this)
    }

    override fun onChanged(change: ListChangeListener.Change<out S>?) {
        val target = weakReference.get()

        if (change != null) {
            if (target == null) {
                change.list.removeListener(this)
            } else {
                while (change.next()) {
                    if (change.wasPermutated()) {
                        target.subList(change.from, change.to).clear()
                        target.addAll(
                            change.from,
                            change.list.subList(change.from, change.to).map(mapper)
                        )
                    } else {
                        if (change.wasRemoved()) {
                            target.subList(change.from, change.from + change.removedSize).clear()
                        }

                        if (change.wasAdded()) {
                            target.addAll(change.from, change.addedSubList.map(mapper))
                        }
                    }
                }
            }
        }
    }

    override fun wasGarbageCollected(): Boolean = weakReference.get() == null

    override fun hashCode(): Int {
        return weakReference.get()?.hashCode() ?: 0
    }

    override fun equals(other: Any?): Boolean {
        return when {
            this === other -> {
                true
            }
            wasGarbageCollected() -> {
                false
            }
            other is ListMappedBinding<*, *> -> {
                weakReference === other.weakReference
            }
            else -> {
                false
            }
        }
    }
}
