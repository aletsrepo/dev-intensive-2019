package ru.skillbranch.devintensive.models

class UserView(
    id: String?,
    fullname: String?,
    nickname: String?,
    avatar: String? = null,
    status: String? = "offline",
    initials: String?
)
