package organizer.model.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import organizer.model.OrganizerException;
import organizer.model.entities.Nomenclature;
import organizer.model.entities.Subcategory;
import organizer.model.repositories.NomenclatureRepository;
import organizer.utils.SpecificationUtils;
import organizer.views.controller.common.Dialog;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static organizer.utils.Constants.*;

@Service
public class NomenclatureService {

    @Autowired
    private NomenclatureRepository nomenclatureRepository;

    public List<Nomenclature> findAll() {
        return nomenclatureRepository.findAll();
    }

    public void save(Nomenclature nomenclature) {
        if (StringUtils.isEmpty(nomenclature.getName())) {
            throw new OrganizerException(ADD_NOMENCLATURE_NAME);
        }
        nomenclatureRepository.save(nomenclature);
    }

    public void delete(String id) {
        nomenclatureRepository.deleteById(id);
        Dialog.DialogBuilder.builder().title(COMPLETED_SUCCESSFULLY).message(REMOVE_NOMENCLATURE).build().show();
    }

    public List<Nomenclature> searchBySubcategoryAndName(Subcategory subcategory, String name) {
        StringBuffer sb = new StringBuffer("select o from Nomenclature o where 1 = 1");
        Map<String, Object> params = new HashMap<>();

        if (subcategory != null) {
            sb.append(" and o.subcategory = :subcategory");
            params.put("subcategory", subcategory);
        }

        if (!StringUtils.isEmpty(name)) {
            sb.append(" and lower(o.name) like concat('%', lower(:name), '%')");
            params.put("name", name.concat("%"));
        }


        return nomenclatureRepository.findByQuery(sb.toString(), params);
    }

    public List<Nomenclature> searchBySubcategory(Subcategory subcategory) {
        StringBuffer sb = new StringBuffer("select o from Nomenclature o where 1 = 1");
        Map<String, Object> params = new HashMap<>();

        if (subcategory != null) {
            sb.append(" and o.subcategory = :subcategory");
            params.put("subcategory", subcategory);
        }
        return nomenclatureRepository.findByQuery(sb.toString(), params);
    }

    public List<Nomenclature> findByNameContainingIgnoreCase(String name) {
        return nomenclatureRepository.findByNameContainingIgnoreCase(name);
    }

    public static Specification<Nomenclature> containsText(String text) {
        return (Specification<Nomenclature>) SpecificationUtils.containsTextInAttributes(text,
                Arrays.asList("id", "name", "codeKSM_.id", "codeKSM_.name", "technicalRequirement_.id", "technicalRequirement_.name"));
    }



    public List<Nomenclature> findByAllColumns(String text) {
        return nomenclatureRepository.findAll(Specification.where(containsText(text)));
    }

    public Nomenclature findById(String id) {
        return nomenclatureRepository.findById(id).get();
    }

}
