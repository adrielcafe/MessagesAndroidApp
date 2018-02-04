package cafe.adriel.messages.model.repository

import cafe.adriel.messages.model.entity.User
import io.realm.kotlin.where

object UserRepository : BaseRepository() {
    // Session user has ID == 1
    private const val CURRENT_USER_ID = 1

    fun getCurrentUser() =
        realm.where<User>().equalTo("id", CURRENT_USER_ID).findFirst()!!

}