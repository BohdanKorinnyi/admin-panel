package com.omnia.admin.service;

import lombok.NonNull;
import lombok.extern.log4j.Log4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.StreamUtils;

import java.nio.charset.Charset;

@Log4j
public final class QueryHelper {
    private static final String PATH_PREFIX = "sql/";

    private QueryHelper() {
        throw new UnsupportedOperationException();
    }

    public static String loadQueryFromFile(@NonNull String fileName) {
        try {
            Resource resource = new ClassPathResource(PATH_PREFIX + fileName);
            return StreamUtils.copyToString(resource.getInputStream(), Charset.defaultCharset());
        } catch (Exception e) {
            log.error("Error occurred during loading sql file to string, file=" + fileName, e);
            throw new IllegalStateException();
        }
    }
}
