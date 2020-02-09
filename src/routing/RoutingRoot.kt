package routing

import Food
import LocationProperties
import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import us.surve.auth.User
import us.surve.db.MongoClient
import us.surve.responses.getGenericErrorResponse
import us.surve.auth.SessionManager
import us.surve.responses.runPreChecks
import java.util.*
import kotlin.collections.HashMap

class RoutingRoot(val routing: Routing, val sessions: HashMap<UUID, String>) {


    init {
        setupRoutes()
    }

    private fun setupRoutes() {
        routing.get("/auth/login/{email}/{password}") {
            println("I got a login request")
            val email = context.parameters["email"]?.toLowerCase()
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
            SessionManager.setSession(token, email)
            call.respond(mapOf("message" to "Success", "token" to token))
        }

        routing.get("/auth/signup/{email}/{password}") {
            println("I got a sign-up request")
            val email = context.parameters["email"]?.toLowerCase()
            val password = context.parameters["password"]
            if (email == null || password == null || email.length < 6 || password.length < 6) {
                call.respond(mapOf("message" to "Invalid Request"))
                return@get
            }
            val user = User(email, password, arrayListOf())
            MongoClient.registerUser(user)
            call.respond(mapOf("message" to "Success"))
        }


        routing.get("/profile/favorites/list/{token}") {
            println("I got a favorite request")
            if (!runPreChecks(call, context)) return@get
            val user = SessionManager.getSession(UUID.fromString(call.parameters["token"]!!))!!
            call.respond(mapOf("message" to "Success", "favorites" to user.favorites))
        }

        routing.get("/profile/favorites/add/{id}/{token}") {
            println("I got a request to add a favorite request")
            val id = context.parameters["id"]?.toInt()
            if (id == null || id < 0 || id > 3) {
                call.respond(mapOf("message" to "invalid id"))
                return@get
            }
            if (!runPreChecks(call, context)) return@get
            val user = SessionManager.getSession(UUID.fromString(call.parameters["token"]!!))!!
            user.favorites.add(id)
            call.respond(mapOf("message" to "Success"))
        }

        routing.get("/profile/favorites/remove/{id}/{token}") {
            println("I got a request to remove a favorite")
            val id = context.parameters["id"]?.toInt()
            if (id == null || id < 0 || id > 3) {
                call.respond(mapOf("message" to "invalid id"))
                return@get
            }
            if (!runPreChecks(call, context)) return@get
            val user = SessionManager.getSession(UUID.fromString(call.parameters["token"]!!))!!
            user.favorites.remove(id)
            return@get
        }


        routing.get("/static/location") {
            call.respond(
                mapOf(
                    "locations" to arrayListOf(
                        mapOf(
                            0 to LocationProperties(
                                0,
                                "Bhartiya Restaurant",
                                "1234 street",
                                5,
                                arrayListOf(
                                    Food(0, "Chicken Chilli", 8.99),
                                    Food(1, "Tandoori Salmon", 12.0),
                                    Food(2, "Paneer Tikka Masala", 10.99),
                                    Food(3, "Aloo Gobhi", 9.99)
                                )
                            ),
                            1 to LocationProperties(
                                1, "Mexican Restaurant", "4578 Gay St, Opelika AL- 36754", 4,
                                arrayListOf(
                                    Food(0, "Linguini with Clams", 23.99),
                                    Food(1, "Spaghetti Carbonara", 18.0),
                                    Food(2, "Rigatoni Bolognese Beef", 21.99),
                                    Food(3, "Rigatoni Bolognese Beef", 21.99)
                                )
                            ),
                            2 to LocationProperties(
                                2, "Chinese Restaurant", "3467 Corner Auburn AL- 36849", 5,
                                arrayListOf(
                                    Food(0, "Taco Burrito, and enchilada",8.00),
                                    Food(1, "Taco Salad", 8.95),
                                    Food(2, "Taco and enchilada with rice and beans", 7.00),
                                    Food(3, "Burrito Supreme", 8.95)
                                )
                            ),
                            3 to LocationProperties(
                                3, "Italian Restaurant", "9087 University Dr, Opelika AL - 45897", 4,
                                arrayListOf(
                                    Food(0, "Sweet and Sour Chicken", 9.99),
                                    Food(1, "Chicken Broccoli", 7.50),
                                    Food(2, "Kung Pao Beef", 8.99),
                                    Food(3, "Szechuan Beef", 7.99)
                                )
                            )
                        )
                    )
                )
            )

        }


    }


}