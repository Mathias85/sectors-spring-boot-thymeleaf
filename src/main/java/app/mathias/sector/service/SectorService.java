package app.mathias.sector.service;

import app.mathias.sector.model.Sector;

import java.util.List;

public interface SectorService {

    /**
     * Fetches all top-level sectors with their hierarchical structure.
     * @return List of sectors
     * @throws app.mathias.sector.exception.ResourceNotFoundException if no sectors are found
     */
    List<Sector> fetchAllSectors();

    /**
     * Finds multiple sectors by their IDs.
     * @param ids list of sector IDs
     * @return list of sectors
     */
    List<Sector> findSectorsByIds(List<Integer> ids);
}
