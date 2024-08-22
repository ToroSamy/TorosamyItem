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

                    val sonItemYml = itemYml.getConfigurationSection(itemConfigString)!!
                    //创建一个custom对象用来储存配置文件里的数据
                    val customItem = CustomItem()
                    customItem.displayName = sonItemYml.getString("displayName")
                    customItem.material = sonItemYml.getString("material")
                    customItem.lore = sonItemYml.getStringList("lore")
                    customItem.unbreakable = sonItemYml.getBoolean("unbreakable")
                    customItem.enchantList = sonItemYml.getStringList("enchantment")
                    customItem.update = sonItemYml.getBoolean("update")
                    customItem.itemFlagList = sonItemYml.getStringList("itemFlagList")
                    customItem.clearAttribute = sonItemYml.getBoolean("clearAttribute")
                    customItem.color = sonItemYml.getString("color")

                    val tempYamlForHashCode = YamlConfiguration()
                    sonItemYml.getValues(false).forEach(tempYamlForHashCode::set)
                    val saveToString = tempYamlForHashCode.saveToString()
                    //去掉最后一个字符后的字符串
                    customItem.configString = saveToString.substring(0, saveToString.length - 1)
                    customItem.hashCode = customItem.configString.hashCode()
                    customItem.key = itemConfigString

                    items[itemConfigString] = customItem
                }
            }
        }
    }





}