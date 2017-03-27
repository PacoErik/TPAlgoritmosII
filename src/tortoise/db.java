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
import java.util.List;

import sun.reflect.annotation.AnnotationParser;

import java.lang.reflect.Method;

public class db {
	public static int insert(Connection con, Department obj){
		for (Annotation a:obj.getClass().getAnnotations()){
			System.out.printf("%n%s",a);
		}
		for (Field f:obj.getClass().getDeclaredFields()){
			for (Annotation a:f.getAnnotations()){
				System.out.printf("%n%s %s",f,a);
			}
			
		}
		return 1;
	}
	public static Class<?> find(Connection con,Class<?> obj,Class<?> index){
		return null;
	}
	public static List<?> query(Connection con,Class<?> obj,String tql,Object ... args){
		return null;
	}
	public static int update(Connection con, Class<?> obj,String tql,Object ... args){
		return 1;
	}
	public static int delete(Connection con, Class<?> obj,String tql,Object ... args){
		return 1;
	}
	
	
	public static void main(String[] args) throws SQLException, ClassNotFoundException{
		PreparedStatement pstm = null;
		ResultSet rs = null;
		Connection c = null;
		
		Class.forName("org.hsqldb.jdbcDriver");
		
		try {
			c = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost", "SA", "");
		} catch (Exception e) {
			System.out.println("No se pudo conectar a la base de datos");
			System.exit(0);
		}
		
		Department dep = new Department();
		dep.deptId = 60;
		dep.deptName = "ASD";
		dep.deptNo = "HASDFAS";
		dep.location = "ASDdf";
		int r = tortoise.db.insert(c,dep);		
		
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
