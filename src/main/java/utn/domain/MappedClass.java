package utn.domain;

import org.mockito.cglib.proxy.Enhancer;
import utn.exceptions.WrongParameterException;
import utn.util.StringUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MappedClass
{
    private String databaseName;
    private String alias;
    private Enhancer enhancer;
    private Class mappedClass;

	public MappedClass(Class mappedClass, String databaseName, String alias) {
	    this.mappedClass = mappedClass;
	    this.databaseName = databaseName;
	    this.alias = alias;

	    enhancer = new Enhancer();
	    enhancer.setSuperclass(mappedClass);
	    enhancer.setCallback(new LazyInterceptor(this));
	}

    private IndexField indexField;

	private List<ClassField> classFields = new ArrayList<ClassField>();
    private List<Relationship> relationships = new ArrayList<Relationship>();

    public Object create() {
        return enhancer.create();
    }

    public ClassField addClassField(Field field, String databaseName, int fetchType){
        ClassField n = new ClassField(field, databaseName, fetchType);
        getClassFields().add(n);
        return n;
    }

    public void addIndexField(Field field, String databaseName, int fetchType, boolean identity){
        indexField = new IndexField(field, databaseName, fetchType, identity);
        getClassFields().add(indexField);
    }

    public void addRelationship(MappedClass destiny, Field field, int fetchType, String attribute){
        relationships.add(new Relationship(destiny, field, fetchType, attribute));
    }

    public IndexField getIndexField() {
        return indexField;
    }

    public List<Relationship> getRelationships() {
        return relationships;
    }

    protected List<String> getDatabaseFiledsNameJoin () {
        List<String> ret = new ArrayList<String>();
        for (ClassField c : getClassFields()) {
                ret.addAll(c.getClassFields(null));
        }
        return ret;
    }

    protected List<String> getDatabaseFiledsName () {
        List<String> ret = new ArrayList<String>();
        for (ClassField c : getClassFields()) {
            if (!((c.getDatabaseName() == getIndexField().getDatabaseName()) && (getIndexField().getIdentity())))
                ret.add(c.getDatabaseName());
        }
        return ret;
    }

    protected String getJoins () {
        String q = "";
        for (ClassField c : getClassFields()) {
            q += c.getJoins(alias);
        }
        return q;
    }

    protected String getXql (String padreClase, String xql) {
        for (ClassField c : getClassFields()) {
            xql = c.getXql(padreClase, getAlias(), xql);
        }
        return xql;
    }

    public String getSelect(String xql) throws WrongParameterException {
        String q = "";
        q = "SELECT " + StringUtil.join(getDatabaseFiledsNameJoin(), ", ");
        q += " FROM " + getDatabaseName();
        q += " AS " + getAlias();
        q += getJoins();

        if(xql.length() > 0) {
            q += " WHERE " + getXql("", xql);

            if (q.contains(" $")) throw new WrongParameterException();
        }

        return q;
    }

    public String getDelete(String xql) throws WrongParameterException {
        String q = "";
        q = "DELETE FROM " + getDatabaseName();

        if(xql.length() > 0) {
            q += " WHERE " + getXql(null, xql);

            if (q.contains(" $")) throw new WrongParameterException();
        }

        return q;
    }

    public String getInsert() throws WrongParameterException {
        List<String> fields = getDatabaseFiledsName();
        String q = "";
        q = "INSERT INTO " + getDatabaseName();
        q += "(" + StringUtil.join(fields, ", ") + ") VALUES (";

        for(String c : fields)
            q += String.format(":%s, ",c,c);

        return StringUtil.replaceLast(q, ", ") + ")";
    }

    public String getUpdate(String xql) throws WrongParameterException {
        String q = "";
        q = "UPDATE " + getDatabaseName() + " ";

        if(xql.length() > 0) {
            q += getXql(null, xql);

            if (q.contains(" $")) throw new WrongParameterException();
        }

        return q;
    }

    protected String getClassName() { return mappedClass.getSimpleName(); }

    public Class getMappedClass() { return mappedClass; }

    protected String getDatabaseName() {
        return databaseName;
    }

    protected String getAlias() {
        return alias;
    }

    public List<ClassField> getClassFields() {
        return classFields;
    }
}
