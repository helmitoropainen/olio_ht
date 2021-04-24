package com.example.olio_ht;

public class EntryManager {

    private static EntryManager entryManager = new EntryManager();
    Entry sportEntries;
    Entry foodEntries;
    Entry spleepEntries;

    private EntryManager() {

    }

    public static EntryManager getInstance() {
        return entryManager;
    }

    public void saveEntries() {
        // entryt logiin :) dunno how
    }

    // en tiiä tarviiks näit johonki

    public Entry getSportEntries() { return sportEntries; }
    public Entry getFoodEntries() { return foodEntries; }
    public Entry getSpleepEntries() { return spleepEntries; }

    public void setSportEntries( Entry e ) { sportEntries = e; }
    public void setFoodEntries( Entry e ) { foodEntries = e; }
    public void setSleepEntries( Entry e ) { spleepEntries = e; }

}
