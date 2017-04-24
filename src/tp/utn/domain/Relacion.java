package tp.utn.domain;

import tp.utn.ann.Column;

/**
 * Created by TATIANA on 23/4/2017.
 */
public class Relacion {
    private Mapa tabla; //La tabla destino siempre se joinea por id
    private int fetchType = Column.EAGER;

    public Relacion(Mapa tabla, int fetchType) {
        this.tabla = tabla;
        this.fetchType = fetchType;
    }

    public Mapa getTabla() {
        return tabla;
    }

    public int getFetchType() {
        return fetchType;
    }
}
