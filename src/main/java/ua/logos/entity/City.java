package ua.logos.entity;

import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(callSuper = true, exclude = {"person", "country"})

@Entity
@Table(name = "city")
public class City extends BaseEntity {

	@Column(unique = true, nullable = false, length = 40)
	private String name;

	@ManyToOne
	@JoinColumn(name = "country_id")
	private Country country;

	@OneToMany(mappedBy = "city")
	private List<Person> person;

}
