package cafe.adriel.messages.model.repository

import cafe.adriel.messages.model.entity.Attachment
import cafe.adriel.messages.model.entity.Message
import cafe.adriel.messages.realmTransaction
import io.realm.kotlin.where

object MessageRepository : BaseRepository() {

    fun getAll() =
        realm.where<Message>()
            .sort("id")
            .findAll()
            .asFlowable()
            .firstElement()

    fun delete(message: Message) =
        realmTransaction { message.deleteFromRealm() }

    fun delete(attachment: Attachment) =
        realmTransaction { attachment.deleteFromRealm() }

}