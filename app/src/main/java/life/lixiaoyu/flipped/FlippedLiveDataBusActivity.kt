package life.lixiaoyu.flipped

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import life.lixiaoyu.flipped.bus.FlippedLiveDataBus

class FlippedLiveDataBusActivity : AppCompatActivity() {
    @SuppressLint("LongLogTag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flipped_livedatabus)
        var data = 100
        FlippedLiveDataBus.with<String>("Event1").observe(this) {
            Log.d("FlippedLiveDataBusActivity", "接收到 Event1 的数据：$it")
            Toast.makeText(this, "接收到 Event1 的数据：$it", Toast.LENGTH_SHORT).show()
        }
        findViewById<Button>(R.id.btn_send_normal_event).setOnClickListener {
            val d = "Hello($data)"
            Log.d("FlippedLiveDataBusActivity", "Send Event1：$d")
            FlippedLiveDataBus.with<String>("Event1").setValue(d)
            data++
        }
        findViewById<Button>(R.id.btn_send_normal_event_io).setOnClickListener {
            val d = "Hello($data)"
            Log.d("FlippedLiveDataBusActivity", "Send Event1 in background thread：$d")
            Thread(
                Runnable {
                    FlippedLiveDataBus.with<String>("Event1").postValue(d)
                    data++
                }).start()
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