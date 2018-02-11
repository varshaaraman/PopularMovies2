package com.example.codelabs.moviestage;

/**
 * Created by varshaa on 21-01-2018.
 */

public class Trailer {
    String trailerId;
    String key;
    String name;

    public Trailer(String trailerId, String key, String name) {
        this.trailerId = trailerId;
        this.key = key;
        this.name = name;
    }

    public String getTrailerId() {
        return trailerId;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }
}
