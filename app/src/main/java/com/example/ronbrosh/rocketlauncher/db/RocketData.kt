package com.example.ronbrosh.rocketlauncher.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.ronbrosh.rocketlauncher.model.Engine
import com.example.ronbrosh.rocketlauncher.model.Rocket
import java.util.*

@Entity(tableName = "rocketData")
data class RocketData(
        @PrimaryKey(autoGenerate = true) var id: Long?
) : Rocket("", "", Engine(0)) {
    override fun equals(other: Any?): Boolean {
        (other as RocketData).let {
            return when {
                it.name != name -> false
                it.country != country -> false
                it.engine.enginesCount != engine.enginesCount -> false
                else -> true
            }
        }
    }

    override fun hashCode(): Int {
        return Objects.hash(id, name, country, engine.enginesCount)
    }
}