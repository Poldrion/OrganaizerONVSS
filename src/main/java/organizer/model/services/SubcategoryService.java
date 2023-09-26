package organizer.model.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import organizer.model.OrganizerException;
import organizer.model.entities.Category;
import organizer.model.entities.Subcategory;
import organizer.model.repositories.SubcategoryRepository;
import organizer.views.controller.common.Dialog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static organizer.utils.Constants.*;

@Service
public class SubcategoryService {

    @Autowired
    private SubcategoryRepository subcategoryRepository;

    public List<Subcategory> findAll() {
        return subcategoryRepository.findAll();
    }

    public void save(Subcategory subcategory) {
        if (StringUtils.isEmpty(subcategory.getName())) {
            throw new OrganizerException(ADD_SUBCATEGORY_NAME);
        }
        subcategoryRepository.save(subcategory);
    }

    public void delete(int id) {
        subcategoryRepository.deleteById(id);
        Dialog.DialogBuilder.builder().title(COMPLETED_SUCCESSFULLY).message(REMOVE_SUBCATEGORY).build().show();
    }

    public List<Subcategory> search(Category category) {
        StringBuffer sb = new StringBuffer("select p from Subcategory p where 1 = 1");
        Map<String, Object> params = new HashMap<>();

        if (category != null) {
            sb.append(" and p.category = :category");
            params.put("category", category);
        }
        return subcategoryRepository.findByQuery(sb.toString(), params);
    }

    public Subcategory findById(int id) {
        return subcategoryRepository.findById(id).get();
    }
}