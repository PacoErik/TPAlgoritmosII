package utn.domain;

import utn.ann.Column;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TATIANA on 22/4/2017.
 */
public class ClassField {
    private static List<String> quotedClasses() {
        List<String> ret = new ArrayList<String>();
        ret.add("class java.lang.String");
        ret.add("class java.util.Date");
        return ret;
    }

    private String className;
    private String databaseName;
    private Class dataType;
    private MappedClass joinMappedClass;
    private int fetchType = Column.EAGER;

    public ClassField(String className, String databaseName, Class genericType) {
        this.className = className;
        this.databaseName = databaseName;
        this.dataType = genericType;
    }

    public String getClassName() { return className; }

    public String getDatabaseName() { return databaseName; }

    public MappedClass getJoinMappedClass() {
        return joinMappedClass;
    }

    public void setJoinMappedClass(MappedClass joinMappedClass) { this.joinMappedClass = joinMappedClass; }

    public int getFetchType() {
        return fetchType;
    }

    public void setFetchType(int fetchType) {
        this.fetchType = fetchType;
    }

    protected List<String> getClassFields(String alias) {
        List<String> ret = new ArrayList<String>();
        if (alias != null)
            ret.add(alias + "." + databaseName);
        else
            ret.add(databaseName);

        if (joinMappedClass != null)
            ret.addAll(joinMappedClass.getDatabaseFiledsName());

        return ret;
    }

    protected String getJoins(String alias) {
        String q = "";

        if (joinMappedClass != null && getFetchType() == Column.EAGER) {
            q += " INNER JOIN " + joinMappedClass.getDatabaseName() + " AS " + joinMappedClass.getAlias() +
                    " ON " + joinMappedClass.getAlias() + "." + joinMappedClass.getIndexField().getDatabaseName() +
                    " = " + alias + "." + databaseName;
            q += joinMappedClass.getJoins();
        }

        return q;
    }

    protected String getXql(String superClassName, String alias, String xql) {

        String parameter = "\\$" + className + " ";

        if (superClassName != null) {
            if (joinMappedClass != null) {
                String superClassNext = superClassName + joinMappedClass.getClassName() + ".";
                xql = joinMappedClass.getXql(superClassNext, xql);
            }

            parameter = "\\$" + superClassName + className + " ";

            return xql.toLowerCase().replaceAll(parameter.toLowerCase(), alias + "." + databaseName + " ");
        }

        return xql.toLowerCase().replaceAll(parameter.toLowerCase(), databaseName + " ");
    }

    public Class getDataType() {
        return dataType;
    }

    public void setDataType(Class dataType) {
        this.dataType = dataType;
    }

    protected String valueFormat() {
        String ret = "%d";
        if (quotedClasses().contains(getDataType().toString())) ret = "'%s'";
        return ret;
    }

    public String replaceNamedParameter(String query, Object dto) throws NoSuchFieldException, IllegalAccessException {
        Field field = dto.getClass().getDeclaredField(getClassName());
        field.setAccessible(true);
        return query.replace(String.format(":%s", getDatabaseName()),
            String.format(valueFormat(), field.get(dto)));
    }
}
