package shop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tabs {
    private List<String> tabs = new ArrayList<>();
    private Map<String, List<String>> subTabs = new HashMap<>();

    public Tabs() {
    }

    public void addTab(String name) throws Exception {
        if(tabs.contains(name))
            throw new Exception("Trying to add a tab (" + name + ") that is already there?");
        tabs.add(name);
        subTabs.put(name, new ArrayList<>());
    }

    public void addSubTab(String tab, String subtab) throws Exception {
        if(!tabs.contains(tab))
            throw new Exception("Trying to add a subtab (" + subtab + ") to a non-existent tab (" + tab + ")?");
        if(subTabs.get(tab).contains(subtab))
            throw new Exception("Trying to add a subtab (" + subtab + ") in tab (" + tab + ") that is already there?");
        subTabs.get(tab).add(subtab);
    }

    public List<String> getTabs() {
        return tabs;
    }

    public Map<String, List<String>> getSubTabs() {
        return subTabs;
    }
}
