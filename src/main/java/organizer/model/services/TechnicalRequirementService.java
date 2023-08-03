package organizer.model.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import organizer.model.OrganizerException;
import organizer.model.entities.TechnicalRequirement;
import organizer.model.repositories.TechnicalRequirementRepository;
import organizer.utils.SpecificationUtils;
import organizer.views.controller.common.Dialog;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TechnicalRequirementService {
    @Autowired
    private TechnicalRequirementRepository technicalRequirementRepository;

    public void save(TechnicalRequirement technicalRequirement) {
        if (StringUtils.isEmpty(technicalRequirement.getId())) {
            throw new OrganizerException("Пожалуйста, введите номер карточки ОЛ/ТТ.");
        }
        if (StringUtils.isEmpty(technicalRequirement.getName())) {
            throw new OrganizerException("Пожалуйста, введите наименование МТР в карточке ОЛ/ТТ.");
        }
        if (StringUtils.isEmpty(technicalRequirement.getCodeKSM())) {
            throw new OrganizerException("Пожалуйста, введите код КСМ.");
        }
        if (StringUtils.isEmpty(technicalRequirement.getFileTRName())) {
            throw new OrganizerException("Пожалуйста, введите название файла ТЗ.");
        }
        if (StringUtils.isEmpty(technicalRequirement.getSystemNumberTransaction())) {
            throw new OrganizerException("Пожалуйста, введите номер системной транзакции.");
        }
        if (StringUtils.isEmpty(technicalRequirement.getDateCreate())) {
            throw new OrganizerException("Пожалуйста, введите дату создания карточки ОЛ/ТТ.");
        }

        technicalRequirementRepository.save(technicalRequirement);
    }

    public List<TechnicalRequirement> findAll() {
        return technicalRequirementRepository.findAll();
    }

    public void delete(String id) {
        technicalRequirementRepository.deleteById(id);
        Dialog.DialogBuilder.builder().title("Выполнено успешно.").message("Удаление карточки ОЛ/ТТ выполнено успешно.").build().show();
    }

    public List<TechnicalRequirement> findByName(String name) {
        StringBuffer sb = new StringBuffer("select t from TechnicalRequirement t where 1 = 1");
        Map<String, Object> params = new HashMap<>();

        if (!StringUtils.isEmpty(name)) {
            sb.append(" and lower(t.name) like concat('%', lower(:name), '%')");
            params.put("name", name.concat("%"));
        }
        return technicalRequirementRepository.findByQuery(sb.toString(), params);
    }

    public List<TechnicalRequirement> findContainById(String id) {
        StringBuffer sb = new StringBuffer("select t from TechnicalRequirement t where 1 = 1");
        Map<String, Object> params = new HashMap<>();

        if (!StringUtils.isEmpty(id)) {
            sb.append(" and lower(t.id) like concat('%', lower(:id), '%')");
            params.put("id", id.concat("%"));
        }
        return technicalRequirementRepository.findByQuery(sb.toString(), params);
    }

    public List<TechnicalRequirement> findContainByCodeKSM(String id) {
        StringBuffer sb = new StringBuffer("select t from TechnicalRequirement t where 1 = 1");
        Map<String, Object> params = new HashMap<>();

        if (!StringUtils.isEmpty(id)) {
            sb.append(" and lower(t.codeKSM) like concat('%', lower(:codeKSM), '%')");
            params.put("codeKSM", id.concat("%"));
        }
        return technicalRequirementRepository.findByQuery(sb.toString(), params);
    }

    public List<TechnicalRequirement> findContainByCodeKSMName(String id) {
        StringBuffer sb = new StringBuffer("select t from TechnicalRequirement t where 1 = 1");
        Map<String, Object> params = new HashMap<>();

        if (!StringUtils.isEmpty(id)) {
            sb.append(" and lower(t.codeKSMName) like concat('%', lower(:codeKSMName), '%')");
            params.put("codeKSMName", id.concat("%"));
        }
        return technicalRequirementRepository.findByQuery(sb.toString(), params);
    }


    public static Specification<TechnicalRequirement> containsText(String text) {
        return (Specification<TechnicalRequirement>) SpecificationUtils.containsTextInAttributes(text, Arrays.asList("id", "name", "codeKSM", "codeKSMName"));
    }

    public List<TechnicalRequirement> findAllBySomeColumn(String text) {
        return technicalRequirementRepository.findAll(Specification.where(containsText(text)));
    }

    public TechnicalRequirement findById(String id) {
        return technicalRequirementRepository.findById(id).get();
    }
}

