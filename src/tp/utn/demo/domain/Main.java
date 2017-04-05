package tp.utn.demo.domain;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import tp.utn.Utn;
import tp.utn.UtnConnectionFactory;

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
		
//		Connection c = UtnConnectionFactory.getConnection();
//		
//		Direccion dir = new Direccion();
//		dir.setCalle("Calle1");
//		dir.setNumero(1);
//		int r = Utn.insert(c,dir);		
//		System.out.println(r);
		
		System.out.println(Utn._query(Direccion.class, ""));
		System.out.println(Utn._query(Persona.class, ""));
		System.out.println(Utn._query(TipoOcupacion.class, ""));
		System.out.println(Utn._query(Ocupacion.class, ""));

	}
}
