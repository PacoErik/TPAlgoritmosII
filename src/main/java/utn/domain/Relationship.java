package utn.domain;

import utn.ann.Column;

/**
 * Created by TATIANA on 23/4/2017.
 */
public class Relationship {
    private MappedClass mappedClass; //La mappedClass destino siempre se joinea por id
    private int fetchType = Column.EAGER;

    public Relationship(MappedClass mappedClass, int fetchType) {
        this.mappedClass = mappedClass;
        this.fetchType = fetchType;
    }

    public MappedClass getMappedClass() {
        return mappedClass;
    }

    public int getFetchType() {
        return fetchType;
    }
}
