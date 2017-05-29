package utn.domain;

import utn.ann.Id;

/**
 * Created by TATIANA on 23/4/2017.
 */
public class IndexField extends ClassField {

    private int fetchType = Id.ASSIGNED;

    public IndexField(String className, String databaseName, int fetchType, Class genericType) {
        super(className, databaseName, genericType);
        this.fetchType = fetchType;
    }
}
