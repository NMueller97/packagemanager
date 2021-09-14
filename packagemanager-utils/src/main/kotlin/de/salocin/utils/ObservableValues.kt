package de.salocin.utils

import javafx.beans.binding.*
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.value.ObservableValue
import javafx.collections.ObservableList
import kotlin.reflect.KProperty

/**
 * Converts an [Int] to a non-modifiable [IntegerExpression].
 */
fun Int.observable(): IntegerExpression = SimpleIntegerProperty(this)

/**
 * Converts a [String] to a non-modifiable [StringExpression].
 */
fun String.observable(): StringExpression = SimpleStringProperty(this)

/**
 * Converts a [String] to a non-modifiable [StringExpression].
 */
fun String?.nullSafeObservable(): StringExpression = SimpleStringProperty(this ?: "")

fun <T : Any?> T.observable(): ObjectExpression<T> = SimpleObjectProperty(this)

/**
 * Expresses that a value wrapped inside an [ObservableValue] may be null.
 */
fun <T> ObservableValue<T>.nullable(): ObjectBinding<T?> =
    Bindings.createObjectBinding({ value }, this)

/**
 * Asserts that a value wrapped inside an [ObservableValue] may not be null.
 */
fun <T> ObservableValue<T?>.assertNonNull(): ObjectBinding<T> =
    Bindings.createObjectBinding({ value ?: throw NullPointerException("value") }, this)

/**
 * Computes a value if the value wrapped inside an [ObservableValue] is null, otherwise returns the
 * normal non-null value.
 */
fun <T> ObservableValue<T?>.computeIfNull(block: () -> T): ObjectBinding<T> =
    Bindings.createObjectBinding({ value ?: block() }, this)


/**
 * Converts an [ObservableValue] wrapping values of source type [S] to an [ObservableValue] wrapping
 * values of target type [T] using the supplied [converter].
 */
fun <S, T> ObservableValue<S>.convert(converter: (S) -> T): ObjectBinding<T> =
    Bindings.createObjectBinding({ converter(value) }, this)

/**
 * Converts an [Int] [ObservableValue] to an [IntegerBinding].
 */
fun ObservableValue<Int>.integerBinding(): IntegerBinding =
    Bindings.createIntegerBinding({ value }, this)

/**
 * Converts a [Boolean] [ObservableValue] to a [BooleanBinding].
 */
fun ObservableValue<Boolean>.booleanBinding(): BooleanBinding =
    Bindings.createBooleanBinding({ value }, this)

/**
 * Converts an [ObservableValue] to a [BooleanBinding].
 */
fun <T> ObservableValue<T>.condition(block: (T) -> Boolean): BooleanBinding =
    Bindings.createBooleanBinding({ block(value) }, this)

fun <S : Any, T, V : ObservableValue<T>> ObservableValue<S>.select(selector: (S) -> V): ObservableValue<T> =
    SelectionBinding(this, selector)

fun <S, T, V : ObservableValue<T>> ObservableValue<S?>.selectFromNullable(selector: (S?) -> V): ObservableValue<T?> =
    SelectionBinding(this, selector).nullable()

fun <S : Any, T, V : ObservableValue<T>> ObservableValue<S>.select(property: KProperty<V>): ObservableValue<T> =
    select { value -> property.call(value) }

fun <S, T, V : ObservableValue<T>> ObservableValue<S?>.selectFromNullable(property: KProperty<V>): ObservableValue<T?> =
    selectFromNullable { value ->
        if (value == null) {
            SimpleObjectProperty()
        } else {
            property.call(value)
        }
    }

/**
 * Selects an [ObservableList] based on a value wrapped inside this [ObservableValue] by the
 * supplied [selector].
 */
fun <T, V> ObservableValue<T>.selectList(selector: (T) -> ObservableList<V>): ObservableList<V> =
    ListSelectionBinding(this, selector)

