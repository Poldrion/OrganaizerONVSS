package organizer.model.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@NoArgsConstructor
@Table(name = "OrderingResult")
public class OrderingResult {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String number;
	private String position;
	private BigDecimal count;
	private BigDecimal cost;
	private BigDecimal price;
	@OneToOne
	@JoinColumn(name = "ordering_id")
	private Ordering ordering;


}
