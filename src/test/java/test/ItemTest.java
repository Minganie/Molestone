package test;

import item.Item;
import org.junit.Test;
import util.Lid;

import static junit.framework.TestCase.*;

public class ItemTest {
    @Test
    public void testArmInfo() throws Exception {
        Lid lid = new Lid("6530f915d4d");
        try {
            Item omegaSword = Item.get(lid);
            assertTrue(omegaSword.isUnique());
            assertTrue(omegaSword.isUntradable());
            assertEquals("Rare", omegaSword.getRarity());
            assertEquals("Omega Sword", omegaSword.getName());
            assertEquals("Arms", omegaSword.getCat2());
            assertEquals("Gladiator's Arm", omegaSword.getCat3());
            assertFalse(omegaSword.isCrestWorthy());
            assertTrue(omegaSword.isDresserAble());
            assertFalse(omegaSword.isArmoireAble());
            assertTrue(omegaSword.getLicon().contains("https://img.finalfantasyxiv.com/lds/pc/global/images/itemicon/9a/9a2bf4c4fb6cb48b4ad194f4b86e34ecba304eda.png"));
            assertEquals(Integer.valueOf(405), omegaSword.getIlvl());
            assertEquals(Integer.valueOf(109), omegaSword.getDamage());
            assertEquals(81.38F, omegaSword.getAutoAttack());
            assertEquals(2.24F, omegaSword.getDelay());
            assertEquals("GLA PLD", omegaSword.getDisciplines());
            assertEquals(Integer.valueOf(70), omegaSword.getRequiredLevel());
            assertEquals(4, omegaSword.getBonuses().size());
            assertEquals(Integer.valueOf(2), omegaSword.getMateriaSlots());
            assertEquals("Blacksmith", omegaSword.getRepairClass());
            assertEquals(Integer.valueOf(60), omegaSword.getRepairLevel());
            assertEquals("Grade 7 Dark Matter", omegaSword.getRepairMaterial());
            assertEquals("Blacksmith", omegaSword.getMeldingClass());
            assertEquals(Integer.valueOf(70), omegaSword.getMeldingLevel());
            assertTrue(omegaSword.isConvertible());
            assertTrue(omegaSword.isProjectable());
            assertEquals("Blacksmith", omegaSword.getDesynthClass());
            assertEquals(405F, omegaSword.getDesynthLevel());
            assertTrue(omegaSword.isDyeable());
            assertTrue(omegaSword.isAdvMelding());
            assertFalse(omegaSword.isUnsellable());
            assertTrue(omegaSword.isMarketProhibited());
        } catch (Exception e) {
            fail("Failed to get general info for " + lid + " because " + e.getMessage());
        }
    }

    @Test
    public void testArmorInfo() throws Exception {
        Lid lid = new Lid("0accd1359d8");
        try {
            Item uniform = Item.get(lid);
            assertEquals(Integer.valueOf(71), uniform.getDefense());
            assertEquals(Integer.valueOf(53), uniform.getMagicDefense());
            assertEquals(Integer.valueOf(206), uniform.getSellPrice());
            assertEquals("Armorer", uniform.getDesynthClass());
            assertEquals(41.00F, uniform.getDesynthLevel());
        } catch (Exception e) {
            fail("Failed to get general info for " + lid + " because " + e.getMessage());
        }
    }

    @Test
    public void testSoulCrystalInfo() throws Exception {
        Lid lid = new Lid("a03321484cc");
        try {
            Item sc = Item.get(lid);
            assertEquals("Accessories", sc.getCat2());
            assertEquals("Soul Crystal", sc.getCat3());
            assertEquals("Upon the surface of this multi-aspected crystal are carved the myriad deeds of monks from eras past.", sc.getNote());
        } catch (Exception e) {
            fail("Failed to get general info for " + lid + " because " + e.getMessage());
        }
    }

    @Test
    public void testPotionInfo() throws Exception {
        Lid lid = new Lid("1e8c49c8ac3");
        try {
            Item potion = Item.get(lid);
            assertEquals(1, potion.getEffects().size());
            assertEquals(Integer.valueOf(36), potion.getRecast());
        } catch (Exception e) {
            fail("Failed to get general info for " + lid + " because " + e.getMessage());
        }
    }

    @Test
    public void testShieldInfo() throws Exception {
        Lid lid = new Lid("805b51af8a4");
        try {
            Item shield = Item.get(lid);
            assertEquals(Integer.valueOf(24), shield.getBlockStrength());
            assertEquals(Integer.valueOf(124), shield.getBlockRate());
        } catch (Exception e) {
            fail("Failed to get general info for " + lid + " because " + e.getMessage());
        }
    }

