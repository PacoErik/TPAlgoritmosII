package tortoise;

import java.sql.DriverManager;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.List;

import sun.reflect.annotation.AnnotationParser;

import java.lang.reflect.Method;

public class db {
	public static String getAnnotationValue(Annotation a){
		String value = "";
		if(a instanceof Table){
		    value = ((Table) a).name();
		}
		else if (a instanceof Id) {
			
		}
		else if (a instanceof Column) {
			value = ((Column) a).name();
		}
		return value;
	}
	public static Object getFieldValue(Object a){
		if (a instanceof String) {
			return "'"+a+"'";
		}
		return a;
	}
	public static int insert(Connection con, Object obj) throws IllegalArgumentException, IllegalAccessException, SQLException{
		/*for (Annotation a:obj.getClass().getDeclaredAnnotations()){
			System.out.printf("%n%s",getAnnotationValue(a));
		}
		for (Field f:obj.getClass().getDeclaredFields()){
			for (Annotation a:f.getAnnotations()){
				System.out.printf("%n%s %s",f.getName(),getAnnotationValue(a));
			}
			System.out.printf("%n%s", f.get(obj));
			
		}*/
		Statement st = con.createStatement();
		String tql = "INSERT INTO ";
		tql+=obj.getClass().getAnnotation(Table.class).name()+"(";
		for (Field f:obj.getClass().getDeclaredFields()){
			for (Annotation a:f.getAnnotations()){
				tql+=getAnnotationValue(a)+",";
			}			
		}
		tql = tql.substring(0,tql.length()-1)+") VALUES ("; //borrar la última coma "," y agregar un ") VALUES ("
		for (Field f:obj.getClass().getDeclaredFields()){
			tql+=getFieldValue(f.get(obj))+",";
		}
		tql = tql.substring(0,tql.length()-1)+")";
		int result = 0;
		try {
			result = st.executeUpdate(tql);
		}
		catch (SQLIntegrityConstraintViolationException ex){
			System.out.println("Dato duplicado (misma PK o mismo valor único), no se pudo añadir");
		}
		return result;
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
	
	public static void main(String[] args) throws SQLException, ClassNotFoundException, IllegalArgumentException, IllegalAccessException{
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
		dep.deptId = 72;
		dep.deptName = "Hola";
		dep.deptNo = "test";
		dep.location = "Hola";
		int r = tortoise.db.insert(c,dep);		
		System.out.println(r);
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
