package io.horrorshow.soulhub.data.records;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@Getter
@AllArgsConstructor
@ToString
public class UserRecord {

    private final String username;
    private final String email;

}
