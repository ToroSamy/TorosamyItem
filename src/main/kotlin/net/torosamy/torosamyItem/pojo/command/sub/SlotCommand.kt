package net.torosamy.torosamyItem.pojo.command.sub

import net.torosamy.torosamyItem.pojo.command.SubCommand
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player
import org.bukkit.inventory.EquipmentSlot

class SlotCommand {
    val slot: EquipmentSlot?

    val command: SubCommand

    constructor(config: ConfigurationSection?) {
        this.command = SubCommand(config)

        if (config == null) {
            this.slot = null
            return
        }
        
        val slot = config.getString("slot", null)

        if (slot == null) {
            this.slot = null
            return
        }

        this.slot = EquipmentSlot.valueOf(slot.uppercase())
    }

    public fun runCommands(player: Player, slot: EquipmentSlot, holders: HashMap<String, String>) {
        if (!player.isOnline) {
            return
        }
        
        if (this.slot != null && slot != this.slot) {
            return
        }
        
        if (holders.isEmpty()) {
            command.runCommands(player)
            return
        }
        
        command.runCommands(player, holders)
    }

    public fun runCommands(player: Player, slot: EquipmentSlot) {
        runCommands(player, slot, hashMapOf())
    }
}