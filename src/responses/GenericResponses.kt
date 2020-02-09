package us.surve.responses

import io.ktor.application.ApplicationCall
import io.ktor.response.respond
import us.surve.auth.SessionManager
import java.util.*


fun getGenericErrorResponse(message: String): Map<String, String> {
    return mapOf("message" to message)
}


suspend fun runPreChecks(call: ApplicationCall, context: ApplicationCall): Boolean {
    val rawToken = context.parameters["token"]?.toLowerCase()
    if (rawToken == null || UUID.fromString(rawToken) == null) {
        call.respond(getGenericErrorResponse("Invalid Token"))
        return false
    }
    val token = UUID.fromString(rawToken)
    val user = SessionManager.getSession(token)

    if (user == null) {
        call.respond(getGenericErrorResponse("You are not logged in,"))
        return false
    }
    return true

}