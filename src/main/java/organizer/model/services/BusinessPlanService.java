package organizer.model.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import organizer.model.OrganizerException;
import organizer.model.entities.BusinessPlan;
import organizer.model.repositories.BusinessPlanRepository;
import organizer.views.controller.common.Dialog;

import java.util.List;

@Service
public class BusinessPlanService {

    @Autowired
    private BusinessPlanRepository businessPlanRepository;

    public List<BusinessPlan> findAll() {
        return businessPlanRepository.findAll();
    }

    public void save(BusinessPlan businessPlan) {
        if (StringUtils.isEmpty(businessPlan.getId())) {
            throw new OrganizerException("Пожалуйста, введите ID.");
        }
        if (StringUtils.isEmpty(businessPlan.getFirstYear())) {
            throw new OrganizerException("Пожалуйста, введите первый год версии БП.");
        }
        businessPlanRepository.save(businessPlan);
    }

    public void delete(String id) {
        businessPlanRepository.deleteById(id);
        Dialog.DialogBuilder.builder().title("Выполнено успешно.").message("Удаление версии БП выполнено успешно.").build().show();
    }

    public BusinessPlan findById(String id) {
        return businessPlanRepository.findById(id).get();
    }
}
