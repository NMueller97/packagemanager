package de.salocin.android.parser

object PlainOutputParser : LineOutputParser<String> {

    override fun parseLine(line: String): String = line
}
