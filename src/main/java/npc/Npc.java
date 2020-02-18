package npc;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import util.JsoupUtils;
import util.Lid;
import util.ZonedCoords;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public abstract class Npc {
    protected String cat;
    protected Lid lid;
    protected String name;

    // Enemy
    protected List<SpawnLocation> spawnLocations = new ArrayList<>();
    protected List<Lid> droppedItems = new ArrayList<>();
    protected boolean conditionalSpawner = false;
    protected List<Lid> relatedDuties = new ArrayList<>();

    // NPC
    protected ZonedCoords location = null;
    protected List<Lid> availableQuests = new ArrayList<>();
    protected List<Lid> relatedQuests = new ArrayList<>();

    protected Npc(String cat, Lid lid) throws Exception {
        this.cat = cat;
        this.lid = lid;
        String urlPart;
        if(cat.equals("Enemies"))
            urlPart = "enemy";
        else if(cat.contains("NPC"))
            urlPart = "npc";
        else
            throw new Exception("Can't figure out npc type from '" + cat + "'");
        URL url = new URL("https://na.finalfantasyxiv.com/lodestone/playguide/db/npc/" + urlPart + "/" + lid.toString());
        Document doc = Jsoup.connect(url.toString()).get();
        name = JsoupUtils.firstNonEmptyTextNode(doc.selectFirst(".db-view__detail__npc_name"));
        parseDetails(doc);
    }

    public static Npc get(String cat, Lid lid) throws Exception {
        if(cat.equals("Enemies"))
            return new Enemy(cat, lid);
        if(cat.contains("NPC"))
            return new NpcNpc(cat, lid);
        throw new Exception("Unable to figure out subtype of npc from '" + cat + "'");
    }

    abstract void parseDetails(Document doc) throws Exception;

    public String getCat() {
        return cat;
    }

    public Lid getLid() {
        return lid;
    }

    public String getName() {
        return name;
    }

    public List<SpawnLocation> getSpawnLocations() {
        return spawnLocations;
    }

    public List<Lid> getDroppedItems() {
        return droppedItems;
    }

    public boolean isConditionalSpawner() {
        return conditionalSpawner;
    }

    public List<Lid> getRelatedDuties() {
        return relatedDuties;
    }

    public ZonedCoords getLocation() {
        return location;
    }

    public List<Lid> getAvailableQuests() {
        return availableQuests;
    }

    public List<Lid> getRelatedQuests() {
        return relatedQuests;
    }

    @Override
    public String toString() {
        return name + " @ " + lid + " (" + cat + ")";
    }
}
