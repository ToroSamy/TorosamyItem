package net.torosamy.torosamyItem.pojo

import net.torosamy.torosamyCore.api.TorosamyCoreAPI
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import kotlin.collections.Map as Map

class CatalogInventory {
    private val rightCommands: HashMap<Int, List<String>> = HashMap()
    
    private val leftCommands: HashMap<Int, List<String>> = HashMap()
    
    private val denyCommands: HashMap<Int, Map<String, List<String>>> = HashMap()

    private val inventory: Inventory
    
    public constructor(inventory: Inventory) {
        this.inventory = inventory
    }
    
    public fun addLeftCommands(slot: Int, commands: List<String>) {
        this.leftCommands[slot] = commands
    }
    
    public fun addRightCommands(slot: Int, commands: List<String>) {
        this.rightCommands[slot] = commands
    }
    
    public fun addDenyCommands(slot: Int, commands: Map<String, List<String>>) {
        this.denyCommands[slot] = commands
    }

    public fun addItem(item: ItemStack, slot: Int, rightCommands: List<String>, leftCommands: List<String>, denyCommands: Map<String, List<String>>) {
        this.inventory.setItem(slot, item)
        addRightCommands(slot, rightCommands)
        addLeftCommands(slot, leftCommands)
        addDenyCommands(slot, denyCommands)
    }
    
    public fun open(player: Player) {
        if (!player.isOnline) {
            return
        }
        
        player.openInventory(inventory)
    }
    
    public fun runLeftCommands(slot: Int, player: Player) {
        val commands = this.leftCommands[slot] ?: return
        
        val denyCommands = this.denyCommands[slot] ?: emptyMap()
        
        TorosamyCoreAPI.runCommands(player, commands, denyCommands)
    }


    public fun runRightCommands(slot: Int, player: Player) {
        val commands = this.rightCommands[slot] ?: return

        val denyCommands = this.denyCommands[slot] ?: emptyMap()

        TorosamyCoreAPI.runCommands(player, commands, denyCommands)
    }
}