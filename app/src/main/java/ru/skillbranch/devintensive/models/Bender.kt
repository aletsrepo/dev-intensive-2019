package ru.skillbranch.devintensive.models

class Bender(var status: Status = Status.NORMAL, var question: Question = Question.NAME) {

    fun askQuestion(): String {
        return when(question) {
            Question.NAME -> Question.NAME.question
            Question.PROFESSION -> Question.PROFESSION.question
            Question.MATERIAL -> Question.MATERIAL.question
            Question.BDAY -> Question.BDAY.question
            Question.SERIAL -> Question.SERIAL.question
            Question.IDLE -> Question.IDLE.question
        }
    }

    fun listenAnswer(answer: String): Pair<String, Triple<Int, Int, Int>> {
        return if (question != Question.IDLE) {

            if (question.validate(answer).isEmpty()) {

                if (question.answers.contains(answer)) {
                    question = question.nextQuestion()
                    "Отлично - ты справился\n${question.question}" to status.color
                } else {
                    if (status == Status.CRITICAL) {
                        status = Status.NORMAL
                        question = Question.NAME
                        "Это неправильный ответ. Давай все по новой\n${question.question}" to status.color
                    } else {
                        status = status.nextStatus()
                        "Это неправильный ответ\n${question.question}" to status.color
                    }
                }

            } else {
                "${question.validate(answer)}\n${question.question}" to status.color
            }

        } else {
            "${question.question}" to Status.NORMAL.color
        }
    }

    enum class Status(val color: Triple<Int, Int, Int>) {
        NORMAL(Triple(255, 255, 255)),
        WARNING(Triple(255, 120, 0)),
        DANGER(Triple(255, 60, 60)),
        CRITICAL(Triple(255, 0, 0));

        fun nextStatus(): Status {
            return if (this.ordinal < values().lastIndex) {
                values()[this.ordinal + 1]
            } else {
                values()[0]
            }
        }
    }

    enum class Question(val question: String, val answers: List<String>) {
        NAME("Как меня зовут?", listOf("Бендер", "Bender")) {
            override fun validate(answer: String): String {
                return if (answer.isEmpty() || answer[0] !in 'A'..'Z' && answer[0] !in 'А'..'Я') {
                    "Имя должно начинаться с заглавной буквы"
                } else {
                    ""
                }
            }
            override fun nextQuestion(): Question = PROFESSION
        },
        PROFESSION("Назови мою профессию?", listOf("сгибальщик", "bender")) {
            override fun validate(answer: String): String {
                return if (answer.isEmpty() || answer[0] !in 'a'..'z' && answer[0] !in 'а'..'я') {
                    "Профессия должна начинаться со строчной буквы"
                } else {
                    ""
                }
            }
            override fun nextQuestion(): Question = MATERIAL
        },
        MATERIAL("Из чего я сделан?", listOf("металл", "дерево", "metal", "iron", "wood")) {
            override fun validate(answer: String): String {
                return if (answer.matches(Regex(".*[0-9].*"))) {
                    "Материал не должен содержать цифр"
                } else {
                    ""
                }
            }
            override fun nextQuestion(): Question = BDAY
        },
        BDAY("Когда меня создали?", listOf("2993")) {
            override fun validate(answer: String): String {
                return if (answer.isEmpty() || answer.toLongOrNull() == null) {
                    "Год моего рождения должен содержать только цифры"
                } else {
                    ""
                }
            }
            override fun nextQuestion(): Question = SERIAL
        },
        SERIAL("Мой серийный номер?", listOf("2716057")) {
            override fun validate(answer: String): String {
                return if (answer.isNotEmpty() && answer.toLongOrNull() != null && answer.length == 7) {
                    ""
                } else {
                    "Серийный номер содержит только цифры, и их 7"
                }
            }
            override fun nextQuestion(): Question = IDLE
        },
        IDLE("На этом все, вопросов больше нет", listOf()) {
            override fun validate(answer: String) = ""
            override fun nextQuestion(): Question = IDLE
        };

        abstract fun nextQuestion() : Question

        abstract fun validate(answer: String) : String
    }
}
