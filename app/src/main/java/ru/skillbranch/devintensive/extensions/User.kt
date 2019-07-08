package ru.skillbranch.devintensive.extensions

import ru.skillbranch.devintensive.models.User
import ru.skillbranch.devintensive.models.UserView
import ru.skillbranch.devintensive.utils.Utils

fun User.toUserView(): UserView {
    val nickname = Utils.transliteration("$firstName $lastName")
    val initials = Utils.toInitials(firstName, lastName)
    val status = if (lastVisit == null) "Ни разу не заходил" else if (isOnline) "онлайн" else "Последний раз ${lastVisit.humanizeDiff()}"

    return UserView(
        id,
        fullname = "$firstName $lastName",
        nickname = nickname,
        avatar = avatar,
        status = status,
        initials = initials
    )
}
