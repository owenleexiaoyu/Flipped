package life.lixiaoyu.flipped.bus

import androidx.lifecycle.Observer

class BusObserver<T>(
    private val liveData: BusLiveData<T>,
    private val sticky: Boolean,
    private val observer: Observer<in T>,
) : Observer<T> {

    // 让 BusObserver 的 lastVersion 在创建时，和 BusLiveData 的 version 对齐
    // 这样可以控制是否接收粘性事件
    private var lastVersion = liveData.version

    override fun onChanged(value: T) {
        // 说明没有新的数据可以发送
        if (lastVersion >= liveData.version) {
            // 如果观察者需要观察粘性事件，则将粘性事件的数据发送给它
            if(sticky && liveData.stickyValue != null) {
                observer.onChanged(liveData.stickyValue!!)
            }
            return
        }
        // 将 lastVersion 与 BusLiveData 的 version 保持对齐
        lastVersion = liveData.version
        observer.onChanged(value)
    }

}