package demo;

import utn.Utn;
import utn.UtnConnectionFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

public class Main
{

	public static void main(String[] args) throws IOException, SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException {
		TipoOcupacion t = new TipoOcupacion();
		t.setIdTipoOcupacion(0);
		t.setDescripcion("Profesional");
		Utn.update(UtnConnectionFactory.getConnection(), t, "$idTipoOcupacion = ?", 0);
		System.out.println(Utn.findAll(UtnConnectionFactory.getConnection(), TipoOcupacion.class));
		//System.out.println(Utn._delete(Ocupacion.class, "$descripcion = 'Pepe'"));
		//System.out.println(Utn._update(Persona.class, ""));
		//System.out.println(Utn._query(Persona.class, "$direccion.calle = 'pep' AND $ocupacion.tipoocupacion.descripcion = '666'"));
		//System.out.println(Utn._query(Direccion.class, "$calle = 'pep'"));
		//System.out.println(Utn._query(Persona.class, "$calle = 'pep' AND $ocupacion.tipo_ocupacion.descripcion = '666'"));
	}
}
