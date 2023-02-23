package io.github.koba.monsterbattle

import io.github.monun.kommand.kommand
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class MonsterBattlePlugin: JavaPlugin() {

    companion object {
        lateinit var instance : MonsterBattlePlugin
    }

    init {
        instance = this
    }

    override fun onEnable() {
        saveConfig()
        val configFile = File(dataFolder, "config.yml")

        if (configFile.length() == 0L) {
            config.options().copyDefaults(true)
            saveConfig()
        }

        setupCommands()
    }

    private fun setupCommands() = kommand {
        KommandMonster.register(this@MonsterBattlePlugin, this)
    }
}