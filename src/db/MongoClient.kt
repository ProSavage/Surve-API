package us.surve.db

import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import org.litote.kmongo.KMongo
import org.litote.kmongo.*
import us.surve.auth.User

object MongoClient {

    val kmongo = KMongo.createClient()


    fun getDatabase(): MongoDatabase {
        return kmongo.getDatabase("surve")
    }


    fun getUserCollection(): MongoCollection<User> {
        return getDatabase().getCollection<User>()
    }


    fun registerUser(user: User) {
        getUserCollection().insertOne(user)
    }

    fun getUser(email: String): User? {
        return getUserCollection().findOne(User::email eq email)
    }



}