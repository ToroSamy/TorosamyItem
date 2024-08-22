package net.torosamy.torosamyItem.manager


import net.torosamy.torosamyCore.utils.MessageUtil
import net.torosamy.torosamyItem.TorosamyItem
import net.torosamy.torosamyItem.pojo.CustomItem
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

class ItemManager() {
    companion object {
        val itemDirectory = File(TorosamyItem.plugin.dataFolder, "Item");
        val items = HashMap<String, CustomItem>()

        fun loadItem(){
            items.clear()
            //一个配置文件可能由多个item
            val itemYmls = HashMap<String, ConfigurationSection>()
            loadItemConfigs(itemDirectory, itemYmls, "");
            loadItemData(itemYmls);
            TorosamyItem.plugin.server.consoleSender.sendMessage(MessageUtil.text("&cLoaded &e${items.size} &citems"))
        }

        /**
         * 加载所有的item.yml
         */
        private fun loadItemConfigs(itemDirectory: File, itemYmls: HashMap<String, ConfigurationSection>, path: String) {
            if(!itemDirectory.exists()) itemDirectory.mkdirs()
            for (file in itemDirectory.listFiles()) {
                val filePath = if (path.isNotEmpty()) path + File.separator + file.name else file.name
                if (file.isDirectory) loadItemConfigs(file, itemYmls, filePath)
                else if (file.name.endsWith(".yml")) itemYmls[filePath] = YamlConfiguration.loadConfiguration(file)
            }
        }

        /**
         * 将每个.yml文件里所有的item的读取到内存区当中
         */
        private fun loadItemData(itemYmls: HashMap<String, ConfigurationSection>) {
            //遍历每个.yml文件
            for (itemYml in itemYmls.values) {
                //遍历每个.yml里面的item配置
                for (itemConfigString in itemYml.getKeys(false)) {
                    //创建一个custom对象用来储存配置文件里的数据
                    val customItem: CustomItem = CustomItem()
                    customItem.displayName = itemYml.getString("$itemConfigString.displayName")
                    customItem.material = itemYml.getString("$itemConfigString.material")
                    customItem.lore = itemYml.getStringList("$itemConfigString.lore")
                    customItem.unbreakable = itemYml.getBoolean("$itemConfigString.unbreakable")
                    customItem.enchantList = itemYml.getStringList("$itemConfigString.enchantment")
                    customItem.update = itemYml.getBoolean("$itemConfigString.update")
                    customItem.itemFlagList = itemYml.getStringList("$itemConfigString.itemFlagList")
                    customItem.clearAttribute = itemYml.getBoolean("$itemConfigString.clearAttribute")
                    customItem.color = itemYml.getString("$itemConfigString.color")
                    items[itemConfigString] = customItem
                }
            }
        }
    }





}