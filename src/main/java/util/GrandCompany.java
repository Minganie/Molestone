package util;

import java.util.Arrays;
import java.util.List;

/**
 * Utility class mostly useful for type checking, used to represent a Grand Company
 */
public class GrandCompany {
    /**
     * Another utility class mostly useful for type checking, contains the grandCompanyRank one can get in a Grand Company
     */
    public class Rank {
        /**
         * Name of the grandCompanyRank
         */
        private String rank;

        /**
         * Constructor that removes GC's particle
         * @param rank Name of the grandCompanyRank
         */
        private Rank(String rank) {
            int beg = rank.indexOf(GrandCompany.this.particle);
            int end = beg+GrandCompany.this.particle.length();
            this.rank = (rank.substring(0, beg) + rank.substring(end, rank.length())).trim();
            this.rank = this.rank.replace("  ", " ");
        }

        /**
         * Getter and toString all rolled into one
         * @return Name of the grandCompanyRank
         */
        public String toString() {
            return rank;
        }
    }

    /**
     * Name of the Grand Company
     */
    private String name;
    /**
     * "Particle" that is added to the grandCompanyRank, for instance "Storm" for the Maelstrom, as in "Second Storm Lieutenant"
     */
    private String particle;

    /**
     * Constructor
     * @param name Name of the Grand Company
     * @param particle Particle that is inserted into the grandCompanyRank name
     */
    private GrandCompany(String name, String particle) {
        this.name = name;
        this.particle = particle;
    }

    /**
     * "Singleton" of grand companies. Would be better to pull them from Lodestone, but it doesn't offer the possibility.
     * Acceptable because Grand Companies haven't changed since 2.0
     */
    private static List<GrandCompany> grandCompanies = Arrays.asList(
            new GrandCompany("Maelstrom", "Storm"),
            new GrandCompany("Order of the Twin Adder", "Serpent"),
            new GrandCompany("Immortal Flames", "Flame")
    );

    /**
     * Method to get a GrandCompany instance from a name
     * @param name Name of the Grand Company
     * @return Instance of GrandCompany
     * @throws Exception if the name is not among the three known Grand Companies
     */
    public static GrandCompany get(String name) throws Exception {
        switch (name) {
            case "Maelstrom":
                return grandCompanies.get(0);
            case "Order of the Twin Adder":
                return grandCompanies.get(1);
            case "Immortal Flames":
                return grandCompanies.get(2);
            default:
                if(name.contains("Storm"))
                    return grandCompanies.get(0);
                if(name.contains("Serpent"))
                    return grandCompanies.get(1);
                if(name.contains("Flame"))
                    return grandCompanies.get(2);
                throw new Exception("Unknown grand company '" + name + "'");
        }
    }

    /**
     * Method to get a GrandCompany.Rank from a name
     * @param name Name of the grandCompanyRank
     * @return Instance of GrandCompany.Rank
     */
    public Rank getRank(String name) {
        return new Rank(name);
    }

    /**
     * Utility toString method
     * @return Name of the Grand Company
     */
    public String toString() {
        return name;
    }
}
