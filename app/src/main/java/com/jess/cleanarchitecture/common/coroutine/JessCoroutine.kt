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

sealed class JessState {
    object CoroutinesStarted : JessState()
    class CoroutinesFinished(val throwable: Throwable?) : JessState()
}

sealed class JessException {
    class Network(val exception: Exception) : JessException()
    class Error(val code: String?, val error: ErrorResponse?) : JessException()
}

interface JessCoroutine {

    val exception: LiveData<JessException>
    val coroutineState: LiveData<JessState>

    fun CoroutineScope.jessLaunch(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ): Job

}

class JessCoroutineImpl : JessCoroutine {

    private val _exception = MutableLiveData<JessException>()
    override val exception: LiveData<JessException> get() = _exception

    private val _coroutineState = MutableLiveData<JessState>()
    override val coroutineState: LiveData<JessState> get() = _coroutineState

    override fun CoroutineScope.jessLaunch(
        context: CoroutineContext,
        start: CoroutineStart,
        block: suspend CoroutineScope.() -> Unit
    ): Job = launch(context, start) {
        _coroutineState.postValue(JessState.CoroutinesStarted)
        val result = runCatching {
            block()
        }.onFailure {
            _exception.postValue(it.toException())
        }
        _coroutineState.postValue(
            JessState.CoroutinesFinished(
                result.exceptionOrNull()
            )
        )
    }
}

fun Throwable.toException(): JessException {
    return when (this) {
        is IOException -> {
            Timber.d("IOException ${this.localizedMessage}")
            JessException.Network(IOException())
        }
        is HttpException -> {
            val error = convertErrorBody(this)
            Timber.d("HttpException ${this.localizedMessage} / error $error")
            JessException.Error(code().toString(), error)
        }
        else -> {
            Timber.d("Error ${this.localizedMessage}")
            JessException.Error(null, null)
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
