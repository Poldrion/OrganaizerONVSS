package organizer.model.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@NoArgsConstructor
@Table(name = "GroupMaterials")
public class GroupMaterials {
	@Id
	private Integer id;
	private String name;

	@Override
	public String toString() {
		return name;
	}
}
