package io.horrorshow.soulhub.data.api;

import io.horrorshow.soulhub.data.SPFile;

import static io.horrorshow.soulhub.data.SPFile.FileType.*;
import static java.lang.String.format;

public class SOULPatchParser {
    private static final String MANIFEST_FILE_EXT = "soulpatch";
    private static final String SOUL_FILE_EXT = "soul";

    public static SPFile.FileType guessFileType(SPFile spFile) {
        if (spFile.getName() == null)
            return UNKNOWN;

        String filename = spFile.getName().toLowerCase();
        if (filename.endsWith(format(".%s", MANIFEST_FILE_EXT))) {
            return MANIFEST;
        } else if (filename.endsWith(format(".%s", SOUL_FILE_EXT))) {
            return SOUL;
        } else if (filename.matches("\\w+.*\\.+\\w+")) {
            return OTHER;
        } else {
            return UNKNOWN;
        }
    }
}
