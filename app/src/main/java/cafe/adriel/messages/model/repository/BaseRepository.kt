package cafe.adriel.messages.model.repository

import cafe.adriel.messages.model.repository.adapter.RealmListJsonAdapterFactory
import cafe.adriel.messages.model.repository.adapter.UserAdapter
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import io.realm.Realm

abstract class BaseRepository {
    protected var realm = Realm.getDefaultInstance()
        get() = Realm.getDefaultInstance()
        private set

    companion object {
        internal const val PAGE_SIZE = 20
        internal val moshi by lazy {
            Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .add(RealmListJsonAdapterFactory())
                .add(UserAdapter())
                .build()
        }
    }

}