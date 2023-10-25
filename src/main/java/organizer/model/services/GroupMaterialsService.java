package organizer.model.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import organizer.model.entities.GroupMaterials;
import organizer.model.repositories.GroupMaterialsRepository;

import java.util.List;

@Service
public class GroupMaterialsService {
	@Autowired
	private GroupMaterialsRepository groupMaterialsRepository;

	public void save(GroupMaterials groupMaterials) {
		groupMaterialsRepository.save(groupMaterials);
	}

	public void deleteById(int id) {
		groupMaterialsRepository.deleteById(id);
	}

	public List<GroupMaterials> findAll() {
		return groupMaterialsRepository.findAll();
	}

	public GroupMaterials findById(int id) {
		return groupMaterialsRepository.findById(id).get();
	}

	public List<GroupMaterials> findByName(String name) {
		return groupMaterialsRepository.findByName(name);
	}
}
