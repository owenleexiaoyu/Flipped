package life.lixiaoyu.flipped.uikit.navbar

import android.view.View

interface OnFlippedNavBarListener {
    fun onStartClick(v: View?)
    fun onTitleClick(v: View?)
    fun onEndClick(v: View?)
}