package com.jess.cleanarchitecture.common.coroutine

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.jess.cleanarchitecture.data.remote.ErrorResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import java.lang.Exception
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

sealed class State {
    object CoroutinesStarted : State()
    class CoroutinesFinished(val throwable: Throwable?) : State()
}

sealed class StateException {
    class Network(val exception: Exception) : StateException()
    class Error(val code: String?, val error: ErrorResponse?) : StateException()
}

interface StateCoroutine {

    val exception: LiveData<StateException>
    val coroutineState: LiveData<State>

    fun CoroutineScope.jessLaunch(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ): Job

}

class StateCoroutineImpl : StateCoroutine {

    private val _exception = MutableLiveData<StateException>()
    override val exception: LiveData<StateException> get() = _exception

    private val _coroutineState = MutableLiveData<State>()
    override val coroutineState: LiveData<State> get() = _coroutineState

    override fun CoroutineScope.jessLaunch(
        context: CoroutineContext,
        start: CoroutineStart,
        block: suspend CoroutineScope.() -> Unit
    ): Job = launch(context, start) {
        _coroutineState.postValue(State.CoroutinesStarted)
        val result = runCatching {
            block()
        }.onFailure {
            _exception.postValue(it.toException())
        }
        _coroutineState.postValue(
            State.CoroutinesFinished(
                result.exceptionOrNull()
            )
        )
    }
}

fun Throwable.toException(): StateException {
    return when (this) {
        is IOException -> {
            Timber.d("IOException ${this.localizedMessage}")
            StateException.Network(IOException())
        }
        is HttpException -> {
            val error = convertErrorBody(this)
            Timber.d("HttpException ${this.localizedMessage} / error $error")
            StateException.Error(code().toString(), error)
        }
        else -> {
            Timber.d("Error ${this.localizedMessage}")
            StateException.Error(null, null)
        }
    }
}

fun convertErrorBody(throwable: HttpException): ErrorResponse? {
    runCatching {
        throwable.response()?.errorBody()?.string().let {
            return Gson().fromJson(it, ErrorResponse::class.java)
        }
    }
    return null
}
