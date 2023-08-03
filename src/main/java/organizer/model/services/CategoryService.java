package organizer.model.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import organizer.model.entities.Category;
import organizer.model.OrganizerException;
import organizer.model.repositories.CategoryRepository;
import organizer.views.controller.common.Dialog;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }


    public void save(Category category) {
        if (StringUtils.isEmpty(category.getName())) {
            throw new OrganizerException("Пожалуйста, введите имя категории.");
        }
        categoryRepository.save(category);
    }


    public void delete(int id) {
        categoryRepository.deleteById(id);
        Dialog.DialogBuilder.builder().title("Выполнено успешно.").message("Удаление категории выполнено успешно.").build().show();
    }

    public Category findById(int id) {
        return categoryRepository.findById(id).get();
    }
}
