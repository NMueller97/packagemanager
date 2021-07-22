package de.salocin.parser

interface LineOutputParser<T> {

    fun parseLine(line: String): T?

    fun <V> mapEachLineTo(block: (T) -> V): LineOutputParser<V> = ParserMapping(this, block)
}
