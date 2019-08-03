package ru.skillbranch.devintensive.models

import ru.skillbranch.devintensive.utils.Utils

data class Profile(
    val firstName: String,
    val lastName: String,
    val about: String,
    val repository: String,
    val rating: Int = 0,
    val respect: Int = 0
) {
    var nickname: String = getNick()
    val rank: String = "Junior Android Developer"

    private fun getNick(): String {
        val divider = "_"
        return if (firstName.isEmpty() && lastName.isEmpty()) ""
        else if (firstName.isEmpty()) Utils.transliteration(lastName)
        else if (lastName.isEmpty()) Utils.transliteration(firstName)
        else Utils.transliteration("$firstName$divider$lastName", divider)
    }

    fun toMap(): Map<String, Any> = mapOf(
        "nickName" to nickname,
        "rank" to rank,
        "firstName" to firstName,
        "lastName" to lastName,
        "about" to about,
        "repository" to repository,
        "rating" to rating,
        "respect" to respect
    )
}
