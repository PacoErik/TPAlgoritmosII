package demo;

import utn.Utn;
import utn.UtnConnectionFactory;
import demo.domain.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

public class Main
{

	public static void main(String[] args) throws IOException, SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException {

		System.out.println("/*************** TRUNCATE ***************/");
		Utn.command(UtnConnectionFactory.getConnection(), "TRUNCATE SCHEMA PUBLIC RESTART IDENTITY AND COMMIT NO CHECK");

		System.out.println("/*************** INSERT   ***************/");
		Utn.insert(UtnConnectionFactory.getConnection(), TipoOcupacion.create("Profesional"));
		Utn.insert(UtnConnectionFactory.getConnection(), TipoOcupacion.create("Estudiante"));

		Utn.insert(UtnConnectionFactory.getConnection(), Ocupacion.create("Ingeniero",
				Utn.find(UtnConnectionFactory.getConnection(), TipoOcupacion.class, 0)));

		Utn.insert(UtnConnectionFactory.getConnection(), Ocupacion.create("Estudiante",
				Utn.find(UtnConnectionFactory.getConnection(), TipoOcupacion.class, 1)));

		Utn.insert(UtnConnectionFactory.getConnection(), Direccion.create("Mozart", 3900));

		Utn.insert(UtnConnectionFactory.getConnection(), Persona.create("Pepe",
				Utn.find(UtnConnectionFactory.getConnection(), Direccion.class, 0),
				Utn.find(UtnConnectionFactory.getConnection(), Ocupacion.class, 0)));

		Utn.insert(UtnConnectionFactory.getConnection(), Persona.create("Maria",
				Utn.find(UtnConnectionFactory.getConnection(), Direccion.class, 0),
				Utn.find(UtnConnectionFactory.getConnection(), Ocupacion.class, 0)));

		Utn.insert(UtnConnectionFactory.getConnection(), Persona.create("Joaquin",
				Utn.find(UtnConnectionFactory.getConnection(), Direccion.class, 0),
				Utn.find(UtnConnectionFactory.getConnection(), Ocupacion.class, 1)));

		Utn.insert(UtnConnectionFactory.getConnection(), Persona.create("Lucas",
				Utn.find(UtnConnectionFactory.getConnection(), Direccion.class, 0),
				Utn.find(UtnConnectionFactory.getConnection(), Ocupacion.class, 1)));

		System.out.println("/*************** QUERY    ***************/");

		System.out.println(Utn.query(UtnConnectionFactory.getConnection(), Persona.class, "$direccion.calle = ? AND $ocupacion.tipoocupacion.descripcion = ?", "Mozart", "Profesional"));

		System.out.println("/*************** FIND     ***************/");
		Direccion d = Utn.find(UtnConnectionFactory.getConnection(), Direccion.class, 0);
		d.setNumero(25);

		System.out.println("/*************** UPDATE   ***************/");
		Utn.update(UtnConnectionFactory.getConnection(), d);

		System.out.println(d);

		System.out.println("/*************** LAZY     ***************/");
		System.out.println(d.getPersonas().size());

		System.out.println("/*************** DELETE   ***************/");
		Utn.delete(UtnConnectionFactory.getConnection(), Persona.class, 3);

		d = Utn.find(UtnConnectionFactory.getConnection(), Direccion.class, 0);
		System.out.println(d.getPersonas().size());

		System.out.println("/*************** DELETE  WHERE **********/");

		Utn.delete(UtnConnectionFactory.getConnection(), Persona.class, "$nombre = ?", "Pepe");

		d = Utn.find(UtnConnectionFactory.getConnection(), Direccion.class, 0);
		System.out.println(d.getPersonas().size());

		System.out.println("/*************** UPDATE  WHERE **********/");

		Utn.update(UtnConnectionFactory.getConnection(), Direccion.class, "SET $numero = ? WHERE $calle = ?", 3, "Mozart");
		System.out.println(Utn.find(UtnConnectionFactory.getConnection(), Direccion.class, 0));
	}
}
