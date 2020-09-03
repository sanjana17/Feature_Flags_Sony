package com.sonytest.featureflag.utils;

import com.sonytest.featureflag.controller.FeatureFlagController;
import com.sonytest.featureflag.exceptions.FeatureException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Component
public class Utils {

    private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);

    public String readFromURL(String urlPath) throws FeatureException {
        String response = null;
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(new HttpGet(urlPath));
            response = getResponseFromHttp(httpResponse, urlPath);
        } catch (IOException exp) {
            LOGGER.error(urlPath + exp.getMessage(), exp);
            throw new FeatureException(exp.getMessage(), "internal.server.error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    public String postToURL(String urlPath, String properties) throws FeatureException {
        String response = null;
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse httpResponse = null;
        try {
            HttpPost httppost = new HttpPost(urlPath);
            httppost.setHeader("Content-type", "application/json");
            httppost.setEntity(new StringEntity(properties));
            httpResponse = httpClient.execute(httppost);
            response = getResponseFromHttp(httpResponse, urlPath);
        } catch (IOException exp) {
            LOGGER.error(urlPath + exp.getMessage(), exp);
            throw new FeatureException(exp.getMessage(), "internal.server.error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    private String getResponseFromHttp(HttpResponse httpResponse, String urlPath) throws IOException, FeatureException {
        throwEmptyValueException(httpResponse, urlPath);
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        if (statusCode != org.apache.http.HttpStatus.SC_OK) {
            throw new IOException("Invalid status :" + statusCode);
        }
        HttpEntity httpEntity = httpResponse.getEntity();
        throwEmptyValueException(httpEntity, urlPath);
        ContentType contentType = ContentType.getOrDefault(httpResponse.getEntity());
        Charset charset = contentType.getCharset() == null ? StandardCharsets.UTF_8 : contentType.getCharset();
        return EntityUtils.toString(httpEntity, charset);
    }

    private void throwEmptyValueException(Object value, String urlPath) throws FeatureException {
        if (value == null) {
            throw new FeatureException(urlPath, "empty.value", HttpStatus.BAD_REQUEST);
        }
    }
}
