package test;

import npc.Enemy;
import npc.Npc;
import npc.SpawnLocation;
import org.junit.Test;
import util.Coords;
import util.Lid;
import util.ZonedCoords;

import static org.junit.Assert.*;

public class NpcTest {

    @Test
    public void testEnemy() throws Exception {
        Lid lid = new Lid("e73dd29529b");
        try {
            Enemy garlean = Enemy.get(lid);
            assertEquals("e73dd29529b", garlean.getLid().get());
            assertEquals("3rd Cohort Laquearius", garlean.getName());
            assertEquals(0, garlean.getRelatedDuties().size());
            // Spawn locations
            assertEquals(4, garlean.getSpawnLocations().size());
            SpawnLocation es = new SpawnLocation("East Shroud", 42, 42, false);
            SpawnLocation ss = new SpawnLocation("South Shroud", 22, 22, true);
            SpawnLocation nt = new SpawnLocation("Northern Thanalan", 49, 49, false);
            SpawnLocation cch = new SpawnLocation("Coerthas Central Highlands", 39, 39, true);
            assertTrue(garlean.getSpawnLocations().contains(es));
            assertTrue(garlean.getSpawnLocations().contains(ss));
            assertTrue(garlean.getSpawnLocations().contains(nt));
            assertTrue(garlean.getSpawnLocations().contains(cch));
            // Items
            assertEquals(3, garlean.getDroppedItems().size());
            assertTrue(garlean.getDroppedItems().contains(new Lid("1583250b7dc")));
            assertTrue(garlean.getDroppedItems().contains(new Lid("73956562081")));
            assertTrue(garlean.getDroppedItems().contains(new Lid("d3559d1b51c")));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testTwoLevelEnemy() throws Exception {
        Lid lid = new Lid("0949d366cab");
        try {
            Enemy coblyn = Enemy.get(lid);
            SpawnLocation wt = new SpawnLocation("Western Thanalan", 6, 8, false);
            assertEquals(1, coblyn.getSpawnLocations().size());
            assertTrue(coblyn.getSpawnLocations().contains(wt));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testDutyBoss() throws Exception {
        Lid lid = new Lid("aee1959475b");
        try {
            Enemy livia = Enemy.get(lid);
            assertEquals(0, livia.getDroppedItems().size());
            assertEquals(1, livia.getRelatedDuties().size());
            assertTrue(livia.getRelatedDuties().contains(new Lid("59c2b3b84fa")));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testNpc() throws Exception {
        Lid lid = new Lid("52a6ba85e3f");
        try {
            Npc konogg = Npc.get(lid);
            assertEquals("52a6ba85e3f", konogg.getLid().get());
            assertEquals("Konogg", konogg.getName());
            // Location
            ZonedCoords zc = new ZonedCoords(new Coords(13.4, 17.5), "The Copied Factory");
            assertEquals(zc, konogg.getLocation());
            // Available quests
            assertEquals(1, konogg.getAvailableQuests().size());
            assertTrue(konogg.getAvailableQuests().contains(new Lid("989bdf3c51c")));
            // Related quests
            assertEquals(2, konogg.getRelatedQuests().size());
            assertTrue(konogg.getRelatedQuests().contains(new Lid("8169b41d06c")));
            assertTrue(konogg.getRelatedQuests().contains(new Lid("72a6f70a79f")));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}
