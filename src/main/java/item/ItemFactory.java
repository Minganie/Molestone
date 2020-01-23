package item;

import org.jsoup.nodes.Document;
import util.Lid;

/**
 * Factory that determines which type of item to instantiate depending on cat2 and cat3
 */
public abstract class ItemFactory {
	static Item getItem(String icat2, String icat3, Document doc, Lid lid) throws Exception {
		if(icat2.equals("Arms") || icat2.equals("Tools"))
			return new Weapon(icat2, icat3, doc, lid);
		else if(icat3.equals("Shield"))
			return new Shield(icat2, icat3, doc, lid);
		else if(icat3.equals("Soul Crystal"))
			return new SoulCrystal(icat2, icat3, doc, lid);
		else if(icat2.equals("Armor") || icat2.equals("Accessories"))
			return new Armor(icat2, icat3, doc, lid);
		else if(icat2.equals("Medicines & Meals") || icat3.equals("Seasonal Miscellany"))
			return new Consumable(icat2, icat3, doc, lid);
		else if(icat3.equals("Materia"))
			return new Materia(icat2, icat3, doc, lid);
		else if(icat3.equals("Fishing Tackle"))
			return new Bait(icat2, icat3, doc, lid);
		else if(icat2.equals("Materials") || icat2.equals("Other"))
			return new Material(icat2, icat3, doc, lid);
		else
			throw new Exception("Can't seem to figure out which item to instantiate for '" + icat2 + "' -> '" + icat3 + "'");
	}

    /**
     * HaX hAx HaX ... for that time where you really need a material that doesn't exist (I'm looking at you, venture)
     * @param name Name of the item
     * @param lid Lodestone id of the item
     * @param cat2 cat2 of the item
     * @param cat3 cat3 of the item
     * @param licon Url of the icon on the Lodestone
     * @return instance of {@link Item}
     */
	public static Item getHack(String name, Lid lid, String cat2, String cat3, String licon) {
		return new Material(name, lid, cat2, cat3, licon);
	}
}
