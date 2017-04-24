package tp.utn;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import tp.utn.ann.Column;
import tp.utn.ann.Id;
import tp.utn.ann.Relation;
import tp.utn.ann.Table;
import tp.utn.domain.Mapa;
import tp.utn.domain.Campo;
import tp.utn.excepciones.ParametroIncorrectoExcepcion;

public class Utn
{
	private static Map<String, Mapa> dominio = new HashMap<String, Mapa>();

	private static <T> Mapa getTabla(Class<T> dtoClass)
    {
    	String nombreClase = dtoClass.getSimpleName();

        if (dominio.containsKey(nombreClase))
            return dominio.get(nombreClase);

        if(dtoClass.isAnnotationPresent(Table.class)) {
            Table a = dtoClass.getAnnotation(Table.class);
            String alias = "_" + a.name();
            if(!a.alias().isEmpty()) alias = a.alias();

            Mapa m = new Mapa(nombreClase, a.name(), alias);

            dominio.put(nombreClase, m);

            getColumns(dtoClass, m);

            return m;
        }

        return null;
    }

    private static <T> void getColumns(Class<T> dtoClass, Mapa m)
    {
        for(Field f:dtoClass.getDeclaredFields()) {
            if(f.isAnnotationPresent(Column.class)) {
                Column c = f.getAnnotation(Column.class);

				if (f.isAnnotationPresent(Id.class)) {
					Id i = f.getAnnotation(Id.class);
					m.addIndice(f.getName(), c.name(), i.strategy());
				}
				else {
					Campo campo = m.addCampo(f.getName(), c.name());
					Mapa tablaJoin = getTabla((Class) f.getGenericType());
					if (tablaJoin != null) campo.setTablaJoin(tablaJoin);
				}
            }

            if(f.isAnnotationPresent(Relation.class)) {
				Relation r = f.getAnnotation(Relation.class);
            	Mapa relacion = getTabla(r.type());
            	m.addRelacion(relacion, Column.LAZY);
			}
        }
    }

	private static <T> List<T> parseResultSet(ResultSet rs, Class<T> dtoClass)
			throws SQLException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InvocationTargetException {
		List<T> ret=new ArrayList<T>();

		while(rs.next())
		{
			T obj=dtoClass.getConstructor().newInstance();

			for(Field f:dtoClass.getFields())
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

	// Retorna: el SQL correspondiente a la clase dtoClass acotado por xql
	public static <T> String _query(Class<T> dtoClass, String xql) throws ParametroIncorrectoExcepcion {
		String q="";
		Mapa m = getTabla(dtoClass);
		if (m != null)  q = m.getSelect(xql);
		return q;
	}

	// Invoca a: _query para obtener el SQL que se debe ejecutar
	// Retorna: una lista de objetos de tipo T
	public static <T> List<T> query(Connection con, Class<T> dtoClass, String xql, Object... args)
	{
		//Ac� el query llama al parseXQL y se lo manda 
		//todo (menos la conexi�n)
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
