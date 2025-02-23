package app.mathias.sector.repository;

import app.mathias.sector.entity.SectorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SectorRepository extends JpaRepository<SectorEntity, Integer> {

    List<SectorEntity> findAllByParentIdIsNullOrderByName();
}
