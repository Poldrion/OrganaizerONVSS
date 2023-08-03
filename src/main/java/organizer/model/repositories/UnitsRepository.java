package organizer.model.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import organizer.model.BaseRepository;
import organizer.model.entities.Units;

@Repository
public interface UnitsRepository extends BaseRepository<Units, Integer>, JpaSpecificationExecutor<Units> {
    @Query("select u from Units u where upper(u.name) like upper(?1)")
    Units findByName(@NonNull String name);

    @Modifying
    @Transactional
    @Query(value = "BACKUP TO ?1", nativeQuery = true)
    int backupDB(String path);

}
