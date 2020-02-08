package us.surve.routing

import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.post
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import us.surve.auth.User
import us.surve.db.MongoClient
import us.surve.responses.getGenericErrorResponse
import java.util.*

class RoutingRoot(val routing: Routing) {

    init {
        setupRoutes()
    }

    private fun setupRoutes() {
        routing.get("/login/{email}/{password}") {
            println("I got a login request")
            val email = context.parameters["email"]
            val password = context.parameters["password"]
            if (email == null || password == null) {
                call.respond(mapOf("message" to "Invalid Request"))
                return@get
            }
            val user = MongoClient.getUser(email)
            if (user == null) {
                call.respond(getGenericErrorResponse("User does not exist in the database."))
                return@get
            }
            if (user.password != password) {
                call.respond(getGenericErrorResponse("Wrong password"))
                return@get
            }

            val token = UUID.randomUUID()
            call.respond(mapOf("message" to "Success", "token" to token))
        }



        routing.get("/signup/{email}/{password}") {
            println("I got a signup request")
            val email = context.parameters["email"]
            val password = context.parameters["password"]
            if (email == null || password == null || email.length < 10 || password.length < 5) {
                call.respond(getGenericErrorResponse("Invalid Request"))
                return@get
            }
            val user = User(email, password)
            MongoClient.registerUser(user)
            call.respond(mapOf("message" to "Success"))
        }


    }


}