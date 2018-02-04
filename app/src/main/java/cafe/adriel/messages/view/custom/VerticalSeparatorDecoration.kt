package cafe.adriel.messages.view.custom

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View
import cafe.adriel.messages.px

class VerticalSeparatorDecoration(val height: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.set(0, height.px, 0, height.px)
    }

}