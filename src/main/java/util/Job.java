package util;

/**
 * Utility class used to represent a Class and/or Job; tests existence of a class/job against a static list of strings...
 * which is not ideal when Square adds a new one. Should modify this to delegate class/job check to db
 */
public class Job {
    private String name;
    private String abbrev;
    private Job() {
    }

    /**
     * Factory method to get a Class/Job from a name
     * @param mumbo Name of the class/job
     * @return Instance of {@link Job}
     */
    public static Job getFromName(String mumbo) {
        Job j = new Job();
        j.name = mumbo;
        return j;
    }

    /**
     * Factory method to get a Class/Job from an abbreviation
     * @param mumbo Abbreviation of the class/job
     * @return Instance of {@link Job}
     */
    public static Job getFromAbbrev(String mumbo) {
        Job j = new Job();
        j.abbrev = mumbo;
        return j;
    }

    // Commented out because it was delegated to DB (and also, because DB now has job lists)
    /**
     * Factory-type method to get a list of class/job abbreviations from a string like "Disciple of the Land";
     * used to generate a list of classes/jobs required to equip an item that will not break foreign key
     * constraints in Melody's ffxiv db
     * @param discvrac String like "ARC BRD MCH" or "Disciple of the Hand"
     * @return List of class/job abbreviations
     */
//    public static String[] getJobsArray(String discvrac) {
//        String[] disc;
//        if(discvrac.equalsIgnoreCase("All classes and jobs (excluding limited jobs)"))
//            disc = new String[]{"CRP", "CUL", "BSM", "GSM", "WVR", "ARM", "ALC", "LTW", "FSH", "BTN", "MIN", "ROG", "SAM", "DRG", "MRD", "ARC", "PLD", "MCH", "WAR", "PGL", "MNK", "GLA", "DRK", "BRD", "LNC", "NIN", "THM", "CNJ", "AST", "SMN", "WHM", "ACN", "SCH", "BLM", "RDM"};
//        else if (discvrac.equalsIgnoreCase("Any Disciple of War or Magic (excluding limited jobs)"))
//            disc = new String[]{"ROG", "SAM", "DRG", "MRD", "ARC", "PLD", "MCH", "WAR", "PGL", "MNK", "GLA", "DRK", "BRD", "LNC", "NIN", "THM", "CNJ", "AST", "SMN", "WHM", "ACN", "SCH", "BLM", "RDM"};
//        else if(discvrac.equalsIgnoreCase("Disciple of the Hand"))
//            disc = new String[]{"CRP", "CUL", "BSM", "GSM", "WVR", "ARM", "ALC", "LTW"};
//        else if(discvrac.equalsIgnoreCase("Disciple of the Land"))
//            disc = new String[]{"FSH", "BTN", "MIN"};
//        else if(discvrac.equalsIgnoreCase("Disciple of War"))
//            disc = new String[]{"ROG", "SAM", "DRG", "MRD", "ARC", "PLD", "MCH", "WAR", "PGL", "MNK", "GLA", "DRK", "BRD", "LNC", "NIN"};
//        else if(discvrac.equalsIgnoreCase("Disciple of Magic"))
//            disc = new String[]{"THM", "CNJ", "AST", "SMN", "WHM", "ACN", "SCH", "BLM", "RDM", "BLU"};
//        else if(discvrac.equalsIgnoreCase("Disciples of War or Magic"))
//            disc = new String[]{"ROG", "SAM", "DRG", "MRD", "ARC", "PLD", "MCH", "WAR", "PGL", "MNK", "GLA", "DRK", "BRD", "LNC", "NIN", "THM", "CNJ", "AST", "SMN", "WHM", "ACN", "SCH", "BLM", "RDM", "BLU"};
//        else if(discvrac.equalsIgnoreCase("All Classes"))
//            disc = new String[]{"CRP", "CUL", "BSM", "GSM", "WVR", "ARM", "ALC", "LTW", "FSH", "BTN", "MIN", "ROG", "SAM", "DRG", "MRD", "ARC", "PLD", "MCH", "WAR", "PGL", "MNK", "GLA", "DRK", "BRD", "LNC", "NIN", "THM", "CNJ", "AST", "SMN", "WHM", "ACN", "SCH", "BLM", "RDM", "BLU"};
//        else
//            disc = discvrac.split(" ");
//        return disc;
//    }

    // Commented out because DB should now do that stuff; also, was not used
    /**
     * Small method used to correct caps coherence between various sources...
     * @param job Name of the class/job
     * @return Name of the class/job with in-game capitalization
     */
//    public static String correctJob(String job) {
//        if(job.equalsIgnoreCase("Disciples of Magic"))
//            return "Disciple of War or Magic";
//        if(job.equals("Disciple Of War Or Magic"))
//            return "Disciple of War or Magic";
//        return job;
//    }

    /**
     * Utility toString method
     * @return String like so "Carpenter"
     */
    public String toString() {
        return name;
    }

    /**
     * Getter for the name of the class/job
     * @return Name
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for the abbreviation of the class/job
     * @return Abbreviation
     */
    public String getAbbrev() {
        return abbrev;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Job) {
            Job j = (Job) obj;
            if(this.name == null && this.abbrev == null)
                return j.name == null && j.abbrev == null;
            else if(this.name == null)
                return j.name == null && this.abbrev.equals(j.abbrev);
            else if(this.abbrev == null)
                return j.abbrev == null && this.name.equals(j.name);
            else
                return this.name.equals(j.name) && this.abbrev.equals(j.abbrev);
        } else
            return false;
    }

    @Override
    public int hashCode() {
        return (name != null ? name.hashCode() : super.hashCode()) + (abbrev != null ? abbrev.hashCode() : super.hashCode());
    }
}
