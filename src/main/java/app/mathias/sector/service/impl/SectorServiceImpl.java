package app.mathias.sector.service.impl;

import app.mathias.sector.entity.SectorEntity;
import app.mathias.sector.exception.ResourceNotFoundException;
import app.mathias.sector.mapper.SectorMapper;
import app.mathias.sector.model.Sector;
import app.mathias.sector.repository.SectorRepository;
import app.mathias.sector.service.SectorService;
import app.mathias.sector.utils.MessageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SectorServiceImpl implements SectorService {

    private final SectorRepository sectorRepository;
    private final SectorMapper sectorMapper;

    @Override
    @Cacheable(value = "sectors")
    public List<Sector> fetchAllSectors() {
        final List<SectorEntity> allSectors = sectorRepository.findAllByParentIdIsNullOrderByName();
        if (allSectors.isEmpty()) {
            throw new ResourceNotFoundException(MessageHelper.getMessage("error.sectors.notfound"));
        }
        return sectorMapper.toSectorList(allSectors);
    }

    @Override
    @Cacheable(value = "sectors", key = "#ids")
    public List<Sector> findSectorsByIds(List<Integer> ids) {
        final List<SectorEntity> sectors = sectorRepository.findAllById(ids);
        return sectorMapper.toSectorList(sectors);
    }
}
