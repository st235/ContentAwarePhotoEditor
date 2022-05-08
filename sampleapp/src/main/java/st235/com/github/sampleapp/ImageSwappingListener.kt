package st235.com.github.sampleapp

import android.view.MotionEvent
import android.view.View

class ImageSwappingListener(
    private val primaryView: View,
    private val secondaryView: View
): View.OnTouchListener {

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (v == null || event == null) {
            return false
        }

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                primaryView.visibility = View.INVISIBLE
                secondaryView.visibility = View.VISIBLE
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                primaryView.visibility = View.VISIBLE
                secondaryView.visibility = View.INVISIBLE
            }
        }

        return true
    }
}