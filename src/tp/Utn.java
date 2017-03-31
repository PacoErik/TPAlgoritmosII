package tp;

import java.sql.DriverManager;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.List;

public class Utn {
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
		Statement st = con.createStatement();
		String tql = "INSERT INTO ";
		tql+=obj.getClass().getAnnotation(Table.class).name()+"(";
		for (Field f:obj.getClass().getDeclaredFields()){
			for (Annotation a:f.getAnnotations()){
				tql+=getAnnotationValue(a)+",";
			}			
		}
		tql = tql.replaceFirst(".$",") VALUES ("); //borrar la última coma y reemplazarlo por ") VALUES ("
		for (Field f:obj.getClass().getDeclaredFields()){
			tql+=getFieldValue(f.get(obj))+",";
		}
		tql = tql.replaceFirst(".$", ")");
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
		Connection c = null;
		
		Class.forName("org.hsqldb.jdbcDriver");
		
		try {
			c = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost", "SA", "");
		} catch (Exception e) {
			System.out.println("No se pudo conectar a la base de datos");
			System.exit(0);
		}
		
		Department dep = new Department();
		dep.deptId = 75;
		dep.deptName = "Hola";
		dep.deptNo = "gg";
		dep.location = "Hola";
		int r = tp.Utn.insert(c,dep);		
		System.out.println(r);
	}
}
