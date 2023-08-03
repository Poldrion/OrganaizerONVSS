package organizer.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@NoRepositoryBean
public interface BaseRepository<E, ID> extends JpaRepository<E, ID> {

    List<E> findByQuery(String jpql, Map<String, Object> params);
}
