package net.torosamy.torosamyItem.listener


import net.torosamy.torosamyCore.utils.NbtUtil
import net.torosamy.torosamyItem.api.TorosamyItemAPI
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.inventory.ItemStack


class UpdateItemListener : Listener {
    @EventHandler
    fun onPlayerItemHeld(event: PlayerItemHeldEvent) {
        val oldItem: ItemStack = event.player.inventory.getItem(event.newSlot) ?: return
        
        if (!TorosamyItemAPI.isTorosamyItem(oldItem)) {
            return
        }
        
        val newItem = TorosamyItemAPI.getCustomItem(TorosamyItemAPI.getConfigName(oldItem)) ?: return

        if(!newItem.update) {
            return
        }

        
        if(newItem.getHashCode() == TorosamyItemAPI.getHashCode(oldItem)) {
            return
        }

        val item = newItem.itemStack
        
        oldItem.type = item.type
        oldItem.itemMeta = item.itemMeta

        NbtUtil.setString(oldItem, TorosamyItemAPI.GET_TOROSAMY_ITEM_KEY(), newItem.getKey())
        NbtUtil.setInteger(oldItem, TorosamyItemAPI.GET_TOROSAMY_HASH_CODE_KEY(), newItem.getHashCode())

    }
}