package com.xendv

import com.xendv.plugins.configureRouting
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {
    @Test
    fun testRoot() = testApplication {
        application {
            configureRouting()
        }
        client.get("/storeroom/api/openapi").apply {
            assertEquals(HttpStatusCode.OK, status)
        }
    }
}
