package net.torosamy.torosamyItem.config;

import net.torosamy.torosamyCore.config.IConfigManage;

import java.util.List;

public class MainConfig implements IConfigManage {
    public List<String> blackContainer;
    public String amountError;
    public Boolean removeUnloadItem;
    
    public CatalogDefaultItem catalogDefaultItem = new CatalogDefaultItem();
    public class CatalogDefaultItem implements IConfigManage {
//        public List<Integer> slots;
        public List<String> rightCommands;
        public List<String> leftCommands;
    }
}
