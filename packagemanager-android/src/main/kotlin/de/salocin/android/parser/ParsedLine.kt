package de.salocin.android.parser

@JvmInline
value class ParsedLine(val value: List<String>) {

    operator fun get(index: Int) = value[index]

    operator fun contains(element: String) = element in value
}
