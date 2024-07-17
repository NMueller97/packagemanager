package de.salocin.packagemanager.io

import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

data class OutputEncoding(
    val charset: Charset = StandardCharsets.UTF_8,
    val lineEnding: String = System.lineSeparator(),
)
