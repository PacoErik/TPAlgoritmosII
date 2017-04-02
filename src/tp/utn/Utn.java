package tp.utn;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import tp.utn.ann.Column;
import tp.utn.ann.Id;
import tp.utn.ann.Table;

public class Utn
{
	private static String getAnnotationValue(Annotation a){
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
	
	private static Object getFieldValue(Object a){
		if (a instanceof String) {
			return "'"+a+"'";
		}
		return a;
	}

	private static <T> List<T> parseResultSet(ResultSet rs, Class<T> class_type)
			throws SQLException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
	{
		List<T> ret=new ArrayList<T>();

		while(rs.next())
		{
			T obj=class_type.getConstructor().newInstance();

			for(Field f:class_type.getFields())
			{
				if(f.isAnnotationPresent(Column.class))
				{
					Column c=f.getAnnotation(Column.class);
					f.set(obj,rs.getObject(c.name()));
				}
			}

			ret.add(obj);
		}
		return ret;
	}
	
	private static <T> String getColumns(Class<T> class_type, Boolean includeIdentity)
	{
		String ret="";
		for(Field f:class_type.getFields())
		{
			if(f.isAnnotationPresent(Id.class))
			{
				Id i=f.getAnnotation(Id.class);

				if(!includeIdentity && (i.strategy()==Id.IDENTITY)) continue;
			}

			if(f.isAnnotationPresent(Column.class))
			{
				Column c=f.getAnnotation(Column.class);
				ret=c.name()+",";
			}
		}
		
		ret.replaceFirst(".$","");

		return ret;
	}
	
	// Retorna: el SQL correspondiente a la clase dtoClass acotado por xql
	private static <T> String _query(Class<T> dtoClass, String xql)
	{
		String q = "";

		if(dtoClass.isAnnotationPresent(Table.class))
		{
			q = "SELECT " + getColumns(dtoClass,false);

			Table a = dtoClass.getAnnotation(Table.class);
			q = q + " FROM " + a.name();
		}

		return q;
	}
	
	// Invoca a: _query para obtener el SQL que se debe ejecutar
	// Retorna: una lista de objetos de tipo T
	public static <T> List<T> query(Connection con, Class<T> dtoClass, String xql, Object... args)
	{
		return null;
	}
	
	// Retorna: una fila identificada por id o null si no existe
	// Invoca a: query
	private static <T> T find(Connection con, Class<T> dtoClass, Object id)
	{
		return null;
	}
	
	// Retorna: una todasa las filas de la tabla representada por dtoClass
	// Invoca a: query
	private static <T> List<T> findAll(Connection con, Class<T> dtoClass)
	{
		return null;
	}

	// Retorna: el SQL correspondiente a la clase dtoClass acotado por xql
	public static <T> String _update(Class<T> dtoClass, String xql)
	{
		return null;
	}
	
	// Invoca a: _update para obtener el SQL que se debe ejecutar
	// Retorna: la cantidad de filas afectadas luego de ejecutar el SQL
	public static int update(Connection con, Class<?> dtoClass, String xql, Object... args)
	{
		return 0;
	}

	// Invoca a: update 
	// Que hace?: actualiza todos los campos de la fila identificada por el id de dto
	// Retorna: Cuantas filas resultaron modificadas (deberia: ser 1 o 0) 
	public static int update(Connection con, Object dto)
	{
		return 0;
	}
	
	// Retorna: el SQL correspondiente a la clase dtoClass acotado por xql
	public static String _delete(Class<?> dtoClass, String xql)
	{
		return null;
	}
	
	// Invoca a: _delete para obtener el SQL que se debe ejecutar
	// Retorna: la cantidad de filas afectadas luego de ejecutar el SQL
	public static int delete(Connection con, Class<?> dtoClass, String xql, Object... args)
	{
		return 0;
	}

	// Retorna la cantidad de filas afectadas al eliminar la fila identificada por id
    // (deberia ser: 1 o 0)
	// Invoca a: delete
	public static int delete(Connection con, Class<?> dtoClass, Object id)
	{
		return 0;
	}

	// Retorna: el SQL correspondiente a la clase dtoClass
	public static String _insert(Class<?> dtoClass)
	{
		return null;
	}
	
	// Invoca a: _insert para obtener el SQL que se debe ejecutar
	// Retorna: la cantidad de filas afectadas luego de ejecutar el SQL
	public static int insert(Connection con, Object dto) throws IllegalArgumentException, IllegalAccessException, SQLException
	{
		Statement st = con.createStatement();
		String tql = "INSERT INTO ";
		tql+=dto.getClass().getAnnotation(Table.class).name()+"(";
		for (Field f:dto.getClass().getDeclaredFields()){
			for (Annotation a:f.getAnnotations()){
				tql+=getAnnotationValue(a)+",";
			}			
		}
		tql = tql.replaceFirst(".$",") VALUES ("); //borrar la última coma y reemplazarlo por ") VALUES ("
		for (Field f:dto.getClass().getDeclaredFields()){
			tql+=getFieldValue(f.get(dto))+",";
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
}