    @Test
    public void testMateriaInfo() throws Exception {
        Lid lid = new Lid("2018335c922");
        try {
            Item mat = Item.get(lid);
            assertEquals(Integer.valueOf(70), mat.getMeldItemLevel());
        } catch (Exception e) {
            fail("Failed to get general info for " + lid + " because " + e.getMessage());
        }
    }

    @Test
    public void testDisciplines() throws Exception {
        // "Disciple of the Hand"
        Lid lid = new Lid("d7b20265794");
        try {
            Item turban = Item.get(lid);
            assertEquals("Disciple of the Hand", turban.getDisciplines());
        } catch (Exception e) {
            fail("Failed to get general info for " + lid + " because " + e.getMessage());
        }

        // "CRP"
        lid = new Lid("5049c5cefc7");
        try {
            Item clawHammer = Item.get(lid);
            assertEquals("CRP", clawHammer.getDisciplines());
        } catch (Exception e) {
            fail("Failed to get general info for " + lid + " because " + e.getMessage());
        }
    }

    @Test
    public void testUnprintableCharInName() throws Exception {
        Lid lid = new Lid("ffe527ffb78");
        try {
            Item ramiePoncho = Item.get(lid);
            assertEquals("Ramie Poncho", ramiePoncho.getName());
        } catch (Exception e) {
            fail("Failed to get proper name for " + lid + " because " + e.getMessage());
        }
    }

    @Test
    public void testShopSources() throws Exception {
        Lid lid = new Lid("ba7b835e608");
        try {
            // regular vendors
            Item bronzeIngot = Item.get(lid);
            assertEquals(11, bronzeIngot.getShopSources().size());
            // wolf vendor
            lid = new Lid("dd88a93ac50");
            Item pvpWeapon = Item.get(lid);
            assertEquals(1, pvpWeapon.getShopSources().size());
            assertTrue(pvpWeapon.getShopSources().contains(new Lid("7dcfaaaf669")));
            // scrip vendor
            lid = new Lid("32b7f5e00dc");
            Item scripMainHand = Item.get(lid);
            assertEquals(9, scripMainHand.getShopSources().size());
            // GC QM
            lid = new Lid("8b84874781f");
            Item sword = Item.get(lid);
            assertEquals(1, sword.getShopSources().size());
            assertTrue(sword.getShopSources().contains(new Lid("5bb28b3316b")));
            // poetics
            lid = new Lid("c4e26dc27d0");
            Item magitekSword = Item.get(lid);
            assertEquals(4, magitekSword.getShopSources().size());
            // regular + amaljaa, amaljaa is there twice...
            lid = new Lid("a31d0a708d9");
            Item ironIngot = Item.get(lid);
            assertEquals(5, ironIngot.getShopSources().size());
        } catch (Exception e) {
            fail("Failed to get regular shops for " + lid + " because " + e.getMessage());
        }
    }

    @Test
    public void testDutySources() throws Exception {
        Lid lid = new Lid("94b07877d6d");
        try {
            Item item = Item.get(lid);
            assertEquals(2, item.getDutySources().size());
        } catch (Exception e) {
            fail("Failed to get duties for " + lid + " because " + e.getMessage());
        }
    }

    @Test
    public void testQuestSources() throws Exception {
        Lid lid = new Lid("82a24de366d");
        try {
            Item item = Item.get(lid);
            assertEquals(3, item.getQuestSources().size());
        } catch (Exception e) {
            fail("Failed to get quests for " + lid + " because " + e.getMessage());
        }
    }

    @Test
    public void testGatheringSources() throws Exception {
        Lid lid = new Lid("82a24de366d");
        try {
            Item item = Item.get(lid);
            assertEquals(1, item.getGatheringSources().size());
            assertTrue(item.getGatheringSources().contains(new Lid("15ce02cc550")));
        } catch (Exception e) {
            fail("Failed to get gathering sources for " + lid + " because " + e.getMessage());
        }
    }

    @Test
    public void testCrafting() throws Exception {
        Lid lid = new Lid("ba7b835e608");
        try {
            Item item = Item.get(lid);
            // source
            assertEquals(2, item.getCraftingSources().size());
            // uses
            assertEquals(81, item.getCraftingUses().size());
        } catch (Exception e) {
            fail("Failed to get crafting sources or uses for " + lid + " because " + e.getMessage());
        }
    }
}
