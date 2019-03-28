package edu.cmu.inmind.services.muf.components.middleware.commons;

public enum InMindOSGiLevels {
    BASE(2),
    SDK(3),
    THIRD_PARTY_LIBS(4),
    IMPLEMENTATIONS(5),
    CLIENTS(6);

    private int level;

    InMindOSGiLevels(int level) {
        this.level = level;
    }

    public int level() {
        return this.level;
    }
}
