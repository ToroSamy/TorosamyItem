package net.torosamy.torosamyItem.listener


import net.torosamy.torosamyCore.api.TorosamyCoreAPI
import net.torosamy.torosamyCore.utils.MessageUtil
import net.torosamy.torosamyCore.utils.NbtUtil
import net.torosamy.torosamyItem.api.TorosamyItemAPI
import net.torosamy.torosamyItem.utils.ConfigUtil
import org.bukkit.Bukkit
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

        val itemKey = TorosamyItemAPI.getItemKey(oldItem)

        val customItem = TorosamyItemAPI.getCustomItem(itemKey)
        
        if (customItem == null) {
            if (ConfigUtil.mainConfig.removeUnloadItem) {
                Bukkit.getConsoleSender().sendMessage(MessageUtil.component(ConfigUtil.langConfig.removeItem
                    .replace("%player_name%", event.player.name)
                    .replace("%item%", itemKey)
                ))
                oldItem.amount = 0
            }
            return
        }

        if (!TorosamyCoreAPI.runCommands(event.player, customItem.disappear)) {
            Bukkit.getConsoleSender().sendMessage(MessageUtil.component(ConfigUtil.langConfig.removeItem
                .replace("%player_name%", event.player.name)
                .replace("%item%", itemKey)
            ))
            oldItem.amount = 0
            return
        }
        
        
        if(!customItem.update) {
            return
        }

        
        if(customItem.getHashCode() == TorosamyItemAPI.getHashCode(oldItem)) {
            return
        }
        
        val newItem = customItem.getItem()
        
        oldItem.type = newItem.type
        oldItem.itemMeta = newItem.itemMeta

        NbtUtil.setString(oldItem, TorosamyItemAPI.GET_TOROSAMY_ITEM_KEY(), customItem.getKey())
        NbtUtil.setInteger(oldItem, TorosamyItemAPI.GET_TOROSAMY_HASH_CODE_KEY(), customItem.getHashCode())
    }
}