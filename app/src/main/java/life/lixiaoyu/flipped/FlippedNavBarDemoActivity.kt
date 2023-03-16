package life.lixiaoyu.flipped

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import life.lixiaoyu.flipped.uikit.navbar.FlippedNavBar
import life.lixiaoyu.flipped.uikit.navbar.OnFlippedNavBarListener

class FlippedNavBarDemoActivity : AppCompatActivity() {

    private var titleBar: FlippedNavBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flipped_nav_bar_demo)

        titleBar = findViewById(R.id.title_bar)
        titleBar?.setOnNavBarListener(object : OnFlippedNavBarListener {
            override fun onEndClick(v: View?) {
                Toast.makeText(this@FlippedNavBarDemoActivity, "end click", Toast.LENGTH_SHORT).show()
            }

            override fun onStartClick(v: View?) {
                Toast.makeText(this@FlippedNavBarDemoActivity, "start click", Toast.LENGTH_SHORT).show()
            }

            override fun onTitleClick(v: View?) {
                Toast.makeText(this@FlippedNavBarDemoActivity, "title click", Toast.LENGTH_SHORT).show()
            }

        })
    }
}