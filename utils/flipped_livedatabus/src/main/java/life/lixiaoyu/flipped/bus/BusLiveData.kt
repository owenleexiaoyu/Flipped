package life.lixiaoyu.flipped.bus

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

class BusLiveData<T>(
    private val eventName: String
): LiveData<T>() {

    private val mainHandler = Handler(Looper.getMainLooper())

    var version: Int = -1
        private set

    // 保存粘性事件的数据
    var stickyValue: T? = null
        private set

    public override fun setValue(value: T) {
        super.setValue(value)
        version++
    }

    public override fun postValue(value: T) {
        // 改写 postValue 避免丢失事件
        // 直接通过 Handler post 到主线程调用 setValue
        mainHandler.post {
            setValue(value)
        }
    }


    /**
     * 发送粘性事件
     * 这个方法只能在主线程中调用
     */
    fun setStickyValue(stickyValue: T) {
        this.stickyValue = stickyValue
        setValue(stickyValue)
    }

    /**
     * 发送粘性事件
     * 这个方法可以在子线程中调用
     */
    fun postStickyValue(stickyValue: T) {
        this.stickyValue = stickyValue
        postValue(stickyValue)
    }

    /**
     * 和 [owner] 宿主生命周期绑定的订阅
     * 不关心粘性事件
     */
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        observeSticky(owner, false, observer)
    }

    /**
     * 和 [owner] 宿主生命周期绑定的订阅
     * 可以指定是否需要关心粘性事件
     * sticky = true: 需要接收粘性事件
     * sticky = false: 不需要接收粘性事件
     */
    fun observeSticky(owner: LifecycleOwner, sticky: Boolean, observer: Observer<in T>) {
        super.observe(owner, BusObserver(this, sticky, observer))
    }

    override fun observeForever(observer: Observer<in T>) {
        observeForeverSticky(false, observer)
    }

    /**
     * 永久订阅，需要自行管理反注册，以避免内存泄漏
     * 可以指定是否需要关心粘性事件
     * sticky = true: 需要接收粘性事件
     * sticky = false: 不需要接收粘性事件
     */
    fun observeForeverSticky(sticky: Boolean, observer: Observer<in T>) {
        super.observeForever(BusObserver(this, sticky, observer))
    }

}