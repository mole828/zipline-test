package moletech.app

import app.cash.zipline.ZiplineService
import kotlinx.coroutines.flow.Flow

fun interface FlowZiplineCollector<T> : ZiplineService {
    suspend fun emit(value: T)
}

fun interface FlowZiplineService<T> : ZiplineService {
    suspend fun collect(collector: FlowZiplineCollector<T>)
}

interface GreetingService : ZiplineService {
    fun greet(name: String): String
    suspend fun callDouble(flow: FlowZiplineCollector<Int>)
    suspend fun waitCall(flow: FlowZiplineService<Int>)
    suspend fun getPrintFlow(): FlowZiplineCollector<String>
}
