package net.torosamy.torosamyItem.listener

import net.torosamy.torosamyItem.api.TorosamyItemAPI
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerItemConsumeEvent


class ItemConsumeListener : Listener{
    @EventHandler
    fun onPlayerConsume(event: PlayerItemConsumeEvent) {
        if (!TorosamyItemAPI.isTorosamyItem(event.item)) {
            return
        }
        
        val itemKey = TorosamyItemAPI.getItemKey(event.item)

        val customItem = TorosamyItemAPI.getCustomItem(itemKey) ?: return

        val player = event.player
        
        customItem.consume.execute(player)
        
        if (!customItem.consume.consume) {
            event.isCancelled = true
        }
    }
}