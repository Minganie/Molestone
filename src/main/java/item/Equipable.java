package item;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import util.Job;
import util.Lid;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class used to represent an item that can be equipped (ex. chest piece, necklace, etc.)
 */
public class Equipable extends Item {

    Equipable(String icat2, String icat3, Document doc, Lid lid) throws Exception {
    	super(icat2, icat3, doc, lid);
    }

    @Override
	void parseSpecificDetails(Elements details) throws Exception {
		// CLASSES
		disciplines = details.select(".db-view__item_equipment__class").html().trim();
		
		// BONUSES
		Elements bss = details.select(".sys_nq_element ul.db-view__basic_bonus li");
		for(Element b : bss) {
			Bonus bonus;
			try {
				bonus = parseBonus(b.html());
//				System.out.println("Found a bonus of " + bonus);
				bonuses.add(bonus);
			} catch (PlannedException e) {
				throw new Exception("Failed to get bonuses for " + name + " because " + e.getMessage());
			}
		}
//		System.out.println(String.format("Item %40s has bonuses: %s", name, bonuses.toString()));
		
		// MATERIA
		Elements sockets = details.select("ul.db-view__materia_socket li");
		materiaSlots = sockets.size();
//		System.out.println("Item " + name + " has " + materiaSlots + " materia slots");
		
		// REPAIR
		Elements repairInfo = details.select("ul.db-view__item_repair li>span:nth-child(2)");
		DiscLevel who = null;
		try {
			who = parseClass(repairInfo.get(0).html());
			repairClass = who.getDiscipline();
			repairLevel = who.getLevel();
		} catch (PlannedException e) {
			throw new Exception("Failed to get who repair for " + name + " because " + e.getMessage());
		}
		repairMaterial = repairInfo.get(1).html();
		DiscLevel meld = null;
		try {
			if(repairInfo.size() >=3) {
				meld = parseClass(repairInfo.get(2).html());
				meldingClass = meld.getDiscipline();
				meldingLevel = meld.getLevel();
			}
		} catch (PlannedException e) {
			throw new Exception("Failed to get who meld for " + name + " because " + e.getMessage());
		}
//		System.out.println(String.format("Item %s is repaired by %s for %s and melded by %s",
//				name,
//				(who == null ? "?" : who.toString()),
//				repairMaterial,
//				(meld == null ? "?" : meld.toString())));

		// BOOLEANS
        Elements info = details.select(".db-view__item-info__list li");
		if(!info.isEmpty()) {
		    for(Element li : info) {
		        String s = li.html();
		        if(s.contains("Convertible"))
                    convertible = s.contains("Yes");
		        if(s.contains("Desynthesizable"))
		            desynthLevel = (s.contains("No") ? null : Float.parseFloat(li.select("span").html()));
		        if(s.contains("Dyeable"))
		            dyeable = s.contains("Yes");
		        if(s.contains("Projectable"))
		            projectable = s.contains("Yes");
            }
            if(convertible == null || dyeable == null || projectable == null)
                throw new Exception("Ran into some trouble getting the booleans for item " + name);
		    if(desynthLevel != null && desynthClass == null && repairClass != null)
		        desynthClass = repairClass;
        }
	}
	
	static Bonus parseBonus(String mumbo) throws PlannedException {
		String stat = null;
		Integer size = null;
		Pattern pattern = Pattern.compile("^<span>(.+)</span>.\\+(\\d+)$");
		Matcher matcher = pattern.matcher(mumbo);
		if (matcher.find())
		{
			stat = matcher.group(1);
			size = Integer.parseInt(matcher.group(2));
		}
		if(stat == null || size == null)
			throw new PlannedException("Can't seem to find the class and lvl in provided mumbojumo: '" + mumbo + "'");
		return new Bonus(stat, size);
	}
	
	static DiscLevel parseClass(String mumbo) throws PlannedException {
		String disc = null;
		Integer lvl = null;
		Pattern pattern = Pattern.compile("^(.+).Lv\\..(\\d+)$");
		Matcher matcher = pattern.matcher(mumbo);
		if (matcher.find())
		{
			disc = matcher.group(1);
			lvl = Integer.parseInt(matcher.group(2));
		}
		if(disc == null || lvl == null)
			throw new PlannedException("Can't seem to find the class and lvl in provided mumbojumo: " + mumbo);
		return new DiscLevel(disc, lvl);
	}
}
