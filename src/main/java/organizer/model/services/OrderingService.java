package organizer.model.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import organizer.model.OrganizerException;
import organizer.model.entities.Ordering;
import organizer.model.repositories.OrderingRepository;
import organizer.utils.SpecificationUtils;
import organizer.views.controller.common.Dialog;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
public class OrderingService {

    @Autowired
    private OrderingRepository orderingRepository;

    public List<Ordering> search(String text) {
        return orderingRepository.findAll(Specification.where(containsText(text)));

    }

    public static Specification<Ordering> containsText(String text) {
        return (Specification<Ordering>) SpecificationUtils.containsTextInAttributes(text, Arrays.asList("number", "nomenclature_.codeKSM_.name"));
    }


    public void save(Ordering ordering) {
        if (ordering.getNomenclature() == null) {
            throw new OrganizerException("Проверьте правильность ввода кода КСМ и/или номера карточки ОЛ/ТТ.");
        }
        orderingRepository.save(ordering);
    }

    public List<Ordering> findAll() {
        return orderingRepository.findAll();
    }

    public void deleteById(long id) {
        orderingRepository.deleteById(id);
        Dialog.DialogBuilder.builder().title("Выполнено успешно.").message("Удаление заказа выполнено успешно.").build().show();
    }

    public List<Ordering> findByYear(LocalDate dateStart, LocalDate dateEnd) {
        return orderingRepository.findByYear(dateStart, dateEnd);
    }

    public Ordering findById(long id) {
        return orderingRepository.findById(id).get();
    }
}
