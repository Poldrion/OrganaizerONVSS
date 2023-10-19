package organizer.model.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import organizer.model.BaseRepository;
import organizer.model.entities.Ordering;
import organizer.model.entities.OrderingResult;

@Repository
public interface OrderingResultRepository extends BaseRepository<OrderingResult, Long>, JpaSpecificationExecutor<OrderingResult> {
	@Query("select o from OrderingResult o where o.number like ?1 and o.position like ?2")
	OrderingResult findByNumberAndPosition(String number, String position);

	@Query("select o from OrderingResult o where o.ordering = ?1")
	OrderingResult findByOrdering(Ordering ordering);
}
