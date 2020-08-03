package io.horrorshow.soulhub.data.api;

import io.horrorshow.soulhub.data.SPFile;
import org.junit.jupiter.api.Test;

import static io.horrorshow.soulhub.data.SPFile.FileType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SOULPatchParserTest {

    @Test
    void parse_SOULPatch_by_file_name_ending() {
        SPFile file = new SPFile();
        String name = null;
        file.setName(null);
        assertEquals(UNKNOWN, SOULPatchParser.guessFileType(file), "null");
        name = "";
        file.setName(name);
        assertEquals(UNKNOWN, SOULPatchParser.guessFileType(file), name);
        name = "dflkj";
        file.setName(name);
        assertEquals(UNKNOWN, SOULPatchParser.guessFileType(file), name);
        name = "a.soulpatch";
        file.setName(name);
        assertEquals(MANIFEST, SOULPatchParser.guessFileType(file), name);
        name = "asdf.adf.soulpatch";
        file.setName(name);
        assertEquals(MANIFEST, SOULPatchParser.guessFileType(file), name);
        name = "asdf.soul";
        file.setName(name);
        assertEquals(SOUL, SOULPatchParser.guessFileType(file), name);
        name = "asdf.a.soul";
        file.setName(name);
        assertEquals(SOUL, SOULPatchParser.guessFileType(file), name);
        name = "asdf.asdf";
        file.setName(name);
        assertEquals(OTHER, SOULPatchParser.guessFileType(file), name);
        name = "asdf.asdf.asdf";
        file.setName(name);
        assertEquals(OTHER, SOULPatchParser.guessFileType(file), name);
    }
}
