package app.mathias.sector.mapper;

import app.mathias.sector.entity.SectorEntity;
import app.mathias.sector.model.Sector;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class SectorMapper {

    public Sector toSector(SectorEntity sectorEntity) {
        return Optional.ofNullable(sectorEntity)
                .map(entity -> Sector.builder()
                        .id(entity.getId())
                        .name(entity.getName())
                        .parentId(toParentId(entity.getParent()))
                        .children(toSectorList(entity.getChildren()))
                        .build())
                .orElse(null);
    }

    public List<Sector> toSectorList(List<SectorEntity> entityList) {
        return Optional.ofNullable(entityList)
                .orElseGet(Collections::emptyList)
                .stream()
                .map(this::toSector)
                .toList();
    }

    private Integer toParentId(SectorEntity parent) {
        return Optional.ofNullable(parent)
                .map(SectorEntity::getId)
                .orElse(null);
    }


}
