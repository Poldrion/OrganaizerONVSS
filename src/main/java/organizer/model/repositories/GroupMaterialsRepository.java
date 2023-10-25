package organizer.model.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import organizer.model.BaseRepository;
import organizer.model.entities.GroupMaterials;

import java.util.List;

@Repository
public interface GroupMaterialsRepository extends BaseRepository<GroupMaterials, Integer>, JpaSpecificationExecutor<GroupMaterials> {
	@Query("select g from GroupMaterials g where g.name = ?1")
	List<GroupMaterials> findByName(String name);
}
