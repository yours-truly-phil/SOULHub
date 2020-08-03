package io.horrorshow.soulhub.data.records;

import io.horrorshow.soulhub.data.AppUser;
import io.horrorshow.soulhub.data.SOULPatch;
import io.horrorshow.soulhub.data.SPFile;

import java.io.Serializable;
import java.util.Base64;
import java.util.stream.Collectors;

public class RecordsConverter implements Serializable {

    private static final long serialVersionUID = 8750084355540746031L;

    public static SOULPatchRecord newSoulPatchRecord(SOULPatch soulPatch) {
        return new SOULPatchRecord(
                soulPatch.getId(),
                soulPatch.getName(),
                soulPatch.getDescription(),
                soulPatch.getSpFiles().stream().map(RecordsConverter::newSPFileRecord).collect(Collectors.toSet()),
                newUserRecord(soulPatch.getAuthor()),
                soulPatch.getCreatedAt(),
                soulPatch.getUpdatedAt()
        );
    }

    public static SPFileRecord newSPFileRecord(SPFile spFile) {
        return new SPFileRecord(
                spFile.getId(),
                (spFile.getFileType() != null) ? spFile.getFileType().toString() : "",
                spFile.getName(),
                spFile.getCreatedAt(),
                spFile.getUpdatedAt(),
                Base64.getEncoder().encodeToString(spFile.getFileContent().getBytes())
        );
    }

    public static UserRecord newUserRecord(AppUser user) {
        return new UserRecord(
                user.getUserName(),
                user.getEmail());
    }

}
