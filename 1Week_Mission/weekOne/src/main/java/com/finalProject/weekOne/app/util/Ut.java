package com.finalProject.weekOne.app.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;

public class Ut {

    public static class url {
        public static boolean isUrl(String url) {
            if (url == null) return false;
            return url.matches("^(https?):\\/\\/([^:\\/\\s]+)(:([^\\/]*))?((\\/[^\\s/\\/]+)*)?\\/?([^#\\s\\?]*)(\\?([^#\\s]*))?(#(\\w*))?$");
        }

        public static String addQueryParam(String url, String paramName, String paramValue) {
            if (url.contains("?") == false) {
                url += "?";
            }

            if ( !url.endsWith("?") && !url.endsWith("&")) {
                url += "&";
            }

            url += paramName + "=" + encode(paramValue);

            return url;
        }

        public static String modifyQueryParam(String url, String paramName, String paramValue) {
            url = deleteQueryParam(url, paramName);
            url = addQueryParam(url, paramName, paramValue);

            return url;
        }

        private static String deleteQueryParam(String url, String paramName) {
            int startPoint = url.indexOf(paramName + "=");
            if (startPoint == -1) return url;

            int endPoint = url.substring(startPoint).indexOf("&");

            if (endPoint == -1) {
                return url.substring(0, startPoint - 1);
            }

            String urlAfter = url.substring(startPoint + endPoint + 1);

            return url.substring(0, startPoint) + urlAfter;
        }

        public static String encode(String str) {
            try {
                return URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                return str;
            }
        }

        public static String getQueryParamValue(String url, String paramName, String defaultValue) {
            String[] urlBits = url.split("\\?", 2);

            if (urlBits.length == 1) {
                return defaultValue;
            }

            urlBits = urlBits[1].split("&");

            String param = Arrays.stream(urlBits)
                    .filter(s -> s.startsWith(paramName + "="))
                    .findAny()
                    .orElse(paramName + "=" + defaultValue);

            String value = param.split("=", 2)[1].trim();

            return value.length() > 0 ? value : defaultValue;
        }
    }
}

