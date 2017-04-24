package demo;

import java.io.IOException;
import java.sql.SQLException;

import tp.utn.Utn;

public class Main
{

	public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException, SQLException, IOException
	{
		/* Esto se reemplaza con el UtnConnectionFactory
		Connection c = null;
	
		Class.forName("org.hsqldb.jdbcDriver");
		
		try {
			c = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost", "SA", "");
		} catch (Exception e) {
			System.out.println("No se pudo conectar a la base de datos");
			System.exit(0);
		}
		*/

		System.out.println(Utn._query(TipoOcupacion.class, ""));
		System.out.println(Utn._query(Ocupacion.class, ""));
		System.out.println(Utn._query(Persona.class, "$direccion.calle = 'pep' AND $ocupacion.tipoocupacion.descripcion = '666'"));
		System.out.println(Utn._query(Direccion.class, "$calle = 'pep'"));
		System.out.println(Utn._query(Persona.class, "$calle = 'pep' AND $ocupacion.tipo_ocupacion.descripcion = '666'"));
	}
}
