package cafe.adriel.messages.model.entity

import cafe.adriel.messages.model.repository.adapter.UserParser
import com.squareup.moshi.Json
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Message(
    @PrimaryKey
    var id: Long = 0,
    @UserParser
    @Json(name = "userId")
    var user: User = User(),
    var content: String = "",
    var attachments: RealmList<Attachment> = RealmList()) : RealmObject()