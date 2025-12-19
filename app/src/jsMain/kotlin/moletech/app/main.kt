package moletech.app

class JsGreetingService : GreetingService {
    override fun greet(name: String): String {
        return "Hello, $name! Greetings from Kotlin/JS via Zipline."
    }
}

@OptIn(ExperimentalJsExport::class)
@JsExport
fun main() {
    val zipline = app.cash.zipline.Zipline.get()
    zipline.bind<GreetingService>("greetingService", JsGreetingService())
}
