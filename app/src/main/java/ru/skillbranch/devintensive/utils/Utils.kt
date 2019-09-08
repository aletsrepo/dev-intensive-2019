package ru.skillbranch.devintensive.utils

object Utils {

    private val transliterationMap = mapOf(
        'а' to "a", 'б' to "b", 'в' to "v", 'г' to "g", 'д' to "d",
        'е' to "e", 'ё' to "e", 'ж' to "zh", 'з' to "z", 'и' to "i",
        'й' to "i", 'к' to "k", 'л' to "l", 'м' to "m", 'н' to "n",
        'о' to "o", 'п' to "p", 'р' to "r", 'с' to "s", 'т' to "t",
        'у' to "u", 'ф' to "f", 'х' to "h", 'ц' to "c", 'ч' to "ch",
        'ш' to "sh", 'щ' to "sh'", 'ъ' to "", 'ы' to "i", 'ь' to "",
        'э' to "e", 'ю' to "yu", 'я' to "ya"
    )

    fun parseFullName(userName: String?): Pair<String?, String?> {
        val fullName: List<String>? =
            userName?.split(" ")?.filter { s: String -> s.isNotEmpty() }

        val firstName: String? = fullName?.getOrNull(0)
        val lastName: String? = fullName?.getOrNull(1)

        return Pair(firstName, lastName)
    }

    fun toInitials(firstName: String?, lastName: String?): String? {
        val first = firstName?.trim()?.getOrNull(0)?.toUpperCase() ?: ""
        val second = lastName?.trim()?.getOrNull(0)?.toUpperCase() ?: ""
        return if (first == "" && second == "") null else "$first$second"
    }

    fun transliteration(payload: String, divider: String = " "): String {
        if (payload.isEmpty()) return ""
        var result = ""
        val words = payload.trim().split(" ")
        for ((count, word) in words.withIndex()) {
            word.forEach { c ->
                result += if (c.isLowerCase())
                    transliterationMap[c] ?: c
                else
                    transliterationMap[c.toLowerCase()]?.capitalize() ?: c
            }
            if (count < words.size - 1) {
                result += divider
            }
        }
        return result
    }
}
