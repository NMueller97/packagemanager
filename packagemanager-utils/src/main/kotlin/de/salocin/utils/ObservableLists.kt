package de.salocin.utils

import javafx.beans.binding.Bindings
import javafx.collections.FXCollections
import javafx.collections.ObservableList

/**
 * Converts a [List] to an [ObservableList] of type [T].
 */
fun <T> List<T>.observable() = FXCollections.observableArrayList(this)

/**
 * Converts a [List] to an [ObservableList] of type [T].
 */
fun <T> List<T>?.nullSafeObservable() =
    FXCollections.observableArrayList(this) ?: FXCollections.emptyObservableList()

/**
 * Updates this list whenever the [source] list changes.
 */
fun <T> MutableList<T>.bind(source: ObservableList<T>) =
    Bindings.bindContent(this, source)

/**
 * Updates this list whenever the [source] list changes, converting values from the source type [S]
 * to the target type [T] using the specified [mapper].
 */
fun <S, T> MutableList<T>.bindMapped(source: ObservableList<S>, mapper: (S) -> T) =
    ListMappedBinding(source, this, mapper)
