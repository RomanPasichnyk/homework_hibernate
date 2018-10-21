package ua.logos;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;

import ua.logos.entity.City;
import ua.logos.entity.Country;
import ua.logos.entity.Person;

public class App {

	private static Scanner sc = new Scanner(System.in);
	private static BufferedReader reader = null;

	private static final String PATH = System.getProperty("user.dir");
	private static final String SEPARATOR = System.getProperty("file.separator");

	public static void main(String[] args) throws IOException {

		EntityManagerFactory factory = Persistence.createEntityManagerFactory("homework_hibernate");
		EntityManager em = factory.createEntityManager();
		em.getTransaction().begin();

//		initDB(em);

		String answer = "";

		while (!answer.equals("Exit")) {
			System.out.println("\t\t...:::< Menu >:::...\n");
			System.out.println("AddCountry \t\t| AddCity \t\t| AddPerson");
			System.out.println("ShowCountry \t\t| ShowCity \t\t| ShowPerson");
			System.out.println("ShowCountryID \t\t| ShowCityID \t\t| ShowPersonID");
			System.out.println("ShowPersonCity \t\t| ShowCityCountry \t| Exit");
			System.out.println("ShowPersonForName \t| EditPerson");
			answer = sc.next();
			switch (answer) {
			case "AddCountry": {
				addCountry(em);
				em.getTransaction().commit();
				em.getTransaction().begin();
				break;
			}
			case "AddCity": {
				addCity(em);
				em.getTransaction().commit();
				em.getTransaction().begin();
				break;
			}
			case "AddPerson": {
				addPerson(em);
				em.getTransaction().commit();
				em.getTransaction().begin();
				break;
			}
			case "ShowCountry": {
				em.createQuery("SELECT c FROM Country c", Country.class).getResultList().forEach(System.out::println);
				break;
			}
			case "ShowCity": {
				em.createQuery("SELECT c FROM City c", City.class).getResultList().forEach(System.out::println);
				break;
			}
			case "ShowPerson": {
				em.createQuery("SELECT p FROM Person p", Person.class).getResultList().forEach(System.out::println);
				break;
			}
			case "ShowCountryID": {
				showCountryID(em);
				break;
			}
			case "ShowCityID": {
				showCityID(em);
				break;
			}
			case "ShowPersonID": {
				showPersonID(em);
				break;
			}
			case "ShowPersonCity": {
				showPersonCity(em);
				break;
			}
			case "ShowCityCountry": {
				showCityCountry(em);
				break;
			}
			case "ShowPersonForName": {
				showPersonForName(em);
				break;
			}
			case "EditPerson": {
				editPerson(em);
				em.getTransaction().commit();
				em.getTransaction().begin();
				break;
			}

			}
		}

		em.getTransaction().commit();
		em.close();
		factory.close();

	}

