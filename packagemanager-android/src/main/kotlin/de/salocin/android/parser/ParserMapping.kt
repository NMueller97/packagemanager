package de.salocin.parser

class ParserMapping<S, T>(
    private val source: LineOutputParser<S>,
    private val mapping: (S) -> T
) : LineOutputParser<T> {

    override fun parseLine(line: String): T? = source.parseLine(line)?.let(mapping)
}
