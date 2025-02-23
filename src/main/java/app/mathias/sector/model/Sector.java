package app.mathias.sector.model;

import lombok.Builder;

import java.util.List;

@Builder
public record Sector(
        Integer id,
        String name,
        Integer parentId,
        List<Sector> children
) {}
