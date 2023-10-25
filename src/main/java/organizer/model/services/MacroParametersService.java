package organizer.model.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import organizer.model.OrganizerException;
import organizer.model.entities.MacroParameters;
import organizer.model.repositories.MacroParametersRepository;

import java.util.List;

import static organizer.utils.Constants.*;

@Service
public class MacroParametersService {
	@Autowired
	private MacroParametersRepository macroParametersRepository;


	public void save(MacroParameters macroParameters) {
		if (macroParameters.getTypeMacroParameters() == null) {
			throw new OrganizerException(CHECK_TYPE_MACRO_PARAMETER);
		}
		if (StringUtils.isEmpty(macroParameters.getFirstYear())) {
			throw new OrganizerException(CHECK_FIRST_YEAR_MACRO_PARAMETER);
		}
		if (StringUtils.isEmpty(macroParameters.getFirstYearValue()) ||
			StringUtils.isEmpty(macroParameters.getSecondYearValue()) ||
			StringUtils.isEmpty(macroParameters.getThirdYearValue()) ||
			StringUtils.isEmpty(macroParameters.getForthYearValue()) ||
			StringUtils.isEmpty(macroParameters.getFifthYearValue())) {
			throw new OrganizerException(CHECK_YEAR_VALUE_MACRO_PARAMETER);
		}

		if (macroParameters.getDate() == null) {
			throw new OrganizerException(CHECK_DATE_MACRO_PARAMETER);
		}

		macroParametersRepository.save(macroParameters);
	}

	public void deleteById(int id) {
		macroParametersRepository.deleteById(id);
	}

	public List<MacroParameters> findAll() {
		return macroParametersRepository.findAll();
	}

	public List<MacroParameters> findByFirstYear(int firstYear) {
		return macroParametersRepository.findByFirstYear(firstYear);
	}

}
