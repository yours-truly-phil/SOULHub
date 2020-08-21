package io.horrorshow.soulhub.data.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringUtilsTest {

    @Test
    void toValidFilename() {
        assertEquals("hello", StringUtils.toValidFilename("hello"));
        assertEquals("my_file", StringUtils.toValidFilename("my file"));
        assertEquals("filename", StringUtils.toValidFilename(""));
        assertEquals("filename", StringUtils.toValidFilename(null));
        assertEquals("asdf", StringUtils.toValidFilename("a\\s-`´d§$%&f/\""));
        assertEquals("filename", StringUtils.toValidFilename("=)(/&%$§!"));
        assertEquals("asdf.soul", StringUtils.toValidFilename("asdf.soul"));
        assertEquals("asdf.soulpatch", StringUtils.toValidFilename("asdf.soulpatch"));
        assertEquals("_.._", StringUtils.toValidFilename(".."));
        assertEquals("_._", StringUtils.toValidFilename("."));
        assertEquals("_.asdf.asdf._", StringUtils.toValidFilename(".asdf.asdf."));
    }
}