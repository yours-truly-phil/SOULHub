package io.horrorshow.soulhub.data.records;

import io.horrorshow.soulhub.data.AppUser;
import io.horrorshow.soulhub.data.SOULPatch;
import io.horrorshow.soulhub.data.SPFile;

import java.util.stream.Collectors;

public class RecordsConverter {

    public static SOULPatchRecord newSoulPatchRecord(SOULPatch soulPatch) {
        return new SOULPatchRecord(
                soulPatch.getId(),
                soulPatch.getName(),
                soulPatch.getDescription(),
                soulPatch.getSpFiles().stream().map(RecordsConverter::newSPFileRecord).collect(Collectors.toSet()),
                new UserRecord(soulPatch.getAuthor()),
                soulPatch.getCreatedAt(),
                soulPatch.getUpdatedAt()
        );
    }

    public static SPFileRecord newSPFileRecord(SPFile spFile) {
        return new SPFileRecord(
                spFile.getId(),
                spFile.getFileType().toString(),
                spFile.getName(),
                spFile.getCreatedAt(),
                spFile.getUpdatedAt(),
                spFile.getFileContent()
        );
    }

    public static UserRecord newUserRecord(AppUser user) {
        return new UserRecord(user.getUserName());
    }

}
