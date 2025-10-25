package net.torosamy.torosamyItem.pojo

import net.torosamy.torosamyCore.cooldown.CooldownTask
import net.torosamy.torosamyCore.utils.MessageUtil
import net.torosamy.torosamyItem.TorosamyItem

import org.bukkit.Bukkit
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player

class ItemCommand private constructor(key: String, config: ConfigurationSection) {
    val key: String
    val command: String
    val cooldown: Int
    val leftClick: Boolean
    val sneak: Boolean
    val permission: String?
    val isConsole: Boolean
    val playerCooldown = HashMap<Player, CooldownTask>()

    init {
        this.key = key
        this.cooldown = config.getInt("cooldown",0)
        this.permission = config.getString("permission", null)
        this.leftClick = config.getBoolean("trigger.leftClick", false)
        this.sneak = config.getBoolean("trigger.sneak", false)
        this.isConsole = config.getBoolean("console", true)
        this.command = config.getString("command")!!
    }
    
    fun updateCooldownTask(player: Player): Boolean {
        if (cooldown <= 0) {
            return true
        }

        val cooldownTask = playerCooldown[player]
        
        if (cooldownTask == null) {
            val newTask = CooldownTask(cooldown)
            newTask.runTaskTimer(TorosamyItem.plugin,0L,20L)
            playerCooldown[player] = newTask
            return true
        }
        
        if (cooldownTask.isFinish()) {
            playerCooldown.remove(player)
            return true;
        }
        return false
    }
    
    fun getRemainingTime(player: Player): Int {
        if (cooldown <= 0) {
            return 0
        }

        val cooldownTask = playerCooldown[player] ?: return 0
        
        if (cooldownTask.isFinish()) {
            return 0
        }
        
        return cooldownTask.cooldown;
    }
    
    fun execute(player: Player) {
        val executeCommand = MessageUtil.format(player, command)
        if (isConsole) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), executeCommand)
            return
        }
        
        if(player.isOp) {
            Bukkit.dispatchCommand(player, executeCommand)
            return
        }

        player.isOp = true
        Bukkit.dispatchCommand(player, executeCommand)
        player.isOp = false
    }

    companion object {
        fun generateCommandGroup(config: ConfigurationSection?): HashMap<String, ItemCommand> {
            val result: HashMap<String, ItemCommand> = HashMap()
            
            if (config == null) {
                return result   
            }

            for (it in config.getKeys(false)) {
                val section = config.getConfigurationSection(it) ?: continue

                if (section.getString("command") == null) {
                    continue
                }
                result[it] = ItemCommand(it, section)
            }
            return result
        }
    }
}