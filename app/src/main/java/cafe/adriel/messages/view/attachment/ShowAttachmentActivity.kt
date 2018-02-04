package cafe.adriel.messages.view.attachment

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.ViewTreeObserver
import cafe.adriel.messages.GlideApp
import cafe.adriel.messages.R
import cafe.adriel.messages.view.BaseActivity
import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_show_attachment.*
import java.util.*

class ShowAttachmentActivity : BaseActivity() {
    companion object {
        const val EXTRA_IMAGE_URL = "imageUrl"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_attachment)

        if(!intent.hasExtra(EXTRA_IMAGE_URL)){
            throw NoSuchElementException("EXTRA_IMAGE_URL is missing")
        }
        val imageUrl = intent.getStringExtra(EXTRA_IMAGE_URL)

        supportPostponeEnterTransition()

        vAttachment.setOnTouchListener(ImageMatrixTouchHandler(this))
        GlideApp.with(this)
            .load(imageUrl)
            .listener(object : RequestListener<Drawable> {
                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    startTransition()
                    return false
                }
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean) : Boolean {
                    return false
                }
            })
            .into(vAttachment)
    }

    private fun startTransition(){
        vAttachment.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    vAttachment.viewTreeObserver.removeOnPreDrawListener(this)
                    supportStartPostponedEnterTransition()
                    return true
                }
            }
        )
    }

}