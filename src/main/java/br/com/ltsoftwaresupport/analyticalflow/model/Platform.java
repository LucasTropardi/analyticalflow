package br.com.ltsoftwaresupport.analyticalflow.model;

public enum Platform {
    PLAYSTATION("Playstation"),
    XBOX("Xbox"),
    WII("Wii"),
    SWITCH("Switch");

    private String platform;

    Platform() {
    }

    Platform(String platform) {
        this.platform = platform;
    }

    @Override
    public String toString() {
        return platform;
    }


}