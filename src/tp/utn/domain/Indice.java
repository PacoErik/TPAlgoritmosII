package tp.utn.domain;

import tp.utn.ann.Id;

/**
 * Created by TATIANA on 23/4/2017.
 */
public class Indice extends Campo {

    private int fetchType = Id.ASSIGNED;

    public Indice(String nombreClase, String nombreDatabase, int fetchType) {
        super(nombreClase, nombreDatabase);
        this.fetchType = fetchType;
    }
}
