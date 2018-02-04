package cafe.adriel.messages.model.repository.adapter

import cafe.adriel.messages.model.entity.User
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

/*
 * Custom Moshi adapter to convert JSON's userId into Realm's User
 */
class UserAdapter {

    @FromJson
    @UserParser
    fun fromJson(id: Long) = User(id)

    @ToJson
    fun toJson(@UserParser user: User) = user.id

}