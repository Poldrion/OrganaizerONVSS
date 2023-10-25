package organizer.model.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@Table(name = "MacroParameters")
public class MacroParameters {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private TypeMacroParameters typeMacroParameters;
	private int firstYear;
	private LocalDate date;
	@Column(precision = 4, scale = 3)
	private BigDecimal firstYearValue;
	@Column(precision = 4, scale = 3)
	private BigDecimal secondYearValue;
	@Column(precision = 4, scale = 3)
	private BigDecimal thirdYearValue;
	@Column(precision = 4, scale = 3)
	private BigDecimal forthYearValue;
	@Column(precision = 4, scale = 3)
	private BigDecimal fifthYearValue;

	@Override
	public String toString() {
		return typeMacroParameters.getTitle();
	}

	public enum TypeMacroParameters {
		EQUIPMENT("Оборудование"),
		MATERIALS("Материалы");

		private String title;

		TypeMacroParameters(String title) {
			this.title = title;
		}

		public String getTitle() {
			return title;
		}

		@Override
		public String toString() {
			return title;
		}
	}
}
