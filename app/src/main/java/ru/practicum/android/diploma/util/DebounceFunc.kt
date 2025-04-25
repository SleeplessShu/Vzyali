package ru.practicum.android.diploma.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DebounceFunc<T>(
    val call: (T) -> Unit,
    val cancel: () -> Unit
)

fun <T> debounce(
    delayMillis: Long,
    coroutineScope: CoroutineScope,
    useLastParam: Boolean,
    action: (T) -> Unit
): DebounceFunc<T> {
    var debounceJob: Job? = null

    val call: (T) -> Unit = { param: T ->
        if (useLastParam) {
            debounceJob?.cancel()
        }
        if (debounceJob?.isCompleted != false || useLastParam) {
            debounceJob = coroutineScope.launch {
                delay(delayMillis)
                action(param)
            }
        }
    }

    val cancel: () -> Unit = { debounceJob?.cancel() }

    return DebounceFunc(call, cancel)
}
