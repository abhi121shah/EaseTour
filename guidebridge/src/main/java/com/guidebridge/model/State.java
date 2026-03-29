package com.Easetour.model;

import java.util.List;

/**
 * State - Data Model for Indian Tourism States
 * Represents all tourism-related information for a single Indian state.
 */
public class State {

    private String id;              // URL slug  e.g. "goa"
    private String name;            // Display name e.g. "Goa"
    private String tagline;         // Short tagline
    private String description;     // Full description paragraph
    private String heroImage;       // Hero image URL
    private String bestTime;        // Best time to visit
    private List<String> famousPlaces;  // List of famous places
    private List<String> famousFood;    // List of famous foods
    private String culture;         // Cultural summary
    private String popularActivities; // Popular activities summary
    private String navTitle;        // Navigation bar title e.g. "GOA Tourism"

    // ─── Constructor ────────────────────────────────────────────────────────────
    public State(String id, String name, String tagline, String description,
                 String heroImage, String bestTime,
                 List<String> famousPlaces, List<String> famousFood,
                 String culture, String popularActivities, String navTitle) {
        this.id = id;
        this.name = name;
        this.tagline = tagline;
        this.description = description;
        this.heroImage = heroImage;
        this.bestTime = bestTime;
        this.famousPlaces = famousPlaces;
        this.famousFood = famousFood;
        this.culture = culture;
        this.popularActivities = popularActivities;
        this.navTitle = navTitle;
    }

    // ─── Getters ────────────────────────────────────────────────────────────────
    public String getId()                   { return id; }
    public String getName()                 { return name; }
    public String getTagline()              { return tagline; }
    public String getDescription()          { return description; }
    public String getHeroImage()            { return heroImage; }
    public String getBestTime()             { return bestTime; }
    public List<String> getFamousPlaces()   { return famousPlaces; }
    public List<String> getFamousFood()     { return famousFood; }
    public String getCulture()              { return culture; }
    public String getPopularActivities()    { return popularActivities; }
    public String getNavTitle()             { return navTitle; }

    // ─── toString (useful for debugging) ────────────────────────────────────────
    @Override
    public String toString() {
        return "State{id='" + id + "', name='" + name + "'}";
    }
}
