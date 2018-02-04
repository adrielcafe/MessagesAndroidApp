package cafe.adriel.messages.view.message

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import cafe.adriel.kbus.KBus
import cafe.adriel.messages.DeleteAttachmentEvent
import cafe.adriel.messages.R
import cafe.adriel.messages.ShowAttachmentEvent
import cafe.adriel.messages.model.entity.Attachment
import cafe.adriel.messages.model.entity.Message
import cafe.adriel.messages.view.BaseActivity
import cafe.adriel.messages.view.attachment.ShowAttachmentActivity
import cafe.adriel.messages.view.custom.EndlessOnScrollListener
import cafe.adriel.messages.view.custom.VerticalSeparatorDecoration
import cafe.adriel.messages.view.message.adapter.BaseMessageAdapterItem
import cafe.adriel.messages.view.message.adapter.LeftMessageAdapterItem
import cafe.adriel.messages.view.message.adapter.RightMessageAdapterItem
import com.arellomobile.mvp.presenter.InjectPresenter
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import com.mikepenz.fastadapter.listeners.LongClickEventHook
import kotlinx.android.synthetic.main.activity_message_list.*

class MessageListActivity : BaseActivity(), MessageListView {
    @InjectPresenter
    lateinit var presenter: MessageListPresenter

    private val adapter = FastItemAdapter<BaseMessageAdapterItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_list)
        setSupportActionBar(vToolbar)

        adapter.setHasStableIds(true)
        adapter.withEventHook(object : LongClickEventHook<BaseMessageAdapterItem>(){
            override fun onBind(viewHolder: RecyclerView.ViewHolder): View {
                return viewHolder.itemView.findViewById(R.id.vContentLayout)
            }
            override fun onLongClick(v: View, position: Int, fastAdapter: FastAdapter<BaseMessageAdapterItem>, item: BaseMessageAdapterItem): Boolean {
                showDeleteMessageDialog(item.message)
                return true
            }
        })

        vMessages.adapter = adapter
        vMessages.itemAnimator = DefaultItemAnimator()
        vMessages.layoutManager = LinearLayoutManager(this)
        vMessages.addItemDecoration(VerticalSeparatorDecoration(8))
        vMessages.addOnScrollListener(object : EndlessOnScrollListener(vMessages.layoutManager) {
            override fun onLoadMore(currentPage: Int) {
                presenter.loadMessages(currentPage)
            }
        })
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        presenter.loadMessages(0)
    }

    override fun onStart() {
        super.onStart()
        KBus.subscribe<ShowAttachmentEvent>(this, {
            showAttachment(it.view, it.attachment)
        })
        KBus.subscribe<DeleteAttachmentEvent>(this, {
            showDeleteAttachmentDialog(it.message, it.attachment)
        })
    }

    override fun onStop() {
        super.onStop()
        KBus.unsubscribe(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.dispose()
    }

    override fun addMessages(page: Int, messages: List<Message>) {
        if(page == 0) adapter.clear()
        messages.forEach {
            // Check if the message already exists
            if(adapter.getPosition(it.id) == -1) {
                adapter.add(
                    // Session user messages should be right-aligned
                    if (presenter.isCurrentUser(it.user)) RightMessageAdapterItem(it)
                    else LeftMessageAdapterItem(it)
                )
            }
        }
        adapter.notifyAdapterDataSetChanged()
    }

    override fun removeMessage(message: Message) {
        if(message.isValid) {
            val position = adapter.getGlobalPosition(adapter.getPosition(message.id))
            adapter.remove(position)
            if (adapter.adapterItemCount == 0) presenter.loadMessages(0)
        }
    }

    override fun removeAttachment(message: Message) {
        if(message.isValid) {
            val position = adapter.getGlobalPosition(adapter.getPosition(message.id))
            adapter.notifyAdapterItemChanged(position)
        }
    }

    private fun showAttachment(view: View, attachment: Attachment){
        val intent = Intent(this, ShowAttachmentActivity::class.java)
            .putExtra(ShowAttachmentActivity.EXTRA_IMAGE_URL, attachment.url)
        val options = ActivityOptionsCompat
            .makeSceneTransitionAnimation(this, view, "attachment")
            .toBundle()
        startActivity(intent, options)
    }

    private fun showDeleteMessageDialog(message: Message){
        AlertDialog.Builder(this)
            .setItems(R.array.message_dialog_options, { _, which ->
                when(which){
                    0 -> presenter.deleteMessage(message)
                }
            })
            .show()
    }

    private fun showDeleteAttachmentDialog(message: Message, attachment: Attachment){
        AlertDialog.Builder(this)
            .setItems(R.array.attachment_dialog_options, { _, which ->
                when(which){
                    0 -> presenter.deleteAttachment(message, attachment)
                }
            })
            .show()
    }

}