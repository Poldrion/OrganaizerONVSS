package organizer.model.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Data
@Entity
@NoArgsConstructor
@Table(name = "FinancialAnalysts")
public class FinancialAnalysts {
	@Id
	private long id;
	private String name;
	private int position;
	private int yearAFE;
	private String codeSPPElement;
	private String codeInvestmentProject;

}
