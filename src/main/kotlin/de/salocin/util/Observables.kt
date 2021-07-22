package de.salocin.util

import javafx.beans.binding.Bindings
import javafx.beans.value.ObservableBooleanValue
import javafx.beans.value.ObservableValue
import javafx.collections.ObservableList
import tornadofx.bind

fun <T> ObservableValue<T>.nullable(): ObservableValue<T?> =
    Bindings.createObjectBinding({ value }, this)

fun <T> ObservableValue<T?>.assertNonNull(): ObservableValue<T> =
    Bindings.createObjectBinding({ value ?: throw NullPointerException("value") }, this)

fun <T> ObservableValue<T?>.computeIfNull(block: () -> T): ObservableValue<T> =
    Bindings.createObjectBinding({ value ?: block() }, this)

fun <T> ObservableValue<T>.condition(block: (T) -> Boolean): ObservableBooleanValue =
    Bindings.createBooleanBinding({ block(value) }, this)

fun <T, V> ObservableValue<T>.selectList(block: (T) -> ObservableList<V>): ObservableList<V> =
    ListSelectionBinding(this, block)

fun <T> ObservableList<T>.bind(other: ObservableList<T>) = bind(other) { it }

