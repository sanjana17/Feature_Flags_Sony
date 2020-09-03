package com.sonytest.featureflag.controller;

import com.sonytest.featureflag.exceptions.ErrorResponse;
import com.sonytest.featureflag.exceptions.FeatureException;
import com.sonytest.featureflag.model.FeatureCountry;
import com.sonytest.featureflag.service.IFeatureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;

@RestController
public class FeatureFlagController {

    @Autowired
    private IFeatureService featureService;

    private static final Logger LOGGER = LoggerFactory.getLogger(FeatureFlagController.class);

    @CrossOrigin(origins = "http://localhost:3001")
    @RequestMapping(value = "/getFeatures", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public DeferredResult<?> getFeatureDetails() {
        DeferredResult<ResponseEntity<?>> response = new DeferredResult<>();
        try {
            response.setResult(new ResponseEntity<List<FeatureCountry>>(featureService.getFeatureDetails(), HttpStatus.OK));
        } catch (FeatureException fe) {
            LOGGER.error(fe.getMessageCode(),fe);
            response.setResult(new ResponseEntity<ErrorResponse>(new ErrorResponse(fe.getMessageCode(), fe.getMessage()), fe.getHttpStatus()));
        }
        return response;
    }

    @CrossOrigin(origins = "http://localhost:3001")
    @RequestMapping(value = "/getFeatures/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public DeferredResult<?> getFeatureDetailsForName(@PathVariable(value="name", required = true)String name) {
        DeferredResult<ResponseEntity<?>> response = new DeferredResult<>();
        try {
            response.setResult(new ResponseEntity<FeatureCountry>(featureService.getFeatureDetailsForName(name), HttpStatus.OK));
        } catch (FeatureException fe) {
            LOGGER.error(fe.getMessageCode(),fe);
            response.setResult(new ResponseEntity<ErrorResponse>(new ErrorResponse(fe.getMessageCode(), fe.getMessage()), fe.getHttpStatus()));
        }
        return response;
    }

    @CrossOrigin(origins = "http://localhost:3001")
    @RequestMapping(value = "/editFeature", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public DeferredResult<?> editFeature(@RequestBody(required = true) FeatureCountry featureCountry) {
        DeferredResult<ResponseEntity<?>> response = new DeferredResult<>();
        try {
            response.setResult(new ResponseEntity<FeatureCountry>(featureService.editFeature(featureCountry), HttpStatus.OK));
        } catch (FeatureException cse) {
            LOGGER.error(cse.getMessageCode(), cse);
            response.setResult(new ResponseEntity<>(new ErrorResponse(cse.getMessageCode(), cse.getMessage()), cse.getHttpStatus()));
        }
        return response;
    }
}