	private static List<String> readFileCountry(String fileName) {
		List<String> countries = new ArrayList<>();
		reader = null;

		try {
			reader = new BufferedReader(new FileReader(PATH + SEPARATOR + fileName + ".txt"));
			while (true) {
				String line = reader.readLine();
				if (line != null)
					countries.add(line);
				else
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return countries;
	}

	private static List<String> readFileCity(String fileName) {
		List<String> cities = new ArrayList<>();
		reader = null;

		try {
			reader = new BufferedReader(new FileReader(PATH + SEPARATOR + fileName + ".txt"));
			while (true) {
				String line = reader.readLine();
				if (line != null)
					cities.add(line);
				else
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cities;
	}

	private static List<String> readFilePerson(String fileName) {
		List<String> person = new ArrayList<>();
		reader = null;

		try {
			reader = new BufferedReader(new FileReader(PATH + SEPARATOR + fileName + ".txt"));
			while (true) {
				String line = reader.readLine();
				if (line != null)
					person.add(line);
				else
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return person;
	}

	private static void initDB(EntityManager em) {
		Random rd = new Random();

		readFileCountry("country").forEach(c -> {
			Country country = Country.builder().name(c).build();
			em.persist(country);
		});

		readFileCity("city").forEach(c -> {
			int country_id = rd.nextInt(20) + 1;
			Country country = em.createQuery("SELECT c FROM Country c WHERE c.id = ?1", Country.class)
					.setParameter(1, (long) country_id).getSingleResult();
			City city = City.builder().name(c).country(country).build();
			em.persist(city);
		});

		readFilePerson("person").forEach(c -> {
			int city_id = rd.nextInt(39) + 1;
			City city = em.createQuery("SELECT c FROM City c WHERE c.id = ?1", City.class)
					.setParameter(1, (long) city_id).getSingleResult();
			String[] infPerson = c.split(" ");
			infPerson = c.split(" ");
			Person person = Person.builder().firstName(infPerson[0]).lastName(infPerson[1])
					.age(Integer.valueOf(infPerson[2])).city(city).build();
			em.persist(person);
		});
	}

	private static void addCountry(EntityManager em) throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter the name of the country: ");
		String nameCountry = reader.readLine();
		Country country = Country.builder().name(nameCountry).build();
		em.persist(country);
		System.out.println("Country succesfully added!");
	}

	private static void addCity(EntityManager em) throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter the name of the country: ");
		String nameCountry = reader.readLine();

		try {
			Country country = em.createQuery("SELECT c FROM Country c WHERE c.name = ?1", Country.class)
					.setParameter(1, nameCountry).getSingleResult();
			System.out.println("Enter the name of the city: ");
			String nameCity = reader.readLine();
			City city = City.builder().name(nameCity).country(country).build();
			em.persist(city);
			System.out.println("City succesfully added!");
		} catch (NoResultException e) {
			System.out.println("City not found!");
		}
	}

	private static void addPerson(EntityManager em) throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter first name of the person: ");
		String firstName = reader.readLine();
		System.out.println("Enter last name of the person: ");
		String lastName = reader.readLine();
		System.out.println("Enter age of the person: ");
		int age = -1;
		age = sc.nextInt();
		while (age < 0 || age > 150) {
			System.out.println("Age is incorrect. Enter please correct age of the person!");
			age = sc.nextInt();
		}
		System.out.println("Enter the city of the person");
		String cityName = reader.readLine();
		try {
			City city = em.createQuery("SELECT c FROM City c WHERE c.name = ?1", City.class).setParameter(1, cityName)
					.getSingleResult();
			Person person = Person.builder().firstName(firstName).lastName(lastName).age(age).city(city).build();
			em.persist(person);
			System.out.println("Person succesfully added!");
		} catch (NoResultException e) {
			System.out.println("City not found!");
		}
	}

	private static void showPersonCity(EntityManager em) throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter name of the city: ");
		String cityName = reader.readLine();
		try {
			City city = em.createQuery("SELECT c FROM City c WHERE c.name = ?1", City.class).setParameter(1, cityName)
					.getSingleResult();
			em.createQuery("SELECT p FROM Person p INNER JOIN p.city c WHERE c.id = ?1", Person.class)
					.setParameter(1, city.getId()).getResultList().forEach(System.out::println);
		} catch (NoResultException e) {
			System.out.println("City not found!");
		}
	}

	private static void showCityCountry(EntityManager em) throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter name of the country: ");
		String countryName = reader.readLine();
		try {
			Country country = em.createQuery("SELECT c FROM Country c WHERE c.name = ?1", Country.class)
					.setParameter(1, countryName).getSingleResult();
			em.createQuery("SELECT c FROM City c INNER JOIN c.country cc WHERE cc.id = ?1", City.class)
					.setParameter(1, country.getId()).getResultList().forEach(System.out::println);
		} catch (NoResultException e) {
			System.out.println("Country not found!");
		}
	}

	private static void showCountryID(EntityManager em) throws IOException {
		System.out.println("Enter id of the country");
		Long country_id = sc.nextLong();
		try {
			System.out.println(em.createQuery("SELECT c FROM Country c WHERE c.id = ?1", Country.class)
					.setParameter(1, country_id).getSingleResult());
		} catch (NoResultException e) {
			System.out.println("Country not found!");
		}
	}

	private static void showCityID(EntityManager em) throws IOException {
		System.out.println("Enter id of the city");
		Long city_id = sc.nextLong();
		try {
			System.out.println(em.createQuery("SELECT c FROM City c WHERE c.id = ?1", City.class)
					.setParameter(1, city_id).getSingleResult());
		} catch (NoResultException e) {
			System.out.println("City not found!");
		}
	}

	private static void showPersonID(EntityManager em) throws IOException {
		System.out.println("Enter id of the person");
		Long person_id = sc.nextLong();
		try {
			System.out.println(em.createQuery("SELECT p FROM Person p WHERE p.id = ?1", Person.class)
					.setParameter(1, person_id).getSingleResult());
		} catch (NoResultException e) {
			System.out.println("Person not found!");
		}
	}

	private static void showPersonForName(EntityManager em) throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter name: ");
		String name = reader.readLine();
		try {
			List<Person> person = em.createQuery("SELECT p FROM Person p WHERE p.firstName LIKE ?1", Person.class)
					.setParameter(1, "%" + name + "%").getResultList();
			person.forEach(System.out::println);
		} catch (NoResultException e) {
			System.out.println("Person not found!");
		}
	}

	private static void editPerson(EntityManager em) throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter person ID");
		long id = sc.nextLong();
		Person person = em.createQuery("SELECT p FROM Person p WHERE p.id = ?1", Person.class).setParameter(1, id)
				.getSingleResult();
		System.out.println("ID: " + person.getId() + "\t | " + "FirstName: " + person.getFirstName() + "\t | "
				+ "LastName: " + person.getLastName() + "\t | " + "Age: " + person.getAge() + "\t | " + "City: "
				+ person.getCity().getName());
		System.out.println("What do you want to change?");
		String answer = sc.next();
		switch (answer) {
		case "FirstName": {
			System.out.println("Enter first name");
			String firstName = reader.readLine();
			em.createQuery("UPDATE Person p SET firstName = ?1 WHERE p.id = ?2").setParameter(1, firstName)
					.setParameter(2, id).executeUpdate();
			System.out.println("Person successfully edited!");
			break;
		}
		case "LastName": {
			System.out.println("Enter last name");
			String lastName = reader.readLine();
			em.createQuery("UPDATE Person p SET lastName = ?1 WHERE p.id = ?2").setParameter(1, lastName)
					.setParameter(2, id).executeUpdate();
			System.out.println("Person successfully edited!");
			break;
		}
		case "Age": {
			System.out.println("Enter age");
			int age = sc.nextInt();
			em.createQuery("UPDATE Person p SET age = ?1 WHERE p.id = ?2").setParameter(1, age).setParameter(2, id)
					.executeUpdate();
			System.out.println("Person successfully edited!");
			break;
		}
		case "City": {
			System.out.println("Enter city");
			String cityName = reader.readLine();
			try {
				City city = em.createQuery("SELECT c FROM City c WHERE c.name = ?1", City.class)
						.setParameter(1, cityName).getSingleResult();
				em.createQuery("UPDATE Person p SET city = ?1 WHERE p.id = ?2").setParameter(1, city).setParameter(2, id).executeUpdate();
				System.out.println("Person successfully edited!");
			} catch (NoResultException e) {
				System.out.println("City not found!");
			}
			break;
		}
		}
	}

}
