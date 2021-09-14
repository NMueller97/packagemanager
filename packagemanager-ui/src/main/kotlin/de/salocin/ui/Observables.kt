package de.salocin.ui

import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.ObservableValue
import javafx.collections.FXCollections
import javafx.collections.ObservableList

fun <T : Any?> T.observable(): ObservableValue<T> = SimpleObjectProperty(this)

fun <T : Any?> T.observableList(): ObservableList<T> = FXCollections.singletonObservableList(this)

fun <T : Any?> List<T>.observableList(): ObservableList<T> = FXCollections.observableList(this)
