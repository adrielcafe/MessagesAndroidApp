package cafe.adriel.messages

import android.app.Application
import android.support.text.emoji.EmojiCompat
import android.support.text.emoji.FontRequestEmojiCompatConfig
import android.support.v4.provider.FontRequest
import cafe.adriel.messages.model.entity.InitialData
import cafe.adriel.messages.model.repository.BaseRepository
import io.realm.Realm
import io.realm.RealmConfiguration

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initEmoji()
        initDatabase()
    }

    private fun initEmoji(){
        val fontRequest = FontRequest(
            "com.google.android.gms.fonts",
            "com.google.android.gms",
            "Noto Color Emoji Compat",
            R.array.com_google_android_gms_fonts_certs)
        EmojiCompat.init(FontRequestEmojiCompatConfig(this, fontRequest))
    }

    private fun initDatabase() {
        Realm.init(this)
        Realm.setDefaultConfiguration(RealmConfiguration.Builder()
            .schemaVersion(BuildConfig.DB_VERSION)
            .initialData { addInitialData(it) }
            .migration { realm, oldVersion, newVersion ->
                // Nothing to migrate, yet
            }
            .build())
    }

    private fun addInitialData(realm: Realm){
        val json = resources.openRawResource(R.raw.data).asString()
        BaseRepository.moshi
            .adapter(InitialData::class.java)
            .fromJson(json)
            ?.let { data ->
                realm.insertOrUpdate(data.messages)
                realm.insertOrUpdate(data.users)
            }
    }

}