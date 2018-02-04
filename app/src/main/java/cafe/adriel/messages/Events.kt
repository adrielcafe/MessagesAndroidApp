package cafe.adriel.messages

import android.view.View
import cafe.adriel.messages.model.entity.Attachment
import cafe.adriel.messages.model.entity.Message

data class ShowAttachmentEvent(val view: View, val attachment: Attachment)

data class DeleteAttachmentEvent(val message: Message, val attachment: Attachment)