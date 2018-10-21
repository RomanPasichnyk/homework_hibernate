package ua.logos.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "person")
public class Person extends BaseEntity {

	@Column(name = "first_name", length = 40, nullable = false)
	private String firstName;

	@Column(name = "last_name", length = 40, nullable = false)
	private String lastName;

	@Column(name = "age", nullable = false)
	private int age;

	@ManyToOne
	@JoinColumn(name = "city_id")
	private City city;

}
