package test;

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
        Lid lid = new Lid("d273666f5e5");
        try {
            Npc doblyn = Npc.get("Enemies", lid);
            assertEquals("Enemies", doblyn.getCat());
            assertEquals("Quartz Doblyn", doblyn.getName());
            assertFalse(doblyn.isConditionalSpawner());
            assertEquals(0, doblyn.getRelatedDuties().size());
            // Spawn locations
            assertEquals(2, doblyn.getSpawnLocations().size());
            SpawnLocation et = new SpawnLocation("Eastern Thanalan", 41, 44);
            SpawnLocation nt = new SpawnLocation("Northern Thanalan", 49, 49);
            assertTrue(doblyn.getSpawnLocations().contains(et));
            assertTrue(doblyn.getSpawnLocations().contains(nt));
            // Items
            assertEquals(1, doblyn.getDroppedItems().size());
            assertTrue(doblyn.getDroppedItems().contains(new Lid("ad4b9905828")));


            // NPC NPC thingies
            assertNull(doblyn.getLocation());
            assertEquals(0, doblyn.getAvailableQuests().size());
            assertEquals(0, doblyn.getRelatedQuests().size());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testDutyBoss() throws Exception {
        Lid lid = new Lid("aee1959475b");
        try {
            Npc livia = Npc.get("Enemies", lid);
            assertEquals(0, livia.getDroppedItems().size());
            assertEquals(1, livia.getRelatedDuties().size());
            assertTrue(livia.getRelatedDuties().contains(new Lid("59c2b3b84fa")));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testConditionalSpawner() throws Exception {
        Lid lid = new Lid("ec0770a6bf8");
        try {
            Npc buffalo = Npc.get("Enemies", lid);
            assertTrue(buffalo.isConditionalSpawner());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testNpc() throws Exception {
        Lid lid = new Lid("52a6ba85e3f");
        try {
            Npc konogg = Npc.get("Event NPC", lid);
            assertEquals("Event NPC", konogg.getCat());
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

            // Enemy thingies
            assertEquals(0, konogg.getSpawnLocations().size());
            assertEquals(0, konogg.getDroppedItems().size());
            assertFalse(konogg.isConditionalSpawner());
            assertEquals(0, konogg.getRelatedDuties().size());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}
