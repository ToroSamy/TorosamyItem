package net.torosamy.torosamyItem.scheduler

import net.torosamy.torosamyItem.api.TorosamyItemAPI
import net.torosamy.torosamyItem.api.TorosamyItemAPI.isTorosamyItem
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable

class TimerTask : BukkitRunnable() {
    override fun run() {
        for (player in Bukkit.getOnlinePlayers()) {
            val equipment = player.equipment ?: continue

            for (slot in TorosamyItemAPI.EQUIPMENT_SLOTS) {
                val item = equipment.getItem(slot)

                if (!isTorosamyItem(item)) {
                    continue
                }
                val customItem = TorosamyItemAPI.getCustomItem(TorosamyItemAPI.getItemKey(item)) ?: continue

                customItem.timer.runCommands(player, slot)
            }
        }
    }
}