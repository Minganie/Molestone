package shop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class for a series of tabs of a shop
 */
public class Tabs {
    private List<String> tabs = new ArrayList<>();
    private Map<String, List<String>> subTabs = new HashMap<>();

    /**
     * Constructor
     */
    public Tabs() {
    }

    /**
     * Add a parent tab to the list
     * @param name Name of the parent tab
     * @throws Exception for a duplicate tab
     */
    public void addTab(String name) throws Exception {
        if(tabs.contains(name))
            throw new Exception("Trying to add a tab (" + name + ") that is already there?");
        tabs.add(name);
        subTabs.put(name, new ArrayList<>());
    }

    /**
     * Add a child tab to the list
     * @param tab Parent tab
     * @param subtab Child tab
     * @throws Exception for a duplicate tab or subtab
     */
    public void addSubTab(String tab, String subtab) throws Exception {
        if(!tabs.contains(tab))
            throw new Exception("Trying to add a subtab (" + subtab + ") to a non-existent tab (" + tab + ")?");
        if(subTabs.get(tab).contains(subtab))
            throw new Exception("Trying to add a subtab (" + subtab + ") in tab (" + tab + ") that is already there?");
        subTabs.get(tab).add(subtab);
    }

    /**
     * Getter for the list of tab names
     * @return List of prent tab names
     */
    public List<String> getTabs() {
        return tabs;
    }

    /**
     * Getter for the tab and subtab names
     * @return Map of (Parent Tab, List{@literal <}Child Tabs{@literal >})
     */
    public Map<String, List<String>> getSubTabs() {
        return subTabs;
    }
}
