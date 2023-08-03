package organizer.model.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import organizer.model.BaseRepository;
import organizer.model.entities.TechnicalRequirement;

@Repository
public interface TechnicalRequirementRepository extends BaseRepository<TechnicalRequirement, String>, JpaSpecificationExecutor<TechnicalRequirement> {
    @Modifying
    @Transactional
    @Query(value = "BACKUP TO ?1", nativeQuery = true)
    int backupDB(String path);
}
