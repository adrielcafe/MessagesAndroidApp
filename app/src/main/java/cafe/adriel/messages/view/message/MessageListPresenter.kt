package cafe.adriel.messages.view.message

import cafe.adriel.messages.model.entity.Attachment
import cafe.adriel.messages.model.entity.Message
import cafe.adriel.messages.model.entity.User
import cafe.adriel.messages.model.repository.BaseRepository
import cafe.adriel.messages.model.repository.MessageRepository
import cafe.adriel.messages.model.repository.UserRepository
import cafe.adriel.messages.paginate
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

@InjectViewState
class MessageListPresenter : MvpPresenter<MessageListView>() {
    private val disposables by lazy { CompositeDisposable() }

    fun dispose() = disposables.dispose()

    fun isCurrentUser(user: User) = user.id == UserRepository.getCurrentUser().id

    fun loadMessages(page: Int) =
        disposables.add(
            MessageRepository.getAll()
                .filter { it.isValid }
                .map { it.paginate(page, BaseRepository.PAGE_SIZE) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { viewState.addMessages(page, it) }
        )

    fun deleteMessage(message: Message) {
        viewState.removeMessage(message)
        MessageRepository.delete(message)
    }

    fun deleteAttachment(message: Message, attachment: Attachment) {
        viewState.removeAttachment(message)
        MessageRepository.delete(attachment)
    }

}