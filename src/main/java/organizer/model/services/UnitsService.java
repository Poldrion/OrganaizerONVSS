package organizer.model.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import organizer.model.OrganizerException;
import organizer.model.entities.Units;
import organizer.model.repositories.UnitsRepository;

import java.util.List;

import static organizer.utils.Constants.ADD_UNIT_NAME;

@Service
public class UnitsService {

    @Autowired
    private UnitsRepository unitsRepository;


    public void save(Units units) {
        if (StringUtils.isEmpty(units.getName())) {
            throw new OrganizerException(ADD_UNIT_NAME);
        }
        unitsRepository.save(units);
    }

    public List<Units> findAll() {
        return unitsRepository.findAll();
    }

    public Units findByName(String name){
        return unitsRepository.findByName(name);
    }


    public void deleteById(int id) {
        unitsRepository.deleteById(id);
    }
}
