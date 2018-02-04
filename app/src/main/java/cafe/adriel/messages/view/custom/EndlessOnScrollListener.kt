package cafe.adriel.messages.view.custom

import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.view.View

abstract class EndlessOnScrollListener(val layoutManager: RecyclerView.LayoutManager)
    : RecyclerView.OnScrollListener() {

    private val orientationHelper = OrientationHelper.createVerticalHelper(layoutManager)

    private var visibleThreshold = -1
    private var visibleItemCount = 0
    private var firstVisibleItem = 0
    private var totalItemCount = 0
    private var previousTotal = 0
    private var currentPage = 0
    private var loading = true

    private fun findFirstVisibleItemPosition(recyclerView: RecyclerView): Int {
        val child = findOneVisibleChild(0, layoutManager.childCount, false, true)
        return if (child == null) RecyclerView.NO_POSITION
                else recyclerView.getChildAdapterPosition(child)
    }

    private fun findLastVisibleItemPosition(recyclerView: RecyclerView): Int {
        val child = findOneVisibleChild(recyclerView.childCount - 1, -1, false, true)
        return if (child == null) RecyclerView.NO_POSITION
                else recyclerView.getChildAdapterPosition(child)
    }

    private fun findOneVisibleChild(fromIndex: Int, toIndex: Int, completelyVisible: Boolean, acceptPartiallyVisible: Boolean): View? {
        val start = orientationHelper.startAfterPadding
        val end = orientationHelper.endAfterPadding
        val next = if (toIndex > fromIndex) 1 else -1
        var partiallyVisible: View? = null
        var i = fromIndex

        while (i != toIndex) {
            layoutManager.getChildAt(i)?.apply {
                val childStart = orientationHelper.getDecoratedStart(this)
                val childEnd = orientationHelper.getDecoratedEnd(this)
                if (childStart < end && childEnd > start) {
                    if (completelyVisible) {
                        if (childStart >= start && childEnd <= end) {
                            return this
                        } else if (acceptPartiallyVisible && partiallyVisible == null) {
                            partiallyVisible = this
                        }
                    } else {
                        return this
                    }
                }
            }
            i += next
        }
        return partiallyVisible
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (visibleThreshold == -1)
            visibleThreshold = findLastVisibleItemPosition(recyclerView) -
                    findFirstVisibleItemPosition(recyclerView)

        visibleItemCount = recyclerView.childCount
        totalItemCount = layoutManager.itemCount
        firstVisibleItem = findFirstVisibleItemPosition(recyclerView)

        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false
                previousTotal = totalItemCount
            }
        }

        if (!loading && totalItemCount - visibleItemCount <= firstVisibleItem + visibleThreshold) {
            currentPage++
            onLoadMore(currentPage)
            loading = true
        }
    }

    abstract fun onLoadMore(currentPage: Int)
}