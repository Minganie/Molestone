package test;

import achievement.Achievement;
import org.junit.Test;
import util.Lid;

import static junit.framework.TestCase.*;

public class AchievementTest {
    @Test
    public void testAchievement() throws Exception {
        Lid lid = new Lid("3a7057bd04e");
        try {
            Achievement a = Achievement.get(lid);
            assertEquals("Exploration", a.getCategory2());
            assertEquals("The Black Shroud", a.getCategory3());
            assertEquals("Mapping the Realm: East Shroud", a.getTitle());
            assertEquals(10, a.getPoints());
            assertEquals("Visit the East Shroud and unlock the area map.", a.getDescription());
            assertTrue(a.getIcon().toString().contains("https://img.finalfantasyxiv.com/lds/pc/global/images/itemicon/8e/8e92f1cfb0b8af7a417c6012b33e11b72adfd10e.png"));
            assertEquals(0, a.getTitleReward().size());
            assertNull(a.getItemReward());
        } catch (Exception e) {
            fail("Unable to properly parse Mapping East Shroud: " + e.getMessage());
        }
    }

    @Test
    public void testEqualTitleReward() throws Exception {
        Lid lid = new Lid("de887cf928b");
        try {
            Achievement a = Achievement.get(lid);
            assertEquals("Crafting & Gathering", a.getCategory2());
            assertEquals("Carpenter", a.getCategory3());
            assertEquals("Going with the Grain: Artisan", a.getTitle());
            assertEquals(10, a.getPoints());
            assertEquals("Successfully synthesize 3,000 times for level 41-50 woodworking recipes.", a.getDescription());
            assertTrue(a.getIcon().toString().contains("https://img.finalfantasyxiv.com/lds/pc/global/images/itemicon/48/485e81f009337aad790edf50aa5c84e6a37a2921.png"));
            assertEquals("Of the Golden Saw", a.getTitleReward().get("Male"));
            assertEquals("Of the Golden Saw", a.getTitleReward().get("Female"));
            assertNull(a.getItemReward());
        } catch (Exception e) {
            fail("Unable to properly parse Going with the Grain: " + e.getMessage());
        }
    }

    @Test
    public void testGenderedTitleReward() throws Exception {
        Lid lid = new Lid("0274648531b");
        try {
            Achievement a = Achievement.get(lid);
            assertEquals("Character", a.getCategory2());
            assertEquals("General", a.getCategory3());
            assertEquals("Mastering Magic III", a.getTitle());
            assertEquals(20, a.getPoints());
            assertEquals("Achieve level 70 as a conjurer, thaumaturge, arcanist, astrologian, and red mage.", a.getDescription());
            assertTrue(a.getIcon().toString().contains("https://img.finalfantasyxiv.com/lds/pc/global/images/itemicon/1c/1cb034aba5e59cfa1c989039dba5cbd31b0d87b4.png"));
            assertEquals("God of Magic", a.getTitleReward().get("Male"));
            assertEquals("Goddess of Magic", a.getTitleReward().get("Female"));
            assertNull(a.getItemReward());
        } catch (Exception e) {
            fail("Unable to properly parse Mastering Magic III: " + e.getMessage());
        }
    }

    @Test
    public void testItemReward() throws Exception {
        Lid lid = new Lid("9a2cc19313e");
        try {
            Achievement a = Achievement.get(lid);
            assertEquals("Exploration", a.getCategory2());
            assertEquals("Sightseeing Log", a.getCategory3());
            assertEquals("Out of Sight Out of Mind", a.getTitle());
            assertEquals(10, a.getPoints());
            assertEquals("Complete entries 1-80 in your sightseeing log.", a.getDescription());
            assertTrue(a.getIcon().toString().contains("https://img.finalfantasyxiv.com/lds/pc/global/images/itemicon/1d/1d2be754de517a7f299a4b3f80b1b378c5d54738.png"));
            assertEquals(0, a.getTitleReward().size());
            assertEquals("48bcda3953e", a.getItemReward().get());
        } catch (Exception e) {
            fail("Unable to properly parse Out of Sight Out of Mind: " + e.getMessage());
        }
    }

    @Test
    public void testAchievsNotItems() throws Exception {
        Lid lid = new Lid("fcd368656b8");
        try {
            Achievement blue = Achievement.get(lid);
            assertNull(blue.getItemReward());
        } catch (Exception e) {
            fail("Unable to properly parse Blue Unchained");
        }
    }
}
