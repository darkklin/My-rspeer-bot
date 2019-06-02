package miningGuild.util.rocks;

public enum Rock {

    CLAY(new short[]{6705}, 0),
    COPPER(new short[]{4645, 4510}, 0),
    TIN(new short[]{53}, 0),
    IRON(new short[]{2576}, 35),
    SILVER(new short[]{74}, 0),
    COAL(new short[]{10508}, 50),
    GOLD(new short[]{8885}, 0),
    MITHRIL(new short[]{-22239}, 80),
    ADAMANTITE(new short[]{21662}, 0),
    RUNITE(new short[]{-31437}, 0);

    private short[] color;
    private int experience;

    Rock(short[] color, int experience) {
        this.color = color;
        this.experience = experience;
    }

    public short[] getColor() {
        return color;
    }

    public int getExperience() {
        return experience;
    }

}
