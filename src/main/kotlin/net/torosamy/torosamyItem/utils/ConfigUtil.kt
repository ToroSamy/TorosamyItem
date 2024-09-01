package net.torosamy.torosamyItem.utils

import net.torosamy.torosamyCore.manager.ConfigManager
import net.torosamy.torosamyItem.TorosamyItem
import net.torosamy.torosamyItem.config.LangConfig
import net.torosamy.torosamyItem.config.MainConfig

class ConfigUtil {
    companion object {
        var mainConfig: MainConfig = MainConfig()
        var langConfig: LangConfig = LangConfig()
        private var mainConfigManager: ConfigManager = ConfigManager(mainConfig,TorosamyItem.plugin,"","config.yml")
        private var langConfigManager: ConfigManager = ConfigManager(langConfig,TorosamyItem.plugin,"","lang.yml")

        fun reloadConfig() {
            mainConfigManager.load()
            langConfigManager.load()

        }
        fun saveConfig() {
            mainConfigManager.save()
            langConfigManager.save()
        }
    }
}