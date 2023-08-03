package organizer.model.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import organizer.model.BaseRepository;
import organizer.model.entities.Ordering;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderingRepository extends BaseRepository<Ordering, Long>, JpaSpecificationExecutor<Ordering> {
    @Query("select o from Ordering o where o.date between ?1 and ?2")
    List<Ordering> findByYear(@NonNull LocalDate dateStart, @NonNull LocalDate dateEnd);

    @Modifying
    @Transactional
    @Query(value = "BACKUP TO ?1", nativeQuery = true)
    int backupDB(String path);
}
