package Data

import androidx.room.Entity
import androidx.room.PrimaryKey

// Creates and marks the class as a Room database entity (table)
@Entity(tableName = "users_table")
data class User(
    // Primary key of the user is auto generated
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val username: String, // Stores the username of the registered user
    val password: String // Stores the password of the registered user

)