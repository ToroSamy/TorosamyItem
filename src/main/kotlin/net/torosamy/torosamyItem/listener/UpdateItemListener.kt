package net.torosamy.torosamyItem.listener


import net.torosamy.torosamyCore.utils.NbtUtil
import net.torosamy.torosamyItem.manager.ItemManager
import net.torosamy.torosamyItem.utils.ItemUtil
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.inventory.ItemStack


class UpdateItemListener : Listener {
    @EventHandler
    fun onPlayerItemHeld(event: PlayerItemHeldEvent) {
        val oldItem: ItemStack = event.player.inventory.getItem(event.newSlot) ?: return

        //判断物品是否是TorosamyItem
        if (!ItemUtil.isTorosamyItem(oldItem)) {
            return
        }

        //获取新的物品
        val newItem = ItemManager.items[ItemUtil.getConfigName(oldItem)]!!
        //如果未设置或者 设置为禁止
        if(newItem.update == null || newItem.update == false) return
        //如果HashCode没有发生变化
        if(newItem.hashCode == ItemUtil.getHashCode(oldItem)) return

        val item = ItemUtil.getItem(newItem, event.player)
        val itemMeta = item.itemMeta
        val type = item.type

        oldItem.type = type
        oldItem.itemMeta = itemMeta

        NbtUtil.setString(oldItem, ItemUtil.TOROSAMY_ITEM_KEY, newItem.key)
        NbtUtil.setInteger(oldItem, ItemUtil.TOROSAMY_HASH_CODE_KEY, newItem.hashCode)

    }
}