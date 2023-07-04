package life.lixiaoyu.flipped

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.btn_flipped_uikit).setOnClickListener {
            startActivity(Intent(this, FlippedUIKitActivity::class.java))
        }
        findViewById<Button>(R.id.btn_flipped_livedatabus).setOnClickListener {
            startActivity(Intent(this, FlippedLiveDataBusActivity::class.java))
        }
    }
}