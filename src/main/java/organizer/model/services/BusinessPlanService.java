package organizer.model.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import organizer.model.OrganizerException;
import organizer.model.entities.BusinessPlan;
import organizer.model.repositories.BusinessPlanRepository;
import organizer.views.controller.common.Dialog;

import java.util.List;

import static organizer.utils.Constants.*;

@Service
public class BusinessPlanService {

    @Autowired
    private BusinessPlanRepository businessPlanRepository;

    public List<BusinessPlan> findAll() {
        return businessPlanRepository.findAll();
    }

    public void save(BusinessPlan businessPlan) {
        if (StringUtils.isEmpty(businessPlan.getId())) {
            throw new OrganizerException(ADD_ID);
        }
        if (StringUtils.isEmpty(businessPlan.getFirstYear())) {
            throw new OrganizerException(ADD_FIRST_YEAR_FOR_BP);
        }
        businessPlanRepository.save(businessPlan);
    }

    public void delete(String id) {
        businessPlanRepository.deleteById(id);
        Dialog.DialogBuilder.builder().title(COMPLETED_SUCCESSFULLY).message(REMOVE_VERSION_BP).build().show();
    }

    public BusinessPlan findById(String id) {
        return businessPlanRepository.findById(id).get();
    }
}
