package utn;

import utn.domain.ClassField;
import utn.domain.MappedClass;
import utn.ann.Column;
import utn.ann.Id;
import utn.ann.Relation;
import utn.ann.Table;
import utn.exceptions.WrongParameterException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utn
{
	private static Map<String, MappedClass> domain = new HashMap<String, MappedClass>();

	private static void logQuery(String q) {
		System.out.println(q);
	}

	private static <T> MappedClass getMappedClass(Class<T> dtoClass) {
    	String className = dtoClass.getSimpleName();

        if (domain.containsKey(className))
            return domain.get(className);

        if(dtoClass.isAnnotationPresent(Table.class)) {
            Table a = dtoClass.getAnnotation(Table.class);
            String alias = "_" + a.name();
            if(a.alias().length() > 0) alias = a.alias();

            MappedClass m = new MappedClass(className, a.name(), alias);

            domain.put(className, m);

            getClassFields(dtoClass, m);

            return m;
        }

        return null;
    }

    private static <T> void getClassFields(Class<T> dtoClass, MappedClass m) {
        for(Field f:dtoClass.getDeclaredFields()) {
            if(f.isAnnotationPresent(Column.class)) {
                Column c = f.getAnnotation(Column.class);

				if (f.isAnnotationPresent(Id.class)) {
					Id i = f.getAnnotation(Id.class);
					m.addIndexField(f.getName(), c.name(), i.strategy(), (Class)f.getGenericType());
				}
				else {
					ClassField classField = m.addClassField(f.getName(), c.name(), (Class)f.getGenericType());
                    MappedClass joinMappedClass = getMappedClass((Class) f.getGenericType());
					if (joinMappedClass != null) classField.setJoinMappedClass(joinMappedClass);
				}
            }

            if(f.isAnnotationPresent(Relation.class)) {
				Relation r = f.getAnnotation(Relation.class);
            	MappedClass relationship = getMappedClass(r.type());
            	m.addRelationship(relationship, Column.LAZY);
			}
        }
    }

	private static <T> List<T> parseResultSet(ResultSet rs, Class<T> dtoClass)
			throws SQLException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InvocationTargetException {
		List<T> ret=new ArrayList<T>();

		while(rs.next())
		{
			T obj=dtoClass.getConstructor().newInstance();

			for(Field f:dtoClass.getDeclaredFields())
			{
				if(f.isAnnotationPresent(Column.class))
				{
					Column c=f.getAnnotation(Column.class);
					f.setAccessible(true);
					f.set(obj,rs.getObject(c.name()));
				}
			}

			ret.add(obj);
		}
		return ret;
	}

	// Retorna: el SQL correspondiente a la clase dtoClass acotado por xql
	public static <T> String _query(Class<T> dtoClass, String xql) throws WrongParameterException {
		String q="";
		MappedClass m = getMappedClass(dtoClass);
		if (m != null)  q = m.getSelect(xql);
		return q;
	}

	// Invoca a: _query para obtener el SQL que se debe ejecutar
	// Retorna: una lista de objetos de tipo T
	public static <T> List<T> query(Connection con, Class<T> dtoClass, String xql, Object... args) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
		//Ac� el query llama al parseXQL y se lo manda 
		//todo (menos la conexi�n)
		//y luego le pasa el XQL parseado al _query 
		//para obtener el comando SQL a ejecutar
		PreparedStatement s = con.prepareStatement(_query(dtoClass, xql));
		int i = 1;
		for (Object p : args) {
			s.setObject(i, p);
			i++;
		}

		logQuery(s.toString());
		ResultSet r = s.executeQuery();
		return parseResultSet(r, dtoClass);
	}

	// Retorna: una fila identificada por id o null si no existe
	// Invoca a: query
	public static <T> T find(Connection con, Class<T> dtoClass, Object id) throws SQLException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
		List<T> ret = query(con, dtoClass, String.format("$%s = ?", getMappedClass(dtoClass).getIndexField().getClassName()), id);
		if (ret.size() > 0) return ret.get(0);
		return null;
	}

	// Retorna: una todasa las filas de la tabla representada por dtoClass
	// Invoca a: query
	public static <T> List<T> findAll(Connection con, Class<T> dtoClass) throws SQLException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
		return query(con, dtoClass, "");
	}

	// Retorna: el SQL correspondiente a la clase dtoClass acotado por xql
	public static <T> String _update(Class<T> dtoClass, String xql) throws WrongParameterException {
		String q="";
		MappedClass m = getMappedClass(dtoClass);
		if (m != null)  q = m.getUpdate(xql);
		return q;
	}

	// Invoca a: _update para obtener el SQL que se debe ejecutar
	// Retorna: la cantidad de filas afectadas luego de ejecutar el SQL
	public static int update(Connection con, Object dto, String xql, Object... args) throws SQLException, NoSuchFieldException, IllegalAccessException {
		String q = _update(dto.getClass(), xql);

		for (ClassField c : getMappedClass(dto.getClass()).getClassFields()){
			q = c.replaceNamedParameter(q, dto);
		}

		PreparedStatement s = con.prepareStatement(q);
		int i = 1;
		for (Object p : args) {
			s.setObject(i, p);
			i++;
		}
		logQuery(s.toString());
		return s.executeUpdate();
	}

	// Invoca a: update
	// Que hace?: actualiza todos los campos de la fila identificada por el id
	// de dto
	// Retorna: Cuantas filas resultaron modificadas (deberia: ser 1 o 0)
	public static int update(Connection con, Object dto) throws NoSuchFieldException, IllegalAccessException, SQLException, ClassNotFoundException {
		String nombreId = getMappedClass(dto.getClass()).getIndexField().getClassName();

		Field fieldId = dto.getClass().getDeclaredField(nombreId);
		fieldId.setAccessible(true);

		String q = _update(dto.getClass(), String.format("$%s = %s", nombreId, fieldId.get(dto)));

		for (ClassField c : getMappedClass(dto.getClass()).getClassFields()){
			q = c.replaceNamedParameter(q, dto);
		}

		PreparedStatement s = con.prepareStatement(q);
		logQuery(s.toString());
		return s.executeUpdate();
	}

	// Retorna: el SQL correspondiente a la clase dtoClass acotado por xql
	public static String _delete(Class<?> dtoClass, String xql) throws WrongParameterException {
		String q="";
		MappedClass m = getMappedClass(dtoClass);
		if (m != null)  q = m.getDelete(xql);
		return q;
	}

	// Invoca a: _delete para obtener el SQL que se debe ejecutar
	// Retorna: la cantidad de filas afectadas luego de ejecutar el SQL
	public static int delete(Connection con, Class<?> dtoClass, String xql, Object... args) throws SQLException {
		PreparedStatement s = con.prepareStatement(_delete(dtoClass, xql));
		int i = 1;
		for (Object p :
				args) {
			s.setObject(i, p);
			i++;
		}
		logQuery(s.toString());
		return s.executeUpdate();
	}

	// Retorna la cantidad de filas afectadas al eliminar la fila identificada
	// por id
	// (deberia ser: 1 o 0)
	// Invoca a: delete
	public static int delete(Connection con, Class<?> dtoClass, Object id) throws SQLException {
		return delete(con, dtoClass, String.format("$%s = ?", getMappedClass(dtoClass).getIndexField().getClassName()), id);
	}

	// Retorna: el SQL correspondiente a la clase dtoClass
	public static String _insert(Class<?> dtoClass) throws WrongParameterException {		String q="";
		MappedClass m = getMappedClass(dtoClass);
		if (m != null)  q = m.getInsert();
		return q;
	}

	// Invoca a: _insert para obtener el SQL que se debe ejecutar
	// Retorna: la cantidad de filas afectadas luego de ejecutar el SQL
	public static int insert(Connection con, Object dto) throws IllegalArgumentException, IllegalAccessException, SQLException, NoSuchFieldException, ClassNotFoundException {
		String q = _insert(dto.getClass());
		for (ClassField c : getMappedClass(dto.getClass()).getClassFields()){
			q = c.replaceNamedParameter(q, dto);
		}

		PreparedStatement s = con.prepareStatement(q);
		logQuery(s.toString());
		return s.executeUpdate();
	}
}