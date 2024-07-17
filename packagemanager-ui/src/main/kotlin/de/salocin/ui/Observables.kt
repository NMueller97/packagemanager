package de.salocin.ui

import javafx.beans.binding.Binding
import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.ObservableDoubleValue
import javafx.beans.value.ObservableValue
import javafx.beans.value.WritableValue
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import kotlin.reflect.KProperty

inline fun <T : Any?, V : Any?> ObservableValue<T>.mapTo(crossinline block: (T) -> V): Binding<V> =
    Bindings.createObjectBinding({ block(value) }, this)

fun <T : Any?> T.observable(): ObservableValue<T> = SimpleObjectProperty(this)

fun <T : Any?> T.observableList(): ObservableList<T> = FXCollections.singletonObservableList(this)

fun <T : Any?> List<T>.observableList(): ObservableList<T> = FXCollections.observableList(this)

operator fun ObservableDoubleValue.getValue(thisRef: Any?, prop: KProperty<*>): Double = get()

operator fun <T : Any?> ObservableValue<T>.getValue(thisRef: Any?, prop: KProperty<*>): T = value

operator fun <T : Any?> WritableValue<T>.setValue(thisRef: Any?, prop: KProperty<*>, value: T) {
    this.value = value
}
