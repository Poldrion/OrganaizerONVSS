package organizer.model.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import organizer.model.entities.Ordering;
import organizer.model.entities.OrderingResult;
import organizer.model.repositories.OrderingResultRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderingResultService {

	@Autowired
	private OrderingResultRepository orderingResultRepository;

	//TODO реализовать функционал сервиса

	public OrderingResult findById(long id) {
		return orderingResultRepository.findById(id).get();
	}

	public OrderingResult findByNumberAndPosition(String number, String position) {
		return orderingResultRepository.findByNumberAndPosition(number, position);
	}

	public List<OrderingResult> findAll() {
		return orderingResultRepository.findAll();
	}

	public OrderingResult findByOrdering(Ordering ordering) {
		return orderingResultRepository.findByOrdering(ordering);
	}

	public List<OrderingResult> findAllByOrdering(List<Ordering> orderings) {
		List<OrderingResult> temp = new ArrayList<>();
		for (Ordering ordering : orderings) {
			OrderingResult orderingResult = orderingResultRepository.findByOrdering(ordering);
			if (orderingResult != null) {
				temp.add(orderingResult);
			}
		}
		return temp;
	}

	public void save(OrderingResult orderingResult) {
		orderingResultRepository.save(orderingResult);
	}

	public void deleteById(long id) {
		orderingResultRepository.deleteById(id);
//		Dialog.DialogBuilder.builder().title(COMPLETED_SUCCESSFULLY).message(REMOVE_ORDERING).build().show();
	}


}
