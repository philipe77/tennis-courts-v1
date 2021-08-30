package com.tenniscourts.guests;

import com.tenniscourts.config.persistence.Profile;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GuestDTO {

    private Long id;

    @NotNull
    private String name;

    private Profile profile;

}
