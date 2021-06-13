package com.silviucanton.easyorder.commons.utils;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class FileUtils {
    public static byte[] readImageToByteArray(String path) {
        Resource resource = new ClassPathResource(path);
        try(InputStream in = resource.getInputStream()) {
            return IOUtils.toByteArray(in);
        } catch (IOException e) {
            return null;
        }
    }
}
