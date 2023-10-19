package organizer.model.services;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import organizer.model.OrganizerException;
import organizer.model.entities.Ordering;
import organizer.model.repositories.OrderingRepository;
import organizer.utils.CurrentData;
import organizer.utils.SpecificationUtils;
import organizer.views.controller.common.Dialog;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static organizer.utils.Constants.*;

@Service
public class OrderingService {

	@Autowired
	private OrderingRepository orderingRepository;

	public void save(Ordering ordering) {
		if (ordering.getNomenclature() == null) {
			throw new OrganizerException(CHECK_CODE_KSM_OR_TECHNICAL_REQUIREMENT);
		}
		orderingRepository.save(ordering);
	}

	public void deleteById(long id) {
		orderingRepository.deleteById(id);
		Dialog.DialogBuilder.builder().title(COMPLETED_SUCCESSFULLY).message(REMOVE_ORDERING).build().show();
	}

	public List<Ordering> search(String text) {
		return orderingRepository.findAll(Specification.where(containsText(text)));
	}

	public static Specification<Ordering> containsText(String text) {
		return (Specification<Ordering>) SpecificationUtils.containsTextInAttributes(text, Arrays.asList("number", "nomenclature_.codeKSM_.name"));
	}

	public List<Ordering> findAll() {
		return orderingRepository.findAll();
	}

	public List<Ordering> findByYear(LocalDate dateStart, LocalDate dateEnd) {
		return orderingRepository.findByYear(dateStart, dateEnd);
	}

	public Ordering findById(long id) {
		return orderingRepository.findById(id).get();
	}

	public Ordering findByNumberAndPosition(String number, String position) {
		return orderingRepository.findByNumberAndPosition(number, position);
	}

	/**
	 * Метод для фильтрации заказов на поставку со сроком поставки в выбранный период
	 *
	 * @param orderingList коллекция с исходными зазаказами
	 * @return отфильтрованная коллекция
	 */
	private List<Ordering> getOrderingsForCurrentPeriod(String period, List<Ordering> orderingList) {
		orderingList = switch (period) {
			case JANUARY ->
					orderingList.stream().filter(x -> x.getDate().getMonth().getValue() == 1).collect(Collectors.toList());
			case FEBRUARY ->
					orderingList.stream().filter(x -> x.getDate().getMonth().getValue() == 2).collect(Collectors.toList());
			case MARCH ->
					orderingList.stream().filter(x -> x.getDate().getMonth().getValue() == 3).collect(Collectors.toList());
			case APRIL ->
					orderingList.stream().filter(x -> x.getDate().getMonth().getValue() == 4).collect(Collectors.toList());
			case MAY ->
					orderingList.stream().filter(x -> x.getDate().getMonth().getValue() == 5).collect(Collectors.toList());
			case JUNE ->
					orderingList.stream().filter(x -> x.getDate().getMonth().getValue() == 6).collect(Collectors.toList());
			case JULY ->
					orderingList.stream().filter(x -> x.getDate().getMonth().getValue() == 7).collect(Collectors.toList());
			case AUGUST ->
					orderingList.stream().filter(x -> x.getDate().getMonth().getValue() == 8).collect(Collectors.toList());
			case SEPTEMBER ->
					orderingList.stream().filter(x -> x.getDate().getMonth().getValue() == 9).collect(Collectors.toList());
			case OCTOBER ->
					orderingList.stream().filter(x -> x.getDate().getMonth().getValue() == 10).collect(Collectors.toList());
			case NOVEMBER ->
					orderingList.stream().filter(x -> x.getDate().getMonth().getValue() == 11).collect(Collectors.toList());
			case DECEMBER ->
					orderingList.stream().filter(x -> x.getDate().getMonth().getValue() == 12).collect(Collectors.toList());
			default -> orderingList;
		};
		return orderingList;
	}

	/**
	 * Метод для фильтрации заказов на поставку только для выбранной категории
	 *
	 * @param orderingList коллекция с исходными зазаказами
	 * @return отфильтрованная коллекция
	 */
	private List<Ordering> getOrderingsForCurrentCategory(ComboBox<String> category, List<Ordering> orderingList) {
		if (!category.getSelectionModel().getSelectedItem().equals(ALL_CATEGORIES)) {
			orderingList = orderingList.stream()
					.filter(x -> x.getNomenclature().getSubcategory().getCategory().getName().equals(category.getSelectionModel().getSelectedItem()))
					.collect(Collectors.toList());
		}
		return orderingList;
	}

