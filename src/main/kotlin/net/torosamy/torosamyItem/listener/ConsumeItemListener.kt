package net.torosamy.torosamyItem.listener


import net.torosamy.torosamyItem.api.TorosamyItemAPI
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

class ConsumeItemListener : Listener {
    @EventHandler
    fun onPlayerAction(event: PlayerInteractEvent) {
        val oldItemStack = event.item ?: return
        
        val action: Action = event.getAction()
        val material: Material = event.getMaterial()
        
        if (action == Action.PHYSICAL || material == Material.AIR) {
            return
        }

        
        val isRightClick: Boolean = (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)
        val isLeftClick: Boolean = (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK)
        
        val item = event.item ?: return
        
        if (!TorosamyItemAPI.isTorosamyItem(item)) {
            return
        }
        
        val customItem = TorosamyItemAPI.getCustomItem(TorosamyItemAPI.getConfigName(item)) ?: return

        if (customItem.leftConsume && isLeftClick) {
            oldItemStack.amount--
            return
        }

        if (customItem.rightConsume && isRightClick) { 
            oldItemStack.amount-- 
        }
    }
}