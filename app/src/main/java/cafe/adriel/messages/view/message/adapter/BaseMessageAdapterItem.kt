package cafe.adriel.messages.view.message.adapter

import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cafe.adriel.kbus.KBus
import cafe.adriel.messages.DeleteAttachmentEvent
import cafe.adriel.messages.GlideApp
import cafe.adriel.messages.R
import cafe.adriel.messages.ShowAttachmentEvent
import cafe.adriel.messages.model.entity.Attachment
import cafe.adriel.messages.model.entity.Message
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.mikepenz.fastadapter.items.AbstractItem
import com.santalu.aspectratioimageview.AspectRatioImageView

abstract class BaseMessageAdapterItem(val message: Message):
    AbstractItem<BaseMessageAdapterItem, BaseMessageAdapterItem.ViewHolder>() {

    protected val TYPE_LEFT_MESSAGE = 0
    protected val TYPE_RIGHT_MESSAGE = 1

    val id = message.id

    override fun getIdentifier() = id

    override fun getLayoutRes() =
        when(type){
            TYPE_RIGHT_MESSAGE -> R.layout.list_item_message_right
            else -> R.layout.list_item_message_left
        }

    override fun getViewHolder(v: View) = ViewHolder(v)

    override fun bindView(holder: ViewHolder, payloads: MutableList<Any>) {
        super.bindView(holder, payloads)
        holder.itemView?.apply {
            val vAttachments = findViewById<ViewGroup>(R.id.vAttachments)
            addAttachments(message.attachments, vAttachments)
        }
    }

    override fun unbindView(holder: ViewHolder) {
        super.unbindView(holder)
        holder.itemView?.apply {
            val vAttachments = findViewById<ViewGroup>(R.id.vAttachments)
            removeAttachments(vAttachments)
        }
    }

    private fun addAttachments(attachments: List<Attachment>, layout: ViewGroup){
        if(attachments.isNotEmpty()) {
            layout.apply {
                visibility = View.VISIBLE
                removeAllViews()
                attachments.forEach { attachment ->
                    val vRoot = LayoutInflater.from(context)
                        .inflate(R.layout.list_item_attachment, null)
                    val vThumbnail = vRoot.findViewById<AspectRatioImageView>(R.id.vThumbnail)
                    val vTitle = vRoot.findViewById<AppCompatTextView>(R.id.vTitle)

                    vTitle.text = attachment.title
                    GlideApp.with(context)
                        .load(attachment.thumbnailUrl)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(vThumbnail)

                    vRoot.setOnClickListener {
                        KBus.post(ShowAttachmentEvent(it, attachment))
                    }
                    vRoot.setOnLongClickListener {
                        KBus.post(DeleteAttachmentEvent(message, attachment))
                        true
                    }
                    addView(vRoot)
                }
            }
        }
    }

    private fun removeAttachments(layout: ViewGroup){
        layout.apply {
            visibility = View.GONE
            removeAllViews()
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}