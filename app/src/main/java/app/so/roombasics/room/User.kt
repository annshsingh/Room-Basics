package app.so.roombasics.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(@PrimaryKey
                val tag: String,
                val userName: String)