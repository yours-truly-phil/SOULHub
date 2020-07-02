package io.horrorshow.soulhub.data.records;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@EqualsAndHashCode
@Getter
@AllArgsConstructor
@ToString
public class SPFileRecord {

    private final Long id;
    private final String type;
    private final String filename;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final String content;

}
