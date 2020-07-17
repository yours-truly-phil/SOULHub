package io.horrorshow.soulhub.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class RatingId implements Serializable {
    private static final long serialVersionUID = -8480954240291129849L;

    private SOULPatch soulPatch;

    private AppUser appUser;
}
