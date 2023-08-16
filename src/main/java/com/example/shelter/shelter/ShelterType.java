package com.example.shelter.shelter;

import com.example.shelter.exception.notfound.ShelterTypeNotFoundException;
import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum ShelterType {
    TSUNAMI("tsunami"),
    EARTHQUAKE("earthquake"),
    CIVIL_DEFENCE("civilDefense");

    private String name;

    ShelterType(String name) {
        this.name = name;
    }

    private static final Map<String, ShelterType> shelterTypeMap =
            Collections.unmodifiableMap(Stream.of(values())
                    .collect(Collectors.toMap(ShelterType::getName, Function.identity())));

    public String getName() {
        return name;
    }

    @JsonCreator
    public static ShelterType getShelterType(String name) {
        return Optional.ofNullable(shelterTypeMap.get(name))
                .orElseThrow(() -> new ShelterTypeNotFoundException(name));
    }

}
