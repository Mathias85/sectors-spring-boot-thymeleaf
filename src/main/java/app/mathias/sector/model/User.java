package app.mathias.sector.model;

import lombok.Builder;

import java.util.List;

@Builder
public record User (
        Long id,
        String name,
        List<Sector> sectors,
        Boolean agreedToTerms
){}


