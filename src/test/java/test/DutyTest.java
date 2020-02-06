package test;

import duty.*;
import org.junit.Test;
import util.Lid;

import static junit.framework.TestCase.*;

public class DutyTest {

    @Test
    public void testTrial() throws Exception {
        Lid lid = new Lid("a74020d08e8");
        try {
            MaplessOneEncounterDuty dragonsNeck = (MaplessOneEncounterDuty) Duty.get(lid);
            assertEquals("The Dragon's Neck", dragonsNeck.getName());
            assertTrue(dragonsNeck.getBanner().contains("https://img.finalfantasyxiv.com/lds/pc/global/images/itemicon/94/94ef8852088c518f83c15cc8214c55ea49149666.png"));
            assertEquals(50, dragonsNeck.getLevel());
            assertEquals("Regular", dragonsNeck.getDifficulty().toString());
            assertEquals(1, dragonsNeck.getEncounters().size());
            Encounter encounter = dragonsNeck.getEncounters().get(0);
            assertEquals(2, encounter.getBossList().size());
            assertEquals(Integer.valueOf(15), encounter.getTokens().get("Allagan Tomestone of Poetics"));
            assertEquals(3, encounter.getLoot().size());
            assertEquals(0, dragonsNeck.getChests().size());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }


    @Test
    public void testDungeon() throws Exception {
        Lid lid = new Lid("339c4923556");
        try {
            Dungeon aery = (Dungeon) Duty.get(lid);
            assertEquals("The Aery", aery.getName());
            assertEquals(3, aery.getEncounters().size());
            assertEquals(6, aery.getChests().size());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }


    @Test
    public void testRaid() throws Exception {
        Lid lid = new Lid("aa718108575");
        try {
            Raid alpha2Savage = (Raid) Duty.get(lid);
            assertEquals(1, alpha2Savage.getEncounters().size());
            assertEquals(0, alpha2Savage.getChests().size());
            assertEquals(17, alpha2Savage.getEncounters().get(0).getLoot().size());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }


    @Test
    public void testGuildhest() throws Exception {
        Lid lid = new Lid("d5aad9309de");
        try {
            Guildhest stingingBack = (Guildhest) Duty.get(lid);
            assertEquals(Integer.valueOf(2030), stingingBack.getCompletionXp());
            assertEquals(Integer.valueOf(120), stingingBack.getCompletionGil());
            assertEquals(Integer.valueOf(8120), stingingBack.getBonusXp());
            assertEquals(Integer.valueOf(2380), stingingBack.getBonusGil());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }


    @Test
    public void testPvp() throws Exception {
        Lid lid = new Lid("0c83c5e8c0b");
        try {
            PvP shatter = (PvP) Duty.get(lid);
            assertEquals(3, shatter.getTokens().size());
            assertEquals(Integer.valueOf(1000), shatter.getPvpXpRank1());
            assertEquals(Integer.valueOf(750), shatter.getPvpXpRank2());
            assertEquals(Integer.valueOf(500), shatter.getPvpXpRank3());
            assertEquals(Integer.valueOf(1000), shatter.getWolfMarksRank1());
            assertEquals(Integer.valueOf(750), shatter.getWolfMarksRank2());
            assertEquals(Integer.valueOf(500), shatter.getWolfMarksRank3());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testUnnumberedChest() throws Exception {
        Lid lid = new Lid("fade6531524");
        try {
            Duty bahamut = Duty.get(lid);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testNpcWithoutAreaAndCoords() throws Exception {
        Lid lid = new Lid("820a11812ab");
        try {
            Duty bahamut = Duty.get(lid);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testPvpWinnerLoser() throws Exception {
        Lid lid = new Lid("c920bd2da8f");
        try {
            Duty feast = Duty.get(lid);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}
