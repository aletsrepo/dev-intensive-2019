package ru.skillbranch.devintensive.models

class Chat(
    id: String,
    members: MutableList<User> = mutableListOf(),
    messages: MutableList<BaseMessage> = mutableListOf()
) {

}
