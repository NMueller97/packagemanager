package de.salocin.util

import javafx.beans.binding.*
import javafx.beans.value.ObservableValue


/**
 * Creates a [StringBinding] to the [mapper] function which will take value of type [S] and return a [String].
 */
fun <S> ObservableValue<S>.mapString(mapper: (S) -> String): StringBinding =
    Bindings.createStringBinding({ mapper(value) }, this)

/**
 * Creates an [IntegerBinding] to the [mapper] function which will take value of type [S] and return a [Integer].
 */
fun <S> ObservableValue<S>.mapInt(mapper: (S) -> Int): IntegerBinding =
    Bindings.createIntegerBinding({ mapper(value) }, this)

/**
 * Creates a [Binding] to the  [mapper] function which will take value of type [S] and return a value of type [T].
 */
fun <S, T> ObservableValue<S>.map(mapper: (S) -> T): ObjectBinding<T> =
    Bindings.createObjectBinding({ mapper(value) }, this)

/**
 * A binding to a constant value that never changes.
 */
fun constantIntBinding(constant: Int): IntegerBinding = Bindings.createIntegerBinding({ constant })

/**
 * A binding to a constant value that never changes.
 */
fun constantBooleanBinding(constant: Boolean): BooleanBinding =
    Bindings.createBooleanBinding({ constant })
