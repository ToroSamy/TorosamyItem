package net.torosamy.torosamyItem.pojo.command.sub

import net.torosamy.torosamyItem.pojo.command.SubCommand
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player

class Interact private constructor(key: String, config: ConfigurationSection) {
    val key: String
    val cooldown: Int
    val leftClick: Boolean
    val subCommand: SubCommand
    val playerCooldown = HashMap<String, Long>()
    val sneak: Boolean
    val onlyMainHand: Boolean
    
    init {
        this.key = key
        this.cooldown = config.getInt("cooldown",0)
        this.leftClick = config.getBoolean("leftClick", false)
        this.sneak = config.getBoolean("sneak", false)
        this.onlyMainHand = config.getBoolean("onlyMainHand", true)
        this.subCommand = SubCommand(config)
    }
    
    fun updateCooldown(player: Player): Long {
        if (cooldown <= 0) {
            return 0
        }

        val lastExecuteTime = playerCooldown[player.name]

        val nowTime = System.currentTimeMillis()

        if (lastExecuteTime == null) {
            playerCooldown[player.name] = nowTime
            return 0
        }


        val remainTime = (nowTime - lastExecuteTime) / 1000 - cooldown

        if (remainTime > 0) {
            playerCooldown[player.name] = nowTime
            return 0
        }
        
        if (remainTime == 0L) {
            return remainTime + 1
        }

        return -remainTime
    }
    
    fun execute(player: Player) {
        subCommand.runCommands(player)
    }

    companion object {
        fun generateCommandGroup(config: ConfigurationSection?): HashMap<String, Interact> {
            val result: HashMap<String, Interact> = HashMap()
            
            if (config == null) {
                return result   
            }

            for (it in config.getKeys(false)) {
                val section = config.getConfigurationSection(it) ?: continue
                
                result[it] = Interact(it, section)
            }
            return result
        }
    }
}