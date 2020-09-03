package com.sonytest.featureflag.service;

import com.sonytest.featureflag.exceptions.FeatureException;
import com.sonytest.featureflag.model.FeatureCountry;

import java.util.List;

public interface IFeatureService {
    List<FeatureCountry> getFeatureDetails() throws FeatureException;

    FeatureCountry getFeatureDetailsForName(String name) throws FeatureException;

    FeatureCountry editFeature(FeatureCountry featureCountry) throws FeatureException;
}
