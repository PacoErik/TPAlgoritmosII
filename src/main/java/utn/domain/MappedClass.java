package utn.domain;

import utn.ann.Id;
import utn.exceptions.WrongParameterException;
import utn.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class MappedClass
{
    private String className;
    private String databaseName;
    private String alias;

	public MappedClass(String className, String databaseName, String alias) {
	    this.className = className;
	    this.databaseName = databaseName;
	    this.alias = alias;
	}

    private IndexField indexField;

	private List<ClassField> classFields = new ArrayList<ClassField>();
    private List<Relationship> relationships = new ArrayList<Relationship>();

    public ClassField addClassField(String className, String databaseName, Class genericType){
        ClassField n = new ClassField(className, databaseName, genericType);
        getClassFields().add(n);
        return n;
    }

    public void addIndexField(String className, String databaseName, int fetchType, Class genericType){
        indexField = new IndexField(className, databaseName, fetchType, genericType);
        getClassFields().add(indexField);
    }

    public void addRelationship(MappedClass destiny, int fetchType){
        relationships.add(new Relationship(destiny, fetchType));
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
            if ((c.getDatabaseName() != getIndexField().getDatabaseName()) &&
               (getIndexField().getFetchType() != Id.IDENTITY))
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
        q = "UPDATE " + getDatabaseName() + " SET ";

        for (ClassField c : getClassFields()) {
            if (c.getDatabaseName() != getIndexField().getDatabaseName())
                q += String.format("%s = :%s, ", c.getDatabaseName(), c.getDatabaseName());
        }

        q = StringUtil.replaceLast(q, ", ");

        if(xql.length() > 0) {
            q += " WHERE " + getXql(null, xql);

            if (q.contains(" $")) throw new WrongParameterException();
        }

        return q;
    }

    protected String getClassName() { return className; }

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
