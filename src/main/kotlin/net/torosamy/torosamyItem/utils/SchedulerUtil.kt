package net.torosamy.torosamyItem.utils

import net.torosamy.torosamyItem.TorosamyItem
import net.torosamy.torosamyItem.scheduler.TimerTask

object SchedulerUtil {
    fun registerScheduler() {
        TimerTask().runTaskTimer(TorosamyItem.plugin, 0L, 20L)
    }
}