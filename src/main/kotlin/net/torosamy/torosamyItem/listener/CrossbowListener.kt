package net.torosamy.torosamyItem.listener

import net.torosamy.torosamyItem.api.TorosamyItemAPI
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityShootBowEvent


class CrossbowListener : Listener {
    @EventHandler
    fun onCrossbowShoot(event: EntityShootBowEvent) {
        val bow = event.bow
        
        if (bow == null || bow.type != Material.CROSSBOW) {
            return
        }

        if (!TorosamyItemAPI.isTorosamyItem(bow)) {
            return
        }

        val customItem = TorosamyItemAPI.getCustomItem(TorosamyItemAPI.getItemKey(bow)) ?: return
        
        if (!customItem.preventShoot) {
            return
        }

        event.isCancelled = true
    }
}