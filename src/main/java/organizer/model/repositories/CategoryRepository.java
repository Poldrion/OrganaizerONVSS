package organizer.model.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import organizer.model.BaseRepository;
import organizer.model.entities.Category;

@Repository
public interface CategoryRepository extends BaseRepository<Category, Integer> {
    @Modifying
    @Transactional
    @Query(value = "BACKUP TO ?1", nativeQuery = true)
    int backupDB(String path);

	@Query("select c from Category c where c.name like ?1")
	Category findByName(String name);
}
