package cafe.adriel.messages.view.message.adapter

import cafe.adriel.messages.model.entity.Message
import kotlinx.android.synthetic.main.list_item_message_right.view.*

class RightMessageAdapterItem(message: Message): BaseMessageAdapterItem(message) {

    override fun getType() = TYPE_RIGHT_MESSAGE

    override fun bindView(holder: ViewHolder, payloads: MutableList<Any>) {
        super.bindView(holder, payloads)
        holder.itemView?.apply {
            vContent.text = message.content
        }
    }

    override fun unbindView(holder: ViewHolder) {
        super.unbindView(holder)
        holder.itemView?.apply {
            vContent.text = ""
        }
    }

}