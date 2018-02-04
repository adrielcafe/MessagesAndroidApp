package cafe.adriel.messages.model.repository.adapter

import com.squareup.moshi.*
import io.realm.RealmList
import io.realm.RealmObject
import java.lang.reflect.Type

/*
 * Custom Moshi adapter to (de)serialize RealmList
 * Based on https://github.com/Commit451/Regalia/tree/master/regalia-moshi
 */
class RealmListJsonAdapterFactory : JsonAdapter.Factory {

    override fun create(type: Type, annotations: Set<Annotation>, moshi: Moshi): JsonAdapter<*>? {
        if (!annotations.isEmpty()) return null
        return if (Types.getRawType(type) != RealmList::class.java) null
                else getAdapter<RealmObject>(type, moshi).nullSafe()
    }

    private fun <T : RealmObject> getAdapter(type: Type, moshi: Moshi): JsonAdapter<RealmList<T>> {
        val elementType = Types.collectionElementType(type, RealmList::class.java)
        val elementAdapter = moshi.adapter<T>(elementType)
        return RealmListAdapter(elementAdapter)
    }

    private class RealmListAdapter<T : RealmObject>(private val adapter: JsonAdapter<T>) :
        JsonAdapter<RealmList<T>>() {

        override fun fromJson(reader: JsonReader) =
            with(reader) {
                beginArray()
                val result = RealmList<T>()
                while (hasNext()) result.add(adapter.fromJson(this))
                endArray()
                result
            }

        override fun toJson(writer: JsonWriter, value: RealmList<T>?) =
            with(writer) {
                beginArray()
                value?.forEach { adapter.toJson(this, it) }
                endArray()
                Unit
            }
    }
}