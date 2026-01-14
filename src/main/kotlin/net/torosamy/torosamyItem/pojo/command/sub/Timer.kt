package net.torosamy.torosamyItem.pojo.command.sub

import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player
import org.bukkit.inventory.EquipmentSlot

class Timer{
    var counts: Int = 0
    
    val seconds: Int
    
    val command: SlotCommand
    
    public constructor(config: ConfigurationSection?) {
        this.command = SlotCommand(config)
        
        if (config == null) {
            this.seconds = -1
            return
        }

        this.seconds = config.getInt("seconds", -1)
    }

    public fun runCommands(player: Player, slot: EquipmentSlot) {
        if (!player.isOnline) {
            return
        }

        if (seconds == -1) {
            return
        }

        if (counts != seconds) {
            counts++
            return
        }

        counts = 0
        
        command.runCommands(player, slot)
    }
}