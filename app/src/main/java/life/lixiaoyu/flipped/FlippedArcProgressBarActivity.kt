package life.lixiaoyu.flipped

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.LinearInterpolator
import life.lixiaoyu.flipped.uikit.FlippedArcProgressBar

class FlippedArcProgressBarActivity : AppCompatActivity() {

    private lateinit var progressBar1: FlippedArcProgressBar
    private lateinit var progressBar2: FlippedArcProgressBar
    private lateinit var progressBar3: FlippedArcProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flipped_arc_progress_bar)
        progressBar1 = findViewById(R.id.animated_progress_bar_1)
        progressBar2 = findViewById(R.id.animated_progress_bar_2)
        progressBar3 = findViewById(R.id.animated_progress_bar_3)
    }

    override fun onResume() {
        super.onResume()
        progressBar1.startAnimator(LinearInterpolator(), 2000L)
        progressBar2.startAnimator(AccelerateDecelerateInterpolator(), 2000L)
        progressBar3.startAnimator(duration =  2000L)
    }
}