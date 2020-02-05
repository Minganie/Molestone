package test;

import crafting.Recipe;
import org.junit.Test;
import util.Lid;
import util.LidCountBag;

import static junit.framework.TestCase.*;

public class RecipeTest {
    @Test
    public void testGeneralInfo() throws Exception {
        Lid lid = new Lid("fca929efbac");
        try {
            Recipe mortar = Recipe.get(lid);
            assertEquals("fca929efbac", mortar.getLid().toString());
            assertTrue(mortar.getLicon().contains("https://img.finalfantasyxiv.com/lds/pc/global/images/itemicon/86/86aac6bf2a35b07da75e5531a70400d9ab49efaa.png?"));
            assertEquals("Blacksmith", mortar.getDoth());
            assertNull(mortar.getMastery());
            assertEquals(50, mortar.getLvl());
            assertEquals(1, mortar.getnStars());
            assertEquals("Militia Mortar", mortar.getName());
            assertEquals("9e68e645916", mortar.getProduct().toString());
            assertEquals("Alchemist's Secondary Tool", mortar.getCat());
        } catch (Exception e) {
            fail("Failed to correctly parse general info for " + lid + " because " + e.getMessage());
        }
        lid = new Lid("80a72bead39");
        try {
            Recipe pestle = Recipe.get(lid);
            assertEquals("Master Blacksmith I", pestle.getMastery());
        } catch (Exception e) {
            fail("Failed to correctly parse general info for " + lid + " because " + e.getMessage());
        }
        lid = new Lid("44d3af4b4e0");
        try {
            Recipe headgear = Recipe.get(lid);
            assertEquals(0, headgear.getnStars());
        } catch (Exception e) {
            fail("Failed to correctly parse general info for " + lid + " because " + e.getMessage());
        }
    }

    @Test
    public void testIngredients() throws Exception {
        Lid lid = new Lid("44d3af4b4e0");
        try {
            Recipe headgear = Recipe.get(lid);
            assertEquals(3, headgear.getMaterials().size());
            assertEquals(2, headgear.getCrystals().size());
            for(LidCountBag lcb : headgear.getMaterials()) {
                if(lcb.getCount() == 0) {
                    fail("Didn't parse ingredient number correctly!");
                }
                if(lcb.getLid().equals(""))
                    fail("Didn't parse ingredient lid correctly!");
            }
        } catch (Exception e) {
            fail("Failed to correctly parse ingredients for " + lid + " because " + e.getMessage());
        }
    }

    @Test
    public void testCraftData() throws Exception {
        Lid lid = new Lid("c34fa97a3ea");
        try {
            Recipe slops = Recipe.get(lid);
            assertEquals(1, slops.getnCrafted());
            assertEquals(195, slops.getDifficulty());
            assertEquals(80, slops.getDurability());
            assertEquals(2646, slops.getMaxQuality());
            assertEquals(50, slops.getQuality());
        } catch (Exception e) {
            fail("Failed to correctly parse craft data for " + lid + " because " + e.getMessage());
        }
        lid = new Lid("4a539338c8c");
        try {
            Recipe potion = Recipe.get(lid);
            assertEquals(3, potion.getnCrafted());
        } catch (Exception e) {
            fail("Failed to correctly parse craft data for " + lid + " because " + e.getMessage());
        }
    }

    @Test
    public void testCraftConditions() throws Exception {
        Lid lid = new Lid("c34fa97a3ea");
        try {
            Recipe slops = Recipe.get(lid);
            assertEquals(255, slops.getRequiredControl());
            assertEquals(318, slops.getRequiredControlForQuickSynthesis());
            assertEquals(0, slops.getRecommendedCrafstmanship());
            assertEquals(0, slops.getRequiredCraftsmanship());
            assertTrue(slops.isQuickSynthesisEnabled());
            assertTrue(slops.isHighQualityEnabled());
            assertFalse(slops.isCollectable());
        } catch (Exception e) {
            fail("Failed to correctly parse craft data for " + lid + " because " + e.getMessage());
        }
        lid = new Lid("fe73e5cdc28");
        try {
            Recipe legs60 = Recipe.get(lid);
            assertEquals(580, legs60.getRecommendedCrafstmanship());
            assertTrue(legs60.isCollectable());
        } catch (Exception e) {
            fail("Failed to correctly parse craft data for " + lid + " because " + e.getMessage());
        }
        lid = new Lid("a4e6fc31e12");
        try {
            Recipe breakfast = Recipe.get(lid);
            assertEquals(275, breakfast.getRequiredCraftsmanship());
            assertFalse(breakfast.isQuickSynthesisEnabled());
            assertFalse(breakfast.isHighQualityEnabled());
        } catch (Exception e) {
            fail("Failed to correctly parse craft data for " + lid + " because " + e.getMessage());
        }
        lid = new Lid("4da6d36ceba");
        try {
            Recipe delivery = Recipe.get(lid);
            assertEquals("Near Eastern Antique", delivery.getName());
            assertTrue(delivery.isAlwaysCollectible());
        } catch (Exception e) {
            fail("Failed to correctly parse craft data for " + lid + " because " + e.getMessage());
        }
    }
}
