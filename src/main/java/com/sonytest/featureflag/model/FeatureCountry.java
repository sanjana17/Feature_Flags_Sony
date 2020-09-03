package com.sonytest.featureflag.model;

import java.util.List;

public class FeatureCountry {
    private String flagName;
    private List<Country> countries;

    public String getFlagName() {
        return flagName;
    }

    public void setFlagName(String flagName) {
        this.flagName = flagName;
    }

    public List<Country> getCountries() {
        return countries;
    }

    public void setCountries(List<Country> countries) {
        this.countries = countries;
    }
}
