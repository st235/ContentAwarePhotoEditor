package st235.com.github.seamcarving.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

class MutableLiveEvent<T>: MutableLiveData<T>() {

    private val isPending = AtomicBoolean(false)

    override fun setValue(value: T) {
        isPending.set(true)
        super.setValue(value)
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner) { t ->
            if (isPending.compareAndSet(true, false)) {
                observer.onChanged(t)
            }
        }
    }
}