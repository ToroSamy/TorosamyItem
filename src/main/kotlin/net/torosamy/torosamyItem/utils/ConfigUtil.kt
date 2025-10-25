package net.torosamy.torosamyItem.utils

import net.torosamy.torosamyCore.config.Config
import net.torosamy.torosamyCore.config.ConfigFile
import net.torosamy.torosamyItem.TorosamyItem
import net.torosamy.torosamyItem.config.LangConfig
import net.torosamy.torosamyItem.config.MainConfig

class ConfigUtil {
    companion object {
        private val configs: ArrayList<Config> = ArrayList()

        public var mainConfig: MainConfig = MainConfig()
        public var langConfig: LangConfig = LangConfig()


        fun initConfig() {
            configs.clear()
            configs.add(Config(mainConfig, ConfigFile(TorosamyItem.plugin,"config.yml")))
            configs.add(Config(langConfig, ConfigFile(TorosamyItem.plugin,"lang.yml")))
        }

        fun reloadConfig() {
            for (config in configs) {
                config.load()
            }
        }

        fun saveConfig() {
            for (config in configs) {
                config.save()
            }
        }
    }
}