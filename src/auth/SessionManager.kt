package us.surve.auth

import us.surve.db.MongoClient
import java.util.*

object SessionManager {

    val sessions = HashMap<UUID, String>()

    fun getSession(uuid: UUID): User? {
        return MongoClient.getUser(sessions[uuid] ?: "")
    }

    fun setSession(token: UUID, email: String) {
        sessions[token] = email
    }








}