package organizer.model.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import organizer.model.BaseRepository;
import organizer.model.entities.FinancialAnalysts;

import java.util.List;

@Repository
public interface FinancialAnalystsRepository extends BaseRepository<FinancialAnalysts, Long>, JpaSpecificationExecutor<FinancialAnalysts> {
	@Query("select f from FinancialAnalysts f where f.yearAFE = ?1")
	List<FinancialAnalysts> findByYear(int year);

}
