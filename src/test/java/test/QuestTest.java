package test;

import org.junit.Test;
import quest.Quest;
import util.*;

import java.awt.*;
import java.util.Map;

import static org.junit.Assert.*;

public class QuestTest {

    /**
     * Test that the category, title, area, level, Lodestone id, Lodestone banner url and quest giver are correctly
     * parsed
     */
    @Test
    public void testGeneralInfo() throws Exception {
        Lid lid = new Lid("d97e230e9bd");
        try {
            Quest skeletons = Quest.get(lid);
            NPC ursandel = new NPC("Ursandel", new Lid("a6d157b4f1b"), "Old Gridania", new Coords(11.9, 4.5));
            assertEquals("Seventh Umbral Era", skeletons.getCategory());
            assertEquals("Skeletons in Her Closet", skeletons.getTitle());
            assertEquals("Old Gridania", skeletons.getArea());
            assertEquals(28, skeletons.getLevel());
            assertEquals("d97e230e9bd", skeletons.getLid().toString());
            assertTrue(skeletons.getBannerUrl().contains("https://img.finalfantasyxiv.com/lds/pc/global/images/itemicon/ac/ac35585945b4d5ad9861251c4a2123fc690b1604.png?"));
            assertEquals(ursandel, skeletons.getQuestGiver());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test that starting class, class and level requirements, and completed duty/quest requirements are correctly
     * parsed
     */
    @Test
    public void testRestrictions() throws Exception {
        Lid startLid = new Lid("1da75996ae6");
        Lid questLid = new Lid("17764a8aa4a");
        Lid dutyLimLid = new Lid("3fe58aa572c");
        try {
            Job lancer = Job.getFromName("Lancer");
            Quest startQuest = Quest.get(startLid);
            Quest classQuest = Quest.get(questLid);
            Quest dutyLimited = Quest.get(dutyLimLid);
            assertEquals(lancer, startQuest.getStartingClass());
            assertEquals("LNC", classQuest.getClassRequirement());
            assertEquals(Integer.valueOf(10), classQuest.getLevelRequirement());
            assert (dutyLimited.getRequiredDuties().size() > 0);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test that Grand Company and Grand Company grandCompanyRank requirements are correctly parsed
     */
    @Test
    public void testGrandCompany() throws Exception {
        Lid gcLid = new Lid("f77f912eeff");
        Lid gcrankLid = new Lid("b08d7bf6a75");
        try {
            GrandCompany maelstrom = GrandCompany.get("Maelstrom");
            Quest gconly = Quest.get(gcLid);
            Quest gcandrank = Quest.get(gcrankLid);
            assertEquals(maelstrom, gconly.getGrandCompany());
            assertEquals("Sergeant First Class", gcandrank.getGrandCompanyRank().toString());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test that "number rewards" are correctly parsed, that is xp, gil, grandCompanySeals, ventures, beast tribe currency,
     * beast tribe reputation, and tomestones
     */
    @Test
    public void testNumberRewards() throws Exception {
        Lid gcrankLid = new Lid("b08d7bf6a75");
        Lid btLid = new Lid("d3815ff7d43");
        Lid btCurLid = new Lid("7ea536aec62");
        Lid severalRewardsLid = new Lid("02f039d7119");
        Lid dutyLimitedLid = new Lid("3fe58aa572c");
        Lid namazuLid = new Lid("2500840e7fd");
        try {
            Quest gcandrank = Quest.get(gcrankLid);
            Quest btdaily = Quest.get(btLid);
            Quest btwithcurrency = Quest.get(btCurLid);
            Quest severalRewards = Quest.get(severalRewardsLid);
            Quest dutyLimited = Quest.get(dutyLimitedLid);
            Quest namazuQuest = Quest.get(namazuLid);

            assertEquals(Integer.valueOf(12190), gcandrank.getXp());
            assertEquals(Integer.valueOf(0), gcandrank.getGil());
            assertEquals(Integer.valueOf(1265), gcandrank.getGrandCompanySeals());

            assertEquals(Integer.valueOf(1), btdaily.getVentures());
            StringCountBag scbc = btdaily.getBeastTribeCurrency();
            assertEquals(0, scbc.getCount());
            assertNull(scbc.getThingy());
            assertEquals(Integer.valueOf(10), btdaily.getBeastTribeReputation());
            StringCountBag scbt = btdaily.getTomestones();
            assertEquals(5, scbt.getCount());
            assertEquals("Allagan Tomestone of Poetics", scbt.getThingy());

            assertEquals(Integer.valueOf(0), btwithcurrency.getVentures());
            StringCountBag scbwc = btwithcurrency.getBeastTribeCurrency();
            assertEquals(1, scbwc.getCount());
            assertEquals("Steel Amalj'ok", scbwc.getThingy());
            assertEquals(Integer.valueOf(20), btwithcurrency.getBeastTribeReputation());
            StringCountBag scbwct = btwithcurrency.getTomestones();
            assertEquals(10, scbwct.getCount());
            assertEquals("Allagan Tomestone of Poetics", scbwct.getThingy());

            assertEquals("Namazu", namazuQuest.getBeastTribe());
            StringCountBag nz = namazuQuest.getBeastTribeCurrency();
            assertEquals("Namazu Koban", nz.getThingy());
            assertEquals(1, nz.getCount());
            assertEquals(Integer.valueOf(60), namazuQuest.getBeastTribeReputation());
            Map<String, LidCountBag> dlhr = namazuQuest.getClassRewards();
            assertEquals(11, dlhr.size());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test that completion rewards, whether regular, relic, crafting, emote or gender-specific, are correctly parsed
     */
    @Test
    public void testCompletionRewards() throws Exception {
        String hasNonH4Rewards = "c3a18d7b7fc";
        String dotlhLid = "6ffcf50995a";
        String relicLid = "0136a6df60a";
        String emoteLid = "97144ce2654";
        String genderLid = "3726fd8b5ea";
        String numberMustBeTrimmed = "088a43daa15";
        String hasAction = "02577140302";
        Lid severalRewardsLid = new Lid("02f039d7119");
        try {
            Quest hasStupidSpace = Quest.get(new Lid(numberMustBeTrimmed));
        } catch (Exception e) {
            fail("Error occured while trying to parse a reward with a stupid space after its number of rewards at " + numberMustBeTrimmed);
            e.printStackTrace();
        }
        try {
            Quest hasNon = Quest.get(new Lid(hasNonH4Rewards));
        } catch (Exception e) {
            fail("Error occured while trying to test non-h4 rewards at " + hasNonH4Rewards);
            e.printStackTrace();
        }
        try {
            Quest dotlhrewards = Quest.get(new Lid(dotlhLid));
        } catch (Exception e) {
            fail("Error occured while trying to test dotl/doth rewards at " + dotlhLid);
            e.printStackTrace();
        }
        try {
            Quest relicRewards = Quest.get(new Lid(relicLid));
        } catch (Exception e) {
            fail("Error occured while trying to test relic rewards at " + relicLid);
            e.printStackTrace();
        }
        try {
            Quest emoteRewards = Quest.get(new Lid(emoteLid));
        } catch (Exception e) {
            fail("Error occured while trying to test emote rewards at " + relicLid);
            e.printStackTrace();
        }
        try {
            Quest maleFemaleRewards = Quest.get(new Lid(genderLid));
        } catch (Exception e) {
            fail("Error occured while trying to test male/female rewards at " + genderLid);
            e.printStackTrace();
        }
        Quest actionQuest = null;
        try {
            actionQuest = Quest.get(new Lid(hasAction));
        } catch (Exception e) {
            fail("Error occured while trying to test action rewards at " + genderLid);
            e.printStackTrace();
        }
        Quest severalRewards = Quest.get(severalRewardsLid);
        assertEquals(2, severalRewards.getCompletionRewards().size());
        assertEquals(4, severalRewards.getOptionalRewards().size());
        assertEquals(1, severalRewards.getOptionalRewards().get(1).getCount());
        assertEquals(7, severalRewards.getOptionalRewards().get(3).getCount());

        if(actionQuest != null) {
            assertEquals(2, actionQuest.getCompletionRewards().size());
            assertEquals(2, actionQuest.getActionRewards().size());
            assertEquals(0, actionQuest.getOptionalRewards().size());
        }
    }

    @Test
    public void testBLUActionReq() throws Exception {
        Lid lid = new Lid("7b55412faec");
        try {
            Quest blue = Quest.get(lid);
            // Does it parse? the action req used to get smooshed with the class req
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testBLUDutyReq() throws Exception {
        Lid lid = new Lid("e655bd32491");
        try {
            Quest blue = Quest.get(lid);
            // Does it parse? the duty req is a carnivale stage, not a duty
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testNonPrintableChars() throws Exception {
        Lid lid = new Lid("c766556122d");
        try {
            Quest romp = Quest.get(lid);
            assertEquals("A Romp around the Foothills", romp.getTitle());
        } catch (Exception e) {
            fail(e.getMessage());
        }
        lid = new Lid("bf6394ecf52");
        try {
            Quest voice = Quest.get(lid);
            assertEquals("Finding Your Voice", voice.getTitle());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}