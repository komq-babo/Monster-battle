package io.github.koba.monsterbattle

import io.github.monun.kommand.PluginKommand
import io.github.monun.kommand.getValue
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Snowball
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin

object KommandMonster {
    private fun getInstance(): Plugin {
        return MonsterBattlePlugin.instance
    }

    private lateinit var plugin: MonsterBattlePlugin

    internal fun register(plugin: MonsterBattlePlugin, kommand: PluginKommand) {
        KommandMonster.plugin = plugin

        kommand.register("battle") {
            requires { isPlayer && isOp }
            then("start") {
                then("a" to summonableEntity()) {
                    then("b" to summonableEntity()) {
                        executes {
                            val a: NamespacedKey by it
                            val b: NamespacedKey by it

                            val aLocation = getInstance().config.getLocation("a.spawn")
                            val bLocation = getInstance().config.getLocation("b.spawn")

                            val aEntity = player.world.spawnEntity(aLocation!!, EntityType.fromName(a.key)!!) as LivingEntity
                            val bEntity = player.world.spawnEntity(bLocation!!, EntityType.fromName(b.key)!!) as LivingEntity

                            aEntity.equipment?.helmet = ItemStack(Material.STONE_BUTTON)
                            bEntity.equipment?.helmet = ItemStack(Material.STONE_BUTTON)

                            val aSnowBall = player.world.spawn(Location(player.world, aLocation.x, aLocation.y + 3, aLocation.z, aLocation.yaw, aLocation.pitch), Snowball::class.java)
                            val bSnowBall = player.world.spawn(Location(player.world, bLocation.x, bLocation.y + 3, bLocation.z, bLocation.yaw, bLocation.pitch), Snowball::class.java)

                            aSnowBall.shooter = bEntity
                            bSnowBall.shooter = aEntity
                        }
                    }
                }
            }

            then("spawn") {
                then("monster" to string()) {
                    executes {
                        val monster: String by it
                        getInstance().config.set("${monster}.spawn", player.location)
                        getInstance().saveConfig()

                        player.sendMessage("spawn ${monster}=${player.location}")
                    }
                }
            }
        }
    }
}
