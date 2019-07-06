package ru.skillbranch.devintensive.models

import java.util.Date

abstract class BaseMessage(
    val id: String,
    val from: User?,
    val chat: Chat,
    val isIncoming: Boolean = false,
    val date: Date = Date()
) {
    /**
     * возвращает строку содержащюю информацию о
     * id сообщения,µ
     * имени получателя/отправителя,
     * виде сообщения ("получил/отправил") и
     * типе сообщения ("сообщение"/"изображение")
     */
    abstract fun formatMessage(): String

    companion object AbstractFactory {
        private var messageId: Int = -1
        fun makeMessage(
            from: User?,
            chat: Chat,
            date: Date,
            type: String = "text",
            payload: Any?,
            isIncoming: Boolean = false
        ): BaseMessage {
            return when (type) {
                "image" -> ImageMessage(
                    "${++messageId}",
                    from,
                    chat,
                    isIncoming,
                    date = date,
                    image = payload as String
                )
                else -> TextMessage(
                    "${++messageId}",
                    from,
                    chat,
                    isIncoming,
                    date = date,
                    text = payload as String
                )
            }
        }
    }
}
