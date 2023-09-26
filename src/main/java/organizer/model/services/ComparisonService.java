package organizer.model.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import organizer.model.OrganizerException;
import organizer.model.entities.Comparison;
import organizer.model.repositories.ComparisonRepository;

import static organizer.utils.Constants.*;

@Service
public class ComparisonService {

    @Autowired
    private ComparisonRepository comparisonRepository;

    public void save(Comparison comparison) {
        if (StringUtils.isEmpty(comparison.getId())) {
            throw new OrganizerException(ADD_YEAR);
        }
        if (comparison.getBusinessPlan()==null) {
            throw new OrganizerException(CHOOSE_VERSION_BP);
        }
        if (comparison.getCount()==null) {
            throw new OrganizerException(CHOOSE_YEAR_VERSION_BP_FOR_COUNT_TABLE);
        }
        if (comparison.getCost()==null) {
            throw new OrganizerException(CHOOSE_YEAR_VERSION_BP_FOR_COST_TABLE);
        }
        comparisonRepository.save(comparison);
    }

    public Comparison searchById(String id) {
        return comparisonRepository.findById(id).get();
    }
}
