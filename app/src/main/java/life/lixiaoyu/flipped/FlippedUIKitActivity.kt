package life.lixiaoyu.flipped

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class FlippedUIKitActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flipped_uikit)

        findViewById<Button>(R.id.btn_flipped_arc_progress_bar).setOnClickListener {
            startActivity(Intent(this, FlippedArcProgressBarActivity::class.java))
        }
        findViewById<Button>(R.id.btn_flipped_nav_bar).setOnClickListener {
            startActivity(Intent(this, FlippedNavBarDemoActivity::class.java))
        }
        findViewById<Button>(R.id.btn_flipped_cell_item_view).setOnClickListener {
            startActivity(Intent(this, FlippedCellItemViewDemoActivity::class.java))
        }
    }
}