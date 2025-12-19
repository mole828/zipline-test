import app.cash.zipline.Zipline
import app.cash.zipline.ZiplineManifest
import app.cash.zipline.loader.DefaultFreshnessCheckerNotFresh
import app.cash.zipline.loader.FreshnessChecker
import app.cash.zipline.loader.LoadResult
import app.cash.zipline.loader.ManifestVerifier
import app.cash.zipline.loader.ZiplineLoader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule
import okhttp3.OkHttpClient
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import moletech.app.GreetingService
import java.util.concurrent.Executors
import kotlin.test.Test

class ZiplineTest {
    @Test
    fun commonRun() = runBlocking {
        println("begin runBlocking")

        val ziplineExecutor = Executors.newSingleThreadExecutor { runnable ->
            Thread(runnable, "Zipline")
        }
        val ziplineDispatcher = ziplineExecutor.asCoroutineDispatcher()

        val manifestUrl = "http://localhost:8080/manifest.zipline.json"
        println("manifestUrl: $manifestUrl")
        val loader = ZiplineLoader(
            dispatcher = ziplineDispatcher,
            manifestVerifier = ManifestVerifier.NO_SIGNATURE_CHECKS,
            httpClient = OkHttpClient()
        )
        println("loader created")
        val loadResult = loader.loadOnce(
            applicationName = "greetingService",
            freshnessChecker = DefaultFreshnessCheckerNotFresh,
            manifestUrl = manifestUrl,
        )
        println("loader loadOnce")
        when(loadResult) {
            is LoadResult.Success -> {
                println("load success")
                val zipline = loadResult.zipline
                val greetingService = zipline.take<GreetingService>("greetingService")
                println(greetingService.greet("Junie"))

                greetingService.callDouble({
                    println("callDouble $it")
                })

                greetingService.waitCall { flow ->
                    flow.emit(3)
                    flow.emit(4)
                }

                val flow = greetingService.getPrintFlow()
                flow.emit("kt:5")
                flow.emit("kt:6")
                flow.close()

                zipline.close()
            }
            is LoadResult.Failure -> println("load failure: ${loadResult.exception}")
        }
        ziplineExecutor.shutdown()
        println("end runBlocking")
    }
}