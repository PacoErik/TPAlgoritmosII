import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.junit.Assert;

import domain.Direccion;
import domain.Ocupacion;
import domain.Persona;
import domain.PersonaDireccion;
import domain.TipoOcupacion;
import utn.Utn;
import utn.UtnConnectionFactory;

public class Test
{
	@org.junit.Test
	public void testFind() throws IOException, SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
		Connection con = UtnConnectionFactory.getConnection();
		
		// verifico el find
		Persona p = Utn.find(con,Persona.class,12);
		Assert.assertEquals(p.getNombre(),"Pablo");

		// ocupacion es LAZY => debe permanecer NULL hasta que haga el get
		Assert.assertNull(p.ocupacion);

		Assert.assertEquals((Integer)p.getOcupacion().getIdOcupacion(),(Integer)4);

		// debe traer el objeto
		Ocupacion o = p.getOcupacion();
		Assert.assertNotNull(o);
	
		// verifico que lo haya traido bien
		Assert.assertEquals(o.getDescripcion(),"Ingeniero");
	
		// tipoOcupacion (por default) es EAGER => no debe ser null
		Assert.assertNotNull(o.getTipoOcupacion());
		TipoOcupacion to = o.getTipoOcupacion();
		
		// verifico que venga bien...
		Assert.assertEquals(to.getDescripcion(),"Profesional");
		
		// -- Relation --
		
		// las relaciones son LAZY si o si!
		Assert.assertNull(p.direcciones);
		
		List<PersonaDireccion> dirs = p.getDirecciones();
		Assert.assertNotNull(dirs);
		
		// debe tener 2 elementos
		Assert.assertEquals(dirs.size(),2);
		
		for(PersonaDireccion pd:dirs)
		{
			Persona p1 = pd.getPersona();
			Direccion d = pd.getDireccion();
			
			Assert.assertNotNull(p1);
			Assert.assertNotNull(d);
		
			Assert.assertEquals(p1.getNombre(),p.getNombre());
		}
		
	}

	@org.junit.Test
	public void testXQL() throws SQLException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException, IOException {
		Connection con = UtnConnectionFactory.getConnection();
		Utn.query(con, Persona.class, "$ocupacion.tipoOcupacion.descripcion LIKE ?", "Profesional");
	}
}
