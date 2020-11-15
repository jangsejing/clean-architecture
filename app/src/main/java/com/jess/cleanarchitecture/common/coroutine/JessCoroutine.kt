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
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

sealed class State {
    object CoroutinesStarted : State()
    class CoroutinesFinished(val throwable: Throwable?) : State()
}

sealed class Exception {
    object NetworkError : Exception()
    class Error(val code: String?, val error: ErrorResponse?) : Exception()
}

interface JessCoroutine {

    val exception: LiveData<Exception>
    val coroutineState: LiveData<State>

    fun CoroutineScope.jessLaunch(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ): Job

}

class JessCoroutineImpl : JessCoroutine {

    private val _exception = MutableLiveData<Exception>()
    override val exception: LiveData<Exception> get() = _exception

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

fun Throwable.toException(): Exception {
    return when (this) {
        is IOException -> {
            Timber.d("IOException ${this.localizedMessage}")
            Exception.NetworkError
        }
        is HttpException -> {
            val error = convertErrorBody(this)
            Timber.d("HttpException ${this.localizedMessage} / error $error")
            Exception.Error(code().toString(), error)
        }
        else -> {
            Timber.d("Error ${this.localizedMessage}")
            Exception.Error(null, null)
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
