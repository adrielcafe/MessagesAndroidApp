package cafe.adriel.messages.model.entity

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Attachment(
    @PrimaryKey
    var id: String = "",
    var title: String = "",
    var url: String = "",
    var thumbnailUrl: String = "") : RealmObject()