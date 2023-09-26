package organizer.model.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import organizer.model.OrganizerException;
import organizer.model.entities.CodeKSM;
import organizer.model.repositories.CodeKSMRepository;
import organizer.utils.SpecificationUtils;
import organizer.views.controller.common.Dialog;

import java.util.*;

import static organizer.utils.Constants.*;

@Service
public class CodeKSMService {

    @Autowired
    private CodeKSMRepository codeKSMRepository;

    public List<CodeKSM> findByName(String name) {
        StringBuffer sb = new StringBuffer("select c from CodeKSM c where 1 = 1");
        Map<String, Object> params = new HashMap<>();

        if (!StringUtils.isEmpty(name)) {
            sb.append(" and lower(c.name) like lower(:name)");
            params.put("name", name.concat("%"));
        }

        return codeKSMRepository.findByQuery(sb.toString(), params);
    }

    public CodeKSM findById(String id) {
        return codeKSMRepository.findById(id).get();
    }

    public void save(CodeKSM codeKSM) {
        if (StringUtils.isEmpty(codeKSM.getId())) {
            throw new OrganizerException(ADD_CODE_KSM);
        }

        if (StringUtils.isEmpty(codeKSM.getName())) {
            throw new OrganizerException(ADD_SHORT_NAME_EQUIPMENT);
        }

        codeKSMRepository.save(codeKSM);

    }


    public void delete(String id) {
        codeKSMRepository.deleteById(id);
        Dialog.DialogBuilder.builder().title(COMPLETED_SUCCESSFULLY).message(REMOVE_CODE_KSM).build().show();
    }

    public List<CodeKSM> findAll() {
        return codeKSMRepository.findAll();
    }

    public static Specification<CodeKSM> containsText(String text) {
        return (Specification<CodeKSM>) SpecificationUtils.containsTextInAttributes(text, Arrays.asList("id", "name"));
    }

    public List<CodeKSM> findByAllColumns(String text) {
        return codeKSMRepository.findAll(Specification.where(containsText(text)));
    }
}
