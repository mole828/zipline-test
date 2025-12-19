package moletech.app

import app.cash.zipline.ZiplineService

interface GreetingService : ZiplineService {
    fun greet(name: String): String
}