	/**
	 * Метод для фильтрации только заказов на поставку по лизинговой схеме
	 *
	 * @param orderingList коллекция с исходными зазаказами
	 * @return отфильтрованная коллекция
	 */
	private List<Ordering> getOnlyLeasingOrderings(RadioButton onlyLeasing, List<Ordering> orderingList) {
		if (onlyLeasing.isSelected()) {
			orderingList = orderingList.stream().filter(Ordering::isLeasing).collect(Collectors.toList());
		}
		return orderingList;
	}

	/**
	 * Метод для фильтрации только заказов на поставку без лизинговой схемы
	 *
	 * @param orderingList коллекция с исходными зазаказами
	 * @return отфильтрованная коллекция
	 */
	private List<Ordering> getWithoutLeasingOrderings(RadioButton withoutLeasing, List<Ordering> orderingList) {
		if (withoutLeasing.isSelected()) {
			orderingList = orderingList.stream().filter(x -> !x.isLeasing()).collect(Collectors.toList());
		}
		return orderingList;
	}

	/**
	 * Метод для фильтрации заказов на поставку с нулевым количеством
	 *
	 * @param orderingList коллекция с исходными зазаказами
	 * @return отфильтрованная коллекция
	 */
	private List<Ordering> getOrderingsWithZeroCount(CheckBox emptyCategory, List<Ordering> orderingList) {
		if (!emptyCategory.isSelected()) {
			orderingList = orderingList.stream().filter(x -> x.getCount().compareTo(BigDecimal.ZERO) != 0).collect(Collectors.toList());
		}
		return orderingList;
	}

	/**
	 * Метод для фильтрации только заказов на поставку с номерами заявок
	 *
	 * @param orderingList коллекция с исходными зазаказами
	 * @return отфильтрованная коллекция
	 */
	private List<Ordering> getOrderingsWithNumber(CheckBox onlyCreatedOrdering, List<Ordering> orderingList) {
		if (onlyCreatedOrdering.isSelected()) {
			orderingList = orderingList.stream().filter(x -> !x.getNumber().equals("")).collect(Collectors.toList());
		}
		return orderingList;
	}

	/**
	 * Метод для фильтрации только заказов на поставку без номеров заявок
	 *
	 * @param orderingList коллекция с исходными зазаказами
	 * @return отфильтрованная коллекция
	 */
	private List<Ordering> getOrderingsWithoutNumber(CheckBox onlyUncreatedOrdering, List<Ordering> orderingList) {
		if (onlyUncreatedOrdering.isSelected()) {
			orderingList = orderingList.stream().filter(x -> x.getNumber().equals("")).collect(Collectors.toList());
		}
		return orderingList;
	}

	@NotNull
	public List<Ordering> getOrderingsAfterFilter(List<Ordering> orderingsForCurrentYear, TextField search,
												  String period, ComboBox<String> category,
												  RadioButton onlyLeasing, RadioButton withoutLeasing,
												  CheckBox emptyCategory, CheckBox onlyCreatedOrdering,
												  CheckBox onlyUncreatedOrdering) {
		Predicate<Ordering> codeKSM = ordering ->
				ordering.getNomenclature().getCodeKSM().getId().toLowerCase().contains(search.getText().toLowerCase());
		Predicate<Ordering> codeKSMName = ordering ->
				ordering.getNomenclature().getCodeKSM().getName().toLowerCase().contains(search.getText().toLowerCase());
		Predicate<Ordering> number = ordering ->
				ordering.getNumber().toLowerCase().contains(search.getText().toLowerCase());
		Predicate<Ordering> technicalRequirement = ordering ->
				ordering.getNomenclature().getTechnicalRequirement().getId().toLowerCase().contains(search.getText().toLowerCase());

		List<Ordering> results = getOrderingsForCurrentPeriod(period, orderingsForCurrentYear);
		results = getOrderingsForCurrentCategory(category, results);
		results = getOnlyLeasingOrderings(onlyLeasing, results);
		results = getWithoutLeasingOrderings(withoutLeasing, results);
		results = getOrderingsWithZeroCount(emptyCategory, results);
		results = getOrderingsWithNumber(onlyCreatedOrdering, results);
		results = getOrderingsWithoutNumber(onlyUncreatedOrdering, results);
		results = results.stream().filter(number.or(codeKSM).or(codeKSMName).or(technicalRequirement)).collect(Collectors.toList());
		return results;
	}


}
