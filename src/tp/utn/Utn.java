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
	private static <T> String getColumns(Class<T> dtoClass, String alias, Join join)
	{
		String q="";
		for(Field f:dtoClass.getDeclaredFields())
		{
			if(f.isAnnotationPresent(Column.class))
			{
				Column c=f.getAnnotation(Column.class);
				q+=alias+"."+c.name()+",";

				if(f.isAnnotationPresent(Id.class))
				{
					Id i=f.getAnnotation(Id.class);
					join.setField(alias+"."+c.name());
				}

				if(c.fetchType()==Column.EAGER) q+=getJoin((Class)f.getGenericType(),alias+"."+c.name(),join);
			}
		}
		return q;
	}

	private static <T> String getJoin(Class<T> dtoClass, String field, Join join)
	{
		String ret="";

		if(dtoClass.isAnnotationPresent(Table.class))
		{
			Table a=dtoClass.getAnnotation(Table.class);

			String alias=a.name();

			if(!a.alias().isEmpty()) alias=a.alias();

			join.setS(join.getS()+" INNER JOIN "+a.name());

			if(!a.alias().isEmpty()) join.setS(join.getS()+" AS "+a.alias());
			else join.setS(join.getS()+" AS "+a.name());

			join.setS(join.getS()+" ON "+field+"=");

			Join otros_joins=new Join();

			ret=getColumns(dtoClass,alias,otros_joins);

			join.setS(join.getS()+otros_joins.getField()+otros_joins.getS());
		}

		return ret;
	}
	
	
	
	//Retorna la representación del comando XQL 
	public static String parseXQL(Class<?> dtoClass,String xql, Object ... args)
	{
		xql = xql.replace("$","WHERE ");
		for(Field f:dtoClass.getDeclaredFields())
		{
			if(f.isAnnotationPresent(Column.class))
			{
				Table a=dtoClass.getAnnotation(Table.class);
				String alias=a.name();
				Column c=f.getAnnotation(Column.class);
				xql = xql.replace(f.getName(),alias+"."+c.name());
			}
		}
		for (Object arg:args) {
			xql = xql.replaceFirst("\\?",arg.toString());
		}
		
		return xql;
	}
	
	// Retorna: el SQL correspondiente a la clase dtoClass acotado por xql
	public static <T> String _query(Class<T> dtoClass, String xql)
	{
		String q="";

		if(dtoClass.isAnnotationPresent(Table.class))
		{
			Table a=dtoClass.getAnnotation(Table.class);

			String alias=a.name();

			if(!a.alias().isEmpty()) alias=a.alias();

			Join joins=new Join();

			q="SELECT "+getColumns(dtoClass,alias,joins);
			q=q.replaceFirst(",$","");
			q+=" FROM "+a.name();
			q+=" AS "+alias;
			q+=joins.getS();

			if(!xql.isEmpty()) q+=" "+xql;
		}

		return q;
	}

	// Invoca a: _query para obtener el SQL que se debe ejecutar
	// Retorna: una lista de objetos de tipo T
	public static <T> List<T> query(Connection con, Class<T> dtoClass, String xql, Object... args)
	{
		//Acá el query llama al parseXQL y se lo manda 
		//todo (menos la conexión)
		//y luego le pasa el XQL parseado al _query 
		//para obtener el comando SQL a ejecutar
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
	// Que hace?: actualiza todos los campos de la fila identificada por el id
	// de dto
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

	// Retorna la cantidad de filas afectadas al eliminar la fila identificada
	// por id
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
	public static int insert(Connection con, Object dto) throws IllegalArgumentException,IllegalAccessException,SQLException
	{
		return 0;
	}
}
