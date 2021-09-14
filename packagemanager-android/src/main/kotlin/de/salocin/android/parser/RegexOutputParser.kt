package de.salocin.android.parser

class RegexOutputParser(private val regex: Regex) : LineOutputParser<List<String>> {

    override fun parseLine(line: String) =
        regex.matchEntire(line)?.let { result -> result.groups.mapNotNull { it?.value } }

    fun takeGroup(group: Int): LineOutputParser<String> = mapEachLineTo { it[group] }

    fun takeGroups(vararg groups: Int): LineOutputParser<ParsedLine> =
        mapEachLineTo { matchedGroups ->
            ParsedLine(matchedGroups.filterIndexed { index, _ -> index in groups })
        }

    fun takeAllGroups(): LineOutputParser<ParsedLine> = mapEachLineTo { matchedGroups ->
        ParsedLine(matchedGroups.filterIndexed { index, _ -> index != 0 })
    }
}
