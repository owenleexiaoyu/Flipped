package life.lixiaoyu.flipped.bus

import java.util.concurrent.ConcurrentHashMap

object FlippedLiveDataBus {

    // 维护 eventName 到 BusLiveData 的 map
    // 相同的 eventName 对应同一个 BusLiveData 对象
    // 不同的 eventName 对应不同的 BusLiveData 对象
    private val eventMap = ConcurrentHashMap<String, BusLiveData<*>>()

    fun <T> with(eventName: String): BusLiveData<T> {
        var liveData = eventMap[eventName]
        if (liveData == null) {
            liveData = BusLiveData<T>(eventName)
            eventMap[eventName] = liveData
        }
        return liveData as BusLiveData<T>
    }
}