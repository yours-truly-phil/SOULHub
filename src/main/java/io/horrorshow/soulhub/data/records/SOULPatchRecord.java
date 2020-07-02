package io.horrorshow.soulhub.data.records;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Set;

@EqualsAndHashCode
@Getter
@AllArgsConstructor
@ToString
public class SOULPatchRecord {

    private final Long id;
    private final String name;
    private final String description;
    private final Set<SPFileRecord> files;
    private final UserRecord createdBy;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

}