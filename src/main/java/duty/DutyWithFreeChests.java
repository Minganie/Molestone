package duty;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import util.Lid;

// Basically, dungeons and raids

/**
 * Subclass of {@link Duty} for duties where one might encounter a free chest (a chest not dropped at a boss encounter)
 * somewhere in the duty's map, i.e. {@link Dungeon} and {@link Raid}
 */
public class DutyWithFreeChests extends DifficultyDuty {

    /**
     * Constructor
     * @param doc Jsoup document for the Lodestone page for this duty
     * @param lid Lodestone id
     * @throws Exception for various parsing issues
     */
    DutyWithFreeChests(Document doc, Lid lid) throws Exception {
        super(doc, lid);
    }

    /**
     * Parsing of Dungeon- and Raid-specific details: boss encounters and free chests
     * @param doc Jsoup document for the Lodestone page of this duty
     * @throws Exception for various parsing issues
     */
    @Override
    void parseDetails(Document doc) throws Exception {
        Elements boxes = doc.select("div.db-view__data__inner");
        int i = 0;
        for(Element box : boxes) {
            boolean isBossBox = box.select("h3").first().text().contains("Boss");
            if(isBossBox) {
                parseEncounter(box, i++);
            } else {
                parseFreeChests(box);
            }
        }
    }

    /**
     * Parse information about free chests in this duty
     * @param chestBox Div for this chest in the Lodestone page
     * @throws Exception for various parsing issues
     */
    private void parseFreeChests(Element chestBox) throws Exception {
        Elements chestPopups = chestBox.select("div.sys_treasure_box");
        for(Element chestPopup : chestPopups) {
            chests.add(new Chest(chestPopup));
        }
    }

    /**
     * Parse information about one boss encounter in this duty
     * @param bossBox Div for this encounter in the Lodestone page
     * @param index Order of occurrence of the encounter in the duty
     * @throws Exception for various parsing issues
     */
    private void parseEncounter(Element bossBox, int index) throws Exception {
        encounters.add(new Encounter(bossBox, index));
    }

    /**
     * Parsing of Dungeon- and Raid-specific name and difficulty
     * @param nameAndDifficulty String like so "Amdapor Keep (Hard)" or "Solemn Trinity"
     */
    @Override
    void parseNameAndDifficulty(String nameAndDifficulty) {
        difficulty = Difficulty.get(nameAndDifficulty);
        name = trimName(nameAndDifficulty);
    }

    /**
     * Printing Dungeon- and Raid-specific full information for this duty to standard output
     */
    @Override
    public void print() {
        System.out.println(name + " (" + difficulty.toString() + ") @ " + lid.toString());
        System.out.println("  Bosses:");
        for(Encounter encounter : encounters) {
            encounter.print();
        }
        System.out.println("  Chests:");
        for(Chest chest : chests) {
            System.out.println("    " + chest.toString());
        }
    }
}
