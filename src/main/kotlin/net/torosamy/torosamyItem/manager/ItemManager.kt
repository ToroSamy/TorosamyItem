package net.torosamy.torosamyItem.manager



import net.torosamy.torosamyCore.manager.ConfigManager
import net.torosamy.torosamyCore.utils.MessageUtil
import net.torosamy.torosamyItem.TorosamyItem
import net.torosamy.torosamyItem.pojo.CustomItem
import net.torosamy.torosamyItem.pojo.ItemCommand
import org.bukkit.Bukkit
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration


class ItemManager {
    companion object {
        val items = HashMap<String, CustomItem>()

        fun loadItem(){
            items.clear()
            //一个配置文件可能由多个item
            loadItemData(ConfigManager.loadYamls(TorosamyItem.plugin,"Item",""));
            Bukkit.getConsoleSender().sendMessage(MessageUtil.text("&a[服务器娘]&a插件 &eTorosamyItem &a成功加载 &e${items.size} &a个物品喵~"))
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
                    //物品配置
                    customItem.displayName = sonItemYml.getString("displayName")
                    customItem.material = sonItemYml.getString("material")
                    customItem.lore = sonItemYml.getStringList("lore")
                    customItem.unbreakable = sonItemYml.getBoolean("unbreakable")
                    customItem.enchantList = sonItemYml.getStringList("enchantment")
                    customItem.itemFlagList = sonItemYml.getStringList("itemFlagList")
                    customItem.clearAttribute = sonItemYml.getBoolean("clearAttribute")
                    customItem.color = sonItemYml.getString("color")
                    customItem.leftConsume = sonItemYml.getBoolean("leftConsume")
                    customItem.rightConsume = sonItemYml.getBoolean("rightConsume")

                    //物品指令
                    val commandsYml = sonItemYml.getConfigurationSection("commands")
                    if (commandsYml != null) {
                        commandsYml.getKeys(false).forEach { key ->
                            commandsYml.getString("$key.command")?.let {
                                val itemCommand: ItemCommand = ItemCommand(key, it)
                                itemCommand.cooldown = commandsYml.getInt("$key.cooldown",0)
                                val permission = commandsYml.getString("$key.permission")
                                if(permission != null){itemCommand.permission = permission}
                                itemCommand.leftClick = commandsYml.getBoolean("$key.trigger.leftClick",false)
                                itemCommand.sneak = commandsYml.getBoolean("$key.trigger.sneak",false)
                                itemCommand.isConsole = commandsYml.getBoolean("$key.console",true)

                                customItem.commands[key] = itemCommand
                            }
                        }
                    }


                    //物品更新
                    customItem.update = sonItemYml.getBoolean("update")
                    val tempYamlForHashCode = YamlConfiguration()
                    sonItemYml.getValues(true).forEach(tempYamlForHashCode::set)
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