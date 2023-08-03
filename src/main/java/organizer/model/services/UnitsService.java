package organizer.model.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import organizer.model.OrganizerException;
import organizer.model.entities.Units;
import organizer.model.repositories.UnitsRepository;

import java.util.List;

@Service
public class UnitsService {

    @Autowired
    private UnitsRepository unitsRepository;


    public void save(Units units) {
        if (StringUtils.isEmpty(units.getName())) {
            throw new OrganizerException("Пожалуйста, введите наименование единицы измерения.");
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
