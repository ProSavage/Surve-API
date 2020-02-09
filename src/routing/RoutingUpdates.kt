package us.surve.routing

import io.ktor.application.call
import io.ktor.request.receiveParameters
import io.ktor.routing.Routing
import io.ktor.routing.post

class RoutingUpdates(val instance: Routing) {

    init {
        instance.post("/add-resteraunt") {
            val params = call.receiveParameters()




            println(params)
        }
    }

}