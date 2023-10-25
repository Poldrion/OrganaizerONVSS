package organizer.model.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import organizer.model.BaseRepository;
import organizer.model.entities.MacroParameters;

import java.util.List;

@Repository
public interface MacroParametersRepository extends BaseRepository<MacroParameters, Integer>, JpaSpecificationExecutor<MacroParameters> {
	@Query("select m from MacroParameters m where m.firstYear = ?1")
	List<MacroParameters> findByFirstYear(int firstYear);


}