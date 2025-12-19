package moletech.app

import app.cash.zipline.ZiplineService
import kotlinx.coroutines.flow.Flow



class JsGreetingService : GreetingService {
    override fun greet(name: String): String {
        return "Hello, $name! Greetings from Kotlin/JS via Zipline."
    }

    override suspend fun callDouble(flow: FlowZiplineCollector<Int>) {
        flow.emit(1)
        flow.emit(2)
        flow.close()
    }

    override suspend fun waitCall(flow: FlowZiplineService<Int>) {
//        fun jsPrint(i: Int) = println("this is jsPrint: $i")
        flow.collect {
//            jsPrint(it)
            println("this is jsPrint: $it")
        }
    }

    override suspend fun getPrintFlow(): FlowZiplineCollector<String> {
//        TODO("Not yet implemented")
        return FlowZiplineCollector {
            println("print by js $it")
        }
    }
}

@OptIn(ExperimentalJsExport::class)
@JsExport
fun main() {
    val zipline = app.cash.zipline.Zipline.get()
    zipline.bind<GreetingService>("greetingService", JsGreetingService())
}
