package net.torosamy.torosamyItem.config;

import net.torosamy.torosamyCore.config.IConfigManage;

import java.util.List;

public class MainConfig implements IConfigManage {
    public List<String> blackContainer;

    public CatalogDefaultItem catalogDefaultItem = new CatalogDefaultItem();
    public class CatalogDefaultItem implements IConfigManage {
        public String display;
        public String material;
        public List<String> lore;
        public List<String> enchantment;
        public List<String> itemFlagList;
        public List<Integer> slots;
    }
}
