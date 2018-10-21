package ua.logos.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(callSuper = true, exclude = "city")

@Entity
@Table(name = "country")
public class Country extends BaseEntity {

	@Column(length = 40, unique = true, nullable = false)
	private String name;

	@OneToMany(mappedBy = "country")
	private List<City> city;

}
