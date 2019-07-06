package ru.skillbranch.devintensive.extensions

import java.util.regex.Pattern

fun String.truncate(length: Int = 16): String {
    return if (this.trim().length > length) {
        this.trimStart().dropLast(this.trim().length - length).trimEnd().plus("...")
    } else {
        this.trim()
    }
}

fun String.stripHtml(): String {
    var str = this.replace(Pattern.compile("<.*?>|&.[a-zA-Z0-9][^\\s]*?;").toRegex(), "")
    while (str.contains("  ")) {
        str = str.replace("  ", " ")
    }
    return str
}
