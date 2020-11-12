package com.jess.cleanarchitecture.common.coroutine

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

sealed class JessCoroutinesState {
    object CoroutinesStarted : JessCoroutinesState()
    class CoroutinesFinished(val throwable: Throwable?) : JessCoroutinesState()
}

interface JessCoroutine {

    val exception: LiveData<Throwable>
    val coroutineState: LiveData<JessCoroutinesState>

    fun CoroutineScope.jessLaunch(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ): Job

}

class JessCoroutineImpl : JessCoroutine {

    private val _exception = MutableLiveData<Throwable>()
    override val exception: LiveData<Throwable> get() = _exception

    private val _coroutineState = MutableLiveData<JessCoroutinesState>()
    override val coroutineState: LiveData<JessCoroutinesState> get() = _coroutineState

    override fun CoroutineScope.jessLaunch(
        context: CoroutineContext,
        start: CoroutineStart,
        block: suspend CoroutineScope.() -> Unit
    ): Job = launch(context, start) {
        _coroutineState.postValue(JessCoroutinesState.CoroutinesStarted)
        val result = runCatching {
            block()
        }.onFailure {
            _exception.postValue(it)
        }
        _coroutineState.postValue(
            JessCoroutinesState.CoroutinesFinished(
                result.exceptionOrNull()
            )
        )
    }


}