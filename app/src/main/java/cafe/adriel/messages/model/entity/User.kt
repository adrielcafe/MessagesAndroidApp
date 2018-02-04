package cafe.adriel.messages.model.entity

import com.squareup.moshi.Json
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class User(
    @PrimaryKey
    var id: Long = 0,
    var name: String = "",
    @Json(name = "avatarId")
    var avatarUrl: String = "") : RealmObject()