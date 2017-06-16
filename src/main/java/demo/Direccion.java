package demo;

import utn.ann.Column;
import utn.ann.Id;
import utn.ann.Relation;
import utn.ann.Table;

import java.util.Collection;

@Table(name="direccion")
public class Direccion
{
	@Id(strategy=Id.IDENTITY)
	@Column(name="id_direccion")
	private Integer idDireccion;

	@Column(name="calle")
	private String calle;

	@Column(name="numero")
	private int numero;

	public Direccion() {

	}
	private Direccion(String calle, int numero) {
		this.setCalle(calle);
		this.setNumero(numero);
	}

	public static Direccion create(String calle, int numero) {
		return new Direccion(calle, numero);
	}
	
	@Relation(type= Persona.class, att="Direccion")
	private Collection<Persona> personas;

	public Collection<Persona> getPersonas()
	{
		return personas;
	}

	public void setPersonas(Collection<Persona> personas)
	{
		this.personas=personas;
	}

	public Integer getIdDireccion()
	{
		return idDireccion;
	}

	public void setIdDireccion(Integer idDireccion)
	{
		this.idDireccion=idDireccion;
	}

	public String getCalle()
	{
		return calle;
	}

	public void setCalle(String calle)
	{
		this.calle=calle;
	}

	public int getNumero()
	{
		return numero;
	}

	public void setNumero(int numero)
	{
		this.numero=numero;
	}

	@Override
	public String toString()
	{
		return getCalle()+" "+getNumero();
	}

	@Override
	public boolean equals(Object obj)
	{
		Direccion o=(Direccion)obj;
		boolean ok = idDireccion==o.getIdDireccion()&& numero==o.getNumero();
		String sCalle=calle!=null?calle:"null";
		String sOCalle=o.getCalle()!=null?o.getCalle():"null";
		return ok&&sCalle.equals(sOCalle);
	}






}
