package cafe.adriel.messages

import android.content.res.Resources
import io.realm.Realm
import io.realm.RealmResults
import okio.Okio
import java.io.InputStream

val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

fun InputStream.asString(): String =
    Okio.buffer(Okio.source(this)).readUtf8()

inline fun realmTransaction(run: (realm : Realm) -> Unit) =
    with(Realm.getDefaultInstance()){
        beginTransaction()
        run(this)
        commitTransaction()
    }

/* Queries in Realm are lazy, so pagination isn't necessary
 * Since it's required for the project, I had to put the ugly code below :p
 * More info: https://realm.io/docs/swift/latest/#limiting-results
 */
fun <T> RealmResults<T>.paginate(currentPage: Int, pageSize: Int) =
    try {
        val start = currentPage * pageSize
        subList(start, size).take(pageSize)
    } catch (e: Exception){
        e.printStackTrace()
        emptyList<T>()
    }