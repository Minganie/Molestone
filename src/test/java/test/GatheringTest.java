package test;

import gathering.GatheringLogEntry;
import gathering.GatheringLocation;
import org.junit.Test;
import util.Lid;

import static junit.framework.TestCase.*;

public class GatheringTest {

    @Test
    public void notHiddenButTimeLimitedTest() throws Exception {
        Lid leekLid = new Lid("7e0a3a404f7");
        try {
            GatheringLogEntry leek = GatheringLogEntry.get(leekLid);
            assertEquals(leekLid, leek.getLid());
            assertEquals("Harvesting", leek.getCat2());
            assertEquals("Ingredient", leek.getCat3());
            assertEquals(new Lid("639971ee61f"), leek.getItem());
            assertFalse(leek.isHidden());
            assertEquals(50, leek.getLevel());
            assertEquals(2, leek.getnStars());
            GatheringLocation quarterstone = new GatheringLocation("La Noscea", "Western La Noscea", "Quarterstone", 50, true);
            assertEquals(quarterstone, leek.getLocations().get(0));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void hiddenButNotTimeLimitedTest() throws Exception {
        Lid linseedLid = new Lid("cb721c18f59");
        try {
            GatheringLogEntry linseed = GatheringLogEntry.get(linseedLid);
            assertTrue(linseed.isHidden());
            GatheringLocation lowerPaths = new GatheringLocation("The Black Shroud", "South Shroud", "Lower Paths", 35, false);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void severalLocationsTest() throws Exception {
        Lid CuLid = new Lid("15ce02cc550");
        try {
            GatheringLogEntry Cu = GatheringLogEntry.get(CuLid);
            assertEquals(2, Cu.getLocations().size());
            GatheringLocation spineless = new GatheringLocation("Thanalan", "Central Thanalan", "Spineless Basin", 5, false);
            GatheringLocation hammerlea = new GatheringLocation("Thanalan", "Western Thanalan", "Hammerlea", 5, false);
            boolean foundSpineless = false;
            boolean foundHammerlea = false;
            for(GatheringLocation gl : Cu.getLocations()) {
                if(gl.equals(spineless))
                    foundSpineless = true;
                if(gl.equals(hammerlea))
                    foundHammerlea = true;
            }
            assertTrue(foundHammerlea && foundSpineless);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void timeRestrictionsTest() {
        // wouldn't I love if that was on the Lodestone...
    }
}
