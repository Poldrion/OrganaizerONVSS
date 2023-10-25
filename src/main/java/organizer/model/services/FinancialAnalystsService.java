package organizer.model.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import organizer.model.OrganizerException;
import organizer.model.entities.FinancialAnalysts;
import organizer.model.repositories.FinancialAnalystsRepository;

import java.util.List;

import static organizer.utils.Constants.*;

@Service
public class FinancialAnalystsService {
	@Autowired
	private FinancialAnalystsRepository financialAnalystsRepository;

	public void save(FinancialAnalysts financialAnalysts){
		if (StringUtils.isEmpty(financialAnalysts.getId())) {
			throw new OrganizerException(CHECK_ID_FINANCIAL_ANALYSTS);
		}

		if (StringUtils.isEmpty(financialAnalysts.getName())) {
			throw new OrganizerException(CHECK_NAME_FINANCIAL_ANALYSTS);
		}

		if (StringUtils.isEmpty(financialAnalysts.getPosition())) {
			throw new OrganizerException(CHECK_POSITION_FINANCIAL_ANALYSTS);
		}

		if (StringUtils.isEmpty(financialAnalysts.getYearAFE())) {
			throw new OrganizerException(CHECK_YEAR_FINANCIAL_ANALYSTS);
		}

		financialAnalystsRepository.save(financialAnalysts);
	}

	public void deleteById(long id){
		financialAnalystsRepository.deleteById(id);
	}

	public List<FinancialAnalysts> findAll(){
		return financialAnalystsRepository.findAll();
	}

	public List<FinancialAnalysts> findByYear(int year){
		return financialAnalystsRepository.findByYear(year);
	}
}
