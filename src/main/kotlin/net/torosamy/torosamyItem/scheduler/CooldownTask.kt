package net.torosamy.torosamyItem.scheduler

import org.bukkit.scheduler.BukkitRunnable

class CooldownTask(var cooldown: Int) : BukkitRunnable() {
    override fun run() {
        if (cooldown == 0) {
            this.cancel()
            return
        }
        cooldown--
    }
}