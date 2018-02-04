package cafe.adriel.messages.model.entity

data class InitialData(
    val users: List<User> = emptyList(),
    val messages: List<Message> = emptyList())