package test;

import org.junit.Test;
import shop.*;
import util.Coords;
import util.Lid;
import util.LidQCountBag;
import util.ZonedCoords;

import java.util.List;

import static junit.framework.TestCase.*;

public class ShopTest {

    @Test
    public void testGeneralInfo() throws Exception {
        Lid shopLid = new Lid("9d03aec955c");
        try {
            Shop calSal = Shop.get(shopLid);
            ZonedCoords c = calSal.getLocations().get(0);
            assertEquals("Calamity Salvager", calSal.getName());
            assertEquals("Limsa Lominsa", c.getZone());
            ZonedCoords expected = new ZonedCoords(new Coords(11.3, 14.3), "Limsa Lominsa");
            assertEquals(expected, c);
        } catch (Exception e) {
            fail("Failed to get general info for " + shopLid + " because " + e.getMessage());
        }
    }

    @Test
    public void testZeroTabs() throws Exception {
        // Aistan has zero tabs with gil
        Lid shopLid = new Lid("195a739ab0f");
        try {
            Shop aistan = Shop.get(shopLid);
            assertEquals(0, aistan.getGilTabs().getTabs().size());
            assertEquals(0, aistan.getGilTabs().getSubTabs().size());
            for(Sale sale : aistan.getSales()) {
                assertNull(sale.getTab());
                assertNull(sale.getSubTab());
            }
        } catch (Exception e) {
            fail("Inappropriate tabs for " + shopLid + " because " + e.getMessage());
        }
    }

    @Test
    public void testOneTabs() throws Exception {

        // Merchant&Mender has gil tabs and stuff in them
        Lid shopLid = new Lid("656ad69b603");
        try {
            Shop mandm = Shop.get(shopLid);
            assertEquals(5, mandm.getGilTabs().getTabs().size());
            assertEquals(5, mandm.getGilTabs().getSubTabs().size());
            for(Sale sale : mandm.getSales()) {
                assertNotNull(sale.getTab());
                assertNull(sale.getSubTab());
            }
        } catch (Exception e) {
            fail("Inappropriate tabs for " + shopLid + " because " + e.getMessage());
        }
    }

    @Test
    public void testTwoTabs() throws Exception {

        // Calamity Salvager has tabs and subtabs with gil
        Lid shopLid = new Lid("9d03aec955c");
        try {
            Shop calSal = Shop.get(shopLid);
            assertTrue(calSal.getGilTabs().getTabs().size() > 0);
            assertTrue(calSal.getGilTabs().getTabs().size() > 0);
            for(Sale sale : calSal.getSales()) {
                if(sale.getType().equals(SaleType.Gil)) {
                    assertNotNull(sale.getTab());
                    assertNotNull(sale.getSubTab());
                }
            }
        } catch (Exception e) {
            fail("Inappropriate tabs for " + shopLid + " because " + e.getMessage());
        }
    }

    @Test
    public void testMultipleLocations() throws Exception {
        Lid shopLid = new Lid("8fe33e49105");
        try {
            Shop matSupplier = Shop.get(shopLid);
            List<ZonedCoords> locations = matSupplier.getLocations();
            assertEquals(2, locations.size());
            assertEquals("Mist", locations.get(0).getZone());
            assertEquals("Mist", locations.get(1).getZone());
            Coords c1 = new Coords(11.0, 11.0);
            assertEquals(c1, locations.get(0).getCoords());
            Coords c2 = new Coords(11.0, 11.3);
            assertEquals(c2, locations.get(1).getCoords());
        } catch (Exception e) {
            fail("Inappropriate area and coords for " + shopLid + " because " + e.getMessage());
        }
    }

    @Test
    public void testOneItemMerch() throws Exception {
        // one item
        Lid shopLid = new Lid("8f3219354c3");
        try {
            Shop mandm = Shop.get(shopLid);
            for (Sale sale : mandm.getSales()) {
                assertTrue(sale.getMerchandise() instanceof ItemMerchandise);
                ItemMerchandise m = (ItemMerchandise) sale.getMerchandise();
                assertEquals(1, m.getItems().size());
            }
        } catch (Exception e) {
            fail("Inappropriate merch for " + shopLid + " because " + e.getMessage());
        }
    }

    @Test
    public void testManyItemMerch() throws Exception {
        // several items
        Lid shopLid = new Lid("5bb0498d3d8");
        try {
            Shop seika = Shop.get(shopLid);
            boolean hasMoreThanOne = false;
            for(Sale sale : seika.getSales()) {
                assertTrue(sale.getMerchandise() instanceof ItemMerchandise);
                ItemMerchandise m = (ItemMerchandise) sale.getMerchandise();
                hasMoreThanOne |= m.getItems().size() > 1;
            }
            assertTrue(hasMoreThanOne);
        } catch (Exception e) {
            fail("Inappropriate merch for " + shopLid + " because " + e.getMessage());
        }
    }

    @Test
    public void testActionMerch() throws Exception {
        Lid shopLid = new Lid("e94dbe55316");
        try {
            Shop oicqm = Shop.get(shopLid);
            boolean hasActions = false;
            for(Sale sale : oicqm.getSales()) {
                hasActions |= sale.getMerchandise() instanceof ActionMerchandise;
                if(hasActions)
                    break;
            }
            assertTrue(hasActions);
        } catch (Exception e) {
            fail("Inappropriate merch for " + shopLid + " because " + e.getMessage());
        }
    }

    @Test
    public void testGilPaymentTypeParsing() throws Exception {
        // gil
        Lid shopLid = new Lid("656ad69b603");
        try {
            Shop mandm = Shop.get(shopLid);
        } catch (Exception e) {
            fail("Inappropriate payment for " + shopLid + " because " + e.getMessage());
        }
    }
    @Test
    public void testFCCPaymentTypeParsing() throws Exception {
        // credits
        Lid shopLid = new Lid("e94dbe55316");
        try {
            Shop oicqm = Shop.get(shopLid);
        } catch (Exception e) {
            fail("Inappropriate payment for " + shopLid + " because " + e.getMessage());
        }
    }
    @Test
    public void testItemPaymentTypeParsing() throws Exception {
        // item
        Lid shopLid = new Lid("5bb0498d3d8");
        try {
            Shop seika = Shop.get(shopLid);
        } catch (Exception e) {
            fail("Inappropriate payment for " + shopLid + " because " + e.getMessage());
        }
    }
    @Test
    public void testTokItmPaymentTypeParsing() throws Exception {
        // token
        // token and item
        Lid shopLid = new Lid("4b4aa4d9dce");
        try {
            Shop scrips = Shop.get(shopLid);
        } catch (Exception e) {
            fail("Inappropriate payment for " + shopLid + " because " + e.getMessage());
        }
    }
    @Test
    public void testSealsPaymentTypeParsing() throws Exception {
        // seals and rank
        Lid shopLid = new Lid("5bb28b3316b");
        try {
            Shop stormqm = Shop.get(shopLid);
        } catch (Exception e) {
            fail("Inappropriate payment for " + shopLid + " because " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    public void testNumberedVentures() throws Exception {
        Lid shopLid = new Lid("9b8055ffab0");
        try {
            Shop kobold = Shop.get(shopLid);
        } catch (Exception e) {
            fail("Inappropriate venture parsing for " + shopLid + " because " + e.getMessage());
        }
    }

    @Test
    public void testHqSale() throws Exception {
        Lid shopLid = new Lid("854108cf400");
        try {
            Shop khloe = Shop.get(shopLid);
            boolean hasHQ = false;
            for(Sale sale : khloe.getSales()) {
                if(sale.getMerchandise() instanceof ItemMerchandise) {
                    ItemMerchandise im = (ItemMerchandise) sale.getMerchandise();
                    for(LidQCountBag i : im.getItems()) {
                        hasHQ |= i.isHq();
                        if(hasHQ)
                            break;
                    }
                }
            }
            assertTrue(hasHQ);
        } catch (Exception e) {
            fail("Inappropriate venture parsing for " + shopLid + " because " + e.getMessage());
        }
    }

    @Test
    public void testVeryLongInventory() throws Exception {
        Lid shopLid = new Lid("0f7c2a51de7");
        try {
            Shop jtandhaa = Shop.get(shopLid);
        } catch (Exception e) {
            fail("Inappropriate parsing for " + shopLid + " because " + e.getMessage());
        }
    }
}
