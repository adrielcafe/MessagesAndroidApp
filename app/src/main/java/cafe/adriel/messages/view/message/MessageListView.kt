package cafe.adriel.messages.view.message

import cafe.adriel.messages.model.entity.Message
import com.arellomobile.mvp.MvpView

interface MessageListView : MvpView {

    fun addMessages(page: Int, messages: List<Message>)

    fun removeMessage(message: Message)

    fun removeAttachment(message: Message)

}