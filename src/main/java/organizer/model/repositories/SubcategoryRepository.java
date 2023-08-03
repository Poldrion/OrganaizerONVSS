package organizer.model.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import organizer.model.BaseRepository;
import organizer.model.entities.Subcategory;

@Repository
public interface SubcategoryRepository extends BaseRepository<Subcategory, Integer> {
    @Modifying
    @Transactional
    @Query(value = "BACKUP TO ?1", nativeQuery = true)
    int backupDB(String path);
}
