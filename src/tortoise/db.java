package tortoise;

import java.sql.DriverManager;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import sun.reflect.annotation.AnnotationParser;

import java.lang.reflect.Method;

public class db {
	public static int insert(){
		Class<Table> obj = Table.class;
		if (obj.isAnnotationPresent(Table.class)) {

			Annotation annotation = obj.getAnnotation(Table.class);
			Table table = (Table) annotation;

			System.out.printf("%s", table.test());
		}
		return 1;
	}
	
	public static void main(String[] args) throws SQLException, ClassNotFoundException{
		PreparedStatement pstm = null;
		ResultSet rs = null;
		
		Class.forName("org.hsqldb.jdbcDriver");
		Connection c = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost", "SA", "");
		
		DEPARTMENT dep = new DEPARTMENT();
		dep.DEPT_ID = 60;
		dep.DEPT_NAME = "ASD";
		dep.DEPT_NO = "HASDFAS";
		dep.LOCATION = "ASDdf";
		//int r = tortoise.db.insert();		
		for (Annotation a:dep.getClass().getAnnotations()){
			System.out.printf("%n%s",a);
		}
		for (Field f:dep.getClass().getDeclaredFields()){
			for (Annotation a:f.getAnnotations()){
				System.out.printf("%n%s",a);
			}
			
		}
		/*
		Statement stm = c.createStatement();
		
		rs = stm.executeQuery("SELECT EMP_NAME FROM EMPLOYEE WHERE JOB='SALESMAN'");
		while (rs.next()) {
			System.out.println("EMPLOYEE Name:"
					+ rs.getString("EMP_NAME"));
		}
		*/
		
		/*
		String sql ="";
		sql+="CREATE TABLE Personas (";
		sql+="IdPersona int,";
		sql+="Apellidos varchar(255),";
		sql+="Nombres varchar(255),";
	    sql+="Direccion varchar(255),";
		sql+="Ciudad varchar(255)";
		sql+=");";
	
		pstm = c.prepareStatement(sql);
		int rtdo = pstm.executeUpdate();
		if (rtdo == 1){
			System.out.println("Tabla insertada");
		}
		else {
			System.out.println("Fallo al insertar tabla");
		}*/
	}
}
