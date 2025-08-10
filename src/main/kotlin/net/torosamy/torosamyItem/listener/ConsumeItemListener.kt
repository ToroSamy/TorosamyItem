package net.torosamy.torosamyItem.listener


import net.torosamy.torosamyItem.manager.ItemManager
import net.torosamy.torosamyItem.utils.ItemUtil
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

class ConsumeItemListener : Listener {
    @EventHandler
    fun onPlayerAction(event: PlayerInteractEvent) {
        val player: Player = event.getPlayer()
        val action: Action = event.getAction()
        val material: Material = event.getMaterial()
        val oldItemStack = event.item ?: return
        if (action == Action.PHYSICAL || material == Material.AIR) return



        val isRightClick: Boolean = (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)
        val isLeftClick: Boolean = (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK)

        //若手上没有物品则返回
        val item = event.item ?: return
        //判断物品是否是TorosamyItem
        if (!ItemUtil.isTorosamyItem(item)) {
            return
        }

        //获取内存当中的物品
        val customItem = ItemManager.items[ItemUtil.getConfigName(item)]!!

        if (customItem.leftConsume && isLeftClick) {
            oldItemStack.amount--
            return
        }

        if (customItem.rightConsume && isRightClick) { oldItemStack.amount-- }
    }
}