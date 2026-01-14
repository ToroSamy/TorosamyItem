package net.torosamy.torosamyItem.pojo.command.sub

import net.torosamy.torosamyItem.pojo.command.SubCommand
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player

class Consume {
    val consume: Boolean
    
    val command: SubCommand
    
    public constructor(config: ConfigurationSection?) {
        this.command = SubCommand(config)
        
        if (config == null) {
            this.consume = true
            return 
        }
        
        this.consume = config.getBoolean("consume", true)
    }
    
    public fun execute(player: Player) {
        this.command.runCommands(player)
    }
}