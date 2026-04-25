package Data.dao

import Data.User
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User) // Insert a new user into the database

    // Query that can retrieve a user from the database
    @Query("SELECT * FROM users_table WHERE username = :username LIMIT 1")
    suspend fun getUserByUsername(username: String): User?

    // Query that can retrieve a user from the database
    @Query("SELECT * FROM users_table WHERE username = :username AND password = :password LIMIT 1")
    suspend fun loginUser(username: String, password: String): User?
}