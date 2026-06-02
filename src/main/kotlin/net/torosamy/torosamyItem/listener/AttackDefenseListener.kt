package net.torosamy.torosamyItem.listener

import net.torosamy.torosamyItem.api.TorosamyItemAPI
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent

class AttackDefenseListener : Listener {
    @EventHandler
    fun onDefense(event: EntityDamageByEntityEvent) {
        val player = event.entity as? Player ?: return
        
        val equipment = player.equipment ?: return

        for (slot in TorosamyItemAPI.EQUIPMENT_SLOTS) {
            val item = equipment.getItem(slot)

            if (!TorosamyItemAPI.isTorosamyItem(item)) {
                continue
            }
            val customItem = TorosamyItemAPI.getCustomItem(TorosamyItemAPI.getItemKey(item)) ?: continue

            val holders = customItem.defense.command.copyHolder()
            
            holders["%defenser%"] = event.entity.name
            holders["%attacker%"] = event.damager.name
            
            customItem.defense.runCommands(player, slot, holders)
        }
    }

    @EventHandler
    fun onAttack(event: EntityDamageByEntityEvent) {
        val player = event.damager as? Player ?: return

        val equipment = player.equipment ?: return

        for (slot in TorosamyItemAPI.EQUIPMENT_SLOTS) {
            val item = equipment.getItem(slot)

            if (!TorosamyItemAPI.isTorosamyItem(item)) {
                continue
            }
            val customItem = TorosamyItemAPI.getCustomItem(TorosamyItemAPI.getItemKey(item)) ?: continue

            val holders = customItem.attack.command.copyHolder()

            holders["%defenser%"] = event.entity.name
            holders["%attacker%"] = event.damager.name
            
            customItem.attack.runCommands(player, slot, holders)
        }
    }
}