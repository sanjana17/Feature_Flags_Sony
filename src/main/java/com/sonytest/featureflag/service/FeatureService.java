package com.sonytest.featureflag.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sonytest.featureflag.exceptions.FeatureException;
import com.sonytest.featureflag.model.Country;
import com.sonytest.featureflag.model.FeatureCountry;
import com.sonytest.featureflag.model.FeatureFlag;
import com.sonytest.featureflag.utils.Utils;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FeatureService implements IFeatureService {
    @Autowired
    private Utils utils;

    @Override
    public List<FeatureCountry> getFeatureDetails() throws FeatureException {
        List<FeatureFlag> featureFlags = getFeatureFlags();

        return getFeatureCountries(featureFlags);
    }

    private List<FeatureCountry> getFeatureCountries(List<FeatureFlag> featureFlags) {
        List<FeatureCountry> featureCountries = new ArrayList<>();
        for(FeatureFlag featureFlag: featureFlags){
            FeatureCountry featureCountry = new FeatureCountry();
            featureCountry.setFlagName(featureFlag.getName());
            featureCountry.setCountries(getCountries(Integer.toBinaryString(featureFlag.getValue()), 5));
            featureCountries.add(featureCountry);
        }
        return featureCountries;
    }

    @Override
    public FeatureCountry getFeatureDetailsForName(String name) throws FeatureException {
        for(FeatureCountry featureCountry: getFeatureDetails()){
            if(name.equalsIgnoreCase(featureCountry.getFlagName())){
                return featureCountry;
            }
        }
        return null;
    }

    @Override
    public FeatureCountry editFeature(FeatureCountry featureCountry) throws FeatureException {
        Map<String,Object> body = new HashMap<>();
        body.put("value", getIntegerValue(featureCountry.getCountries()));
        body.put("name", featureCountry.getFlagName());
        ObjectMapper mapper = new ObjectMapper();
        try {
            for(FeatureCountry featureCountry1 : getFeatureCountries(postFeatureFlags(mapper.writeValueAsString(body)))){
                if(featureCountry.getFlagName().equalsIgnoreCase(featureCountry1.getFlagName())){
                    return featureCountry1;
                }
            }
        } catch (JsonProcessingException e) {
            throw new FeatureException(e.getMessage(), "io.exception", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return null;
    }

    private int getIntegerValue(List<Country> countries) {
        StringBuilder value = new StringBuilder("");
        for(Country country: countries){
            value.append(country.getValue().toString());
        }

        return Integer.parseInt(value.toString(), 2);
    }

    private List<Country> getCountries(String binaryString, int length) {
        List<Country> countries = new ArrayList<>();
        StringBuilder binaryStringBuilder = new StringBuilder(binaryString);
        StringBuilder zeroString = new StringBuilder("0");
        int existingBinaryLength = binaryStringBuilder.length();
        while(zeroString.length() < (length-existingBinaryLength)){
            zeroString.append("0");
        }
        String[] countriesBits = existingBinaryLength == length ? binaryStringBuilder.toString().split(""):zeroString.append(binaryStringBuilder).toString().split("");
        for(int i=0;i<countriesBits.length;i++){
            Country c = new Country();
            c.setName(getCountryName(i));
            c.setValue(Integer.parseInt(countriesBits[i]));
            countries.add(c);
        }
        return countries;
    }

    private String getCountryName(int i) {
        switch (i){
            case 0:
                return "Asia";
            case 1:
                return "Korea";
            case 2:
                return "Europe";
            case 3:
                return "Japan";
            case 4:
                return "America";
            default:
                return "";
        }
    }

    private List<FeatureFlag> getFeatureFlags() throws FeatureException {
        List<FeatureFlag> featureFlags= new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            JSONArray jsonArray = new JSONArray(utils.readFromURL("http://10.200.193.242:12300/featureflags"));
            for (int index = 0; index < jsonArray.length(); index++) {
                featureFlags.add(mapper.readValue(jsonArray.getString(index), FeatureFlag.class));
            }
        } catch (JSONException | IOException e) {
            throw new FeatureException(e.getMessage(), "io.exception", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return featureFlags;
    }

    private List<FeatureFlag> postFeatureFlags(String payload) throws FeatureException {
        List<FeatureFlag> featureFlags= new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            JSONArray jsonArray = new JSONArray(utils.postToURL("http://10.200.193.242:12300/featureflags", payload));
            for (int index = 0; index < jsonArray.length(); index++) {
                featureFlags.add(mapper.readValue(jsonArray.getString(index), FeatureFlag.class));
            }
        } catch (JSONException | IOException e) {
            throw new FeatureException(e.getMessage(), "io.exception", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return featureFlags;
    }
}
