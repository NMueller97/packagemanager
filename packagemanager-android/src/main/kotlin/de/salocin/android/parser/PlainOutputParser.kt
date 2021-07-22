package de.salocin.parser

object PlainOutputParser : LineOutputParser<String> {

    override fun parseLine(line: String): String = line
}
