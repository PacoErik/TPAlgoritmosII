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
		
		Connection c = UtnConnectionFactory.getConnection();
		
		Dept dep = new Dept();
		dep.setDeptno(74);
		dep.setDname("Hola");
		dep.setLoc("Hola");
		int r = Utn.insert(c,dep);		
		System.out.println(r);

	}
}
