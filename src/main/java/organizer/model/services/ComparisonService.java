package organizer.model.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import organizer.model.OrganizerException;
import organizer.model.entities.Comparison;
import organizer.model.repositories.ComparisonRepository;

@Service
public class ComparisonService {

    @Autowired
    private ComparisonRepository comparisonRepository;

    public void save(Comparison comparison) {
        if (StringUtils.isEmpty(comparison.getId())) {
            throw new OrganizerException("Пожалуйста, введите год.");
        }
        if (comparison.getBusinessPlan()==null) {
            throw new OrganizerException("Пожалуйста, выберите версию БП.");
        }
        if (comparison.getCount()==null) {
            throw new OrganizerException("Пожалуйста, выберите год версии БП для таблицы кол-ва.");
        }
        if (comparison.getCost()==null) {
            throw new OrganizerException("Пожалуйста, выберите год версии БП для таблицы ст-ти.");
        }
        comparisonRepository.save(comparison);
    }

    public Comparison searchById(String id) {
        return comparisonRepository.findById(id).get();
    }
}
