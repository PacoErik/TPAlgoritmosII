package tp.utn.domain;

import tp.utn.ann.Column;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by TATIANA on 22/4/2017.
 */
public class Campo {
    private String nombreClase;
    private String nombreDatabase;
    private Mapa tablaJoin;
    private int fetchType = Column.EAGER;

    public Campo(String nombreClase, String nombreDatabase) {
        this.nombreClase = nombreClase;
        this.nombreDatabase = nombreDatabase;
    }

    public String getNombreClase() { return nombreClase; }

    public String getNombreDatabase() {
        return nombreDatabase;
    }

    public Mapa getTablaJoin() {
        return tablaJoin;
    }

    public void setTablaJoin(Mapa tablaJoin) { this.tablaJoin = tablaJoin; }

    public int getFetchType() {
        return fetchType;
    }

    public void setFetchType(int fetchType) {
        this.fetchType = fetchType;
    }

    protected List<String> getCampos(String alias) {
        List<String> ret = new ArrayList<String>();
        ret.add(alias + "." + nombreDatabase);

        if (tablaJoin != null)
            ret.addAll(tablaJoin.getNombreDatabaseCampos());

        return ret;
    }

    protected String getJoins(String alias) {
        String q = "";

        if (tablaJoin != null && getFetchType() == Column.EAGER) {
            q += " INNER JOIN " + tablaJoin.getNombreDatabase() + " AS " + tablaJoin.getAlias() +
                    " ON " + tablaJoin.getAlias() + "." + tablaJoin.getIndice().getNombreDatabase() +
                    " = " + alias + "." + nombreDatabase;
            q += tablaJoin.getJoins();
        }

        return q;
    }

    protected String getXql(String padreClase, String alias, String xql) {
        if (tablaJoin != null) {
            String padreClaseNext = padreClase + tablaJoin.getNombreClase() + ".";
            xql = tablaJoin.getXql(padreClaseNext, xql);
        }

        String parametro = "\\$" + padreClase + nombreClase + " ";
        return xql.toLowerCase().replaceAll(parametro.toLowerCase(),  alias + "." + nombreDatabase + " ");
    }
}
