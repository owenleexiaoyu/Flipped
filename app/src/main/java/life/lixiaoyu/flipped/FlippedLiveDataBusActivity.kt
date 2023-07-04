package life.lixiaoyu.flipped

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import life.lixiaoyu.flipped.bus.FlippedLiveDataBus

class FlippedLiveDataBusActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flipped_livedatabus)
        FlippedLiveDataBus.with<String>("Event1").observe(this) {
            Toast.makeText(this, "接收到 Event1 的数据：$it", Toast.LENGTH_SHORT).show()
        }
        findViewById<Button>(R.id.btn_send_normal_event).setOnClickListener {
            FlippedLiveDataBus.with<String>("Event1").setValue("Hello")
        }

        findViewById<Button>(R.id.btn_send_sticky_event).setOnClickListener {
            FlippedLiveDataBus.with<String>("Event2").setStickyValue("World")
        }
        findViewById<Button>(R.id.btn_observe).setOnClickListener {
            FlippedLiveDataBus.with<String>("Event2").observe(this) {
                Toast.makeText(this, "接收到 Event2 的粘性数据：$it", Toast.LENGTH_SHORT).show()
            }
        }
        findViewById<Button>(R.id.btn_observeSticky).setOnClickListener {
            FlippedLiveDataBus.with<String>("Event2").observeSticky(this, true) {
                Toast.makeText(this, "接收到 Event2 的粘性数据：$it", Toast.LENGTH_SHORT).show()
            }
        }
    }
}