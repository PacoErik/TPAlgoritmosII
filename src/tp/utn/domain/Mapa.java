package tp.utn.domain;

import tp.utn.excepciones.ParametroIncorrectoExcepcion;
import tp.utn.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class Mapa
{
    private String nombreClase;
    private String nombreDatabase;
    private String alias;

	public Mapa(String nombreClase, String nombreDatabase, String alias) {
	    this.nombreClase = nombreClase;
	    this.nombreDatabase = nombreDatabase;
	    this.alias = alias;
	}

    private Indice indice;

	private List<Campo> campos = new ArrayList<Campo>();
    private List<Relacion> relaciones = new ArrayList<Relacion>();

    public Campo addCampo(String nombreClase, String nombreDatabase){
        Campo n = new Campo(nombreClase, nombreDatabase);
        getCampos().add(n);
        return n;
    }

    public void addIndice(String nombreClase, String nombreDatabase, int fetchType){
        indice = new Indice(nombreClase, nombreDatabase, fetchType);
        getCampos().add(indice);
    }

    public void addRelacion(Mapa destino, int fetchType){
        relaciones.add(new Relacion(destino, fetchType));
    }

    public Indice getIndice() {
        return indice;
    }

    public List<Relacion> getRelaciones() {
        return relaciones;
    }

    protected List<String> getNombreDatabaseCampos () {
        List<String> ret = new ArrayList<String>();
        for (Campo c : getCampos()) {
            ret.addAll(c.getCampos(getAlias()));
        }
        return ret;
    }

    protected String getJoins () {
        String q = "";
        for (Campo c : getCampos()) {
            q += c.getJoins(alias);
        }
        return q;
    }

    protected String getXql (String padreClase, String xql) {
        for (Campo c : getCampos()) {
            xql = c.getXql(padreClase, getAlias(), xql);
        }
        return xql;
    }

    public String getSelect(String xql) throws ParametroIncorrectoExcepcion {
        String q = "";
        q = "SELECT " + StringUtil.join(getNombreDatabaseCampos(), ", ");
        q += " FROM " + getNombreDatabase();
        q += " AS " + getAlias();
        q += getJoins();

        if(!xql.isEmpty()) {
            q += " WHERE " + getXql("", xql);

            if (q.contains(" $")) throw new ParametroIncorrectoExcepcion();
        }

        return q;
    }

    protected String getNombreClase() {
        return nombreClase;
    }

    protected String getNombreDatabase() {
        return nombreDatabase;
    }

    protected String getAlias() {
        return alias;
    }

    public List<Campo> getCampos() {
        return campos;
    }
}
