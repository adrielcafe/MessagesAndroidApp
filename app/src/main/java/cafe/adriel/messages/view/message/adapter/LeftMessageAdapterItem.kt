package cafe.adriel.messages.view.message.adapter

import cafe.adriel.messages.GlideApp
import cafe.adriel.messages.model.entity.Message
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kotlinx.android.synthetic.main.list_item_message_left.view.*

class LeftMessageAdapterItem(message: Message): BaseMessageAdapterItem(message) {

    override fun getType() = TYPE_LEFT_MESSAGE

    override fun bindView(holder: ViewHolder, payloads: MutableList<Any>) {
        super.bindView(holder, payloads)
        holder.itemView?.apply {
            vUserName.text = message.user.name
            vContent.text = message.content
            GlideApp.with(context)
                .load(message.user.avatarUrl)
                .circleCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(vUserAvatar)
        }
    }

    override fun unbindView(holder: ViewHolder) {
        super.unbindView(holder)
        holder.itemView?.apply {
            vUserName.text = ""
            vContent.text = ""
            GlideApp.with(context).clear(vUserAvatar)
        }
    }

}