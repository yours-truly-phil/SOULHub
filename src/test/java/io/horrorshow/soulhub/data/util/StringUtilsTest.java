package io.horrorshow.soulhub.data.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StringUtilsTest {

    @Test
    void toValidFilename() {
        assertThat(StringUtils.toValidFilename("hello")).isEqualTo("hello");
        assertThat(StringUtils.toValidFilename("my file")).isEqualTo("my_file");
        assertThat(StringUtils.toValidFilename("")).isEqualTo("filename");
        assertThat(StringUtils.toValidFilename(null)).isEqualTo("filename");
        assertThat(StringUtils.toValidFilename("a\\s-`´d§$%&f/\"")).isEqualTo("asdf");
        assertThat(StringUtils.toValidFilename("=)(/&%$§!")).isEqualTo("filename");
        assertThat(StringUtils.toValidFilename("asdf.soul")).isEqualTo("asdf.soul");
        assertThat(StringUtils.toValidFilename("asdf.soulpatch")).isEqualTo("asdf.soulpatch");
        assertThat(StringUtils.toValidFilename("..")).isEqualTo("_.._");
        assertThat(StringUtils.toValidFilename(".")).isEqualTo("_._");
        assertThat(StringUtils.toValidFilename(".asdf.asdf.")).isEqualTo("_.asdf.asdf._");
    }
}