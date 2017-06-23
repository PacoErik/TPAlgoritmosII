package utn.domain;

import utn.ann.Column;

import java.io.File;
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

    private Field field;
    private String databaseName;
    private MappedClass joinMappedClass;
    private int fetchType = Column.EAGER;

    public ClassField(Field field, String databaseName, int fetchType) {
        this.databaseName = databaseName;
        this.field = field;
        this.fetchType = fetchType;
    }

    public Field getField() {
        return field;
    }

    public String getClassName() {
        return field.getName();
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public int getFetchType() {
        return fetchType;
    }

    public Class getDataType() {
        return (Class) field.getGenericType();
    }

    public MappedClass getJoinMappedClass() {
        return joinMappedClass;
    }

    public void setJoinMappedClass(MappedClass joinMappedClass) {
        this.joinMappedClass = joinMappedClass;
    }

    protected List<String> getClassFields(String alias) {
        List<String> ret = new ArrayList<String>();
        if (alias != null)
            ret.add(alias + "." + databaseName + " AS " + alias + "_" + databaseName);
        else
            ret.add(databaseName);

        if (joinMappedClass != null)
            ret.addAll(joinMappedClass.getDatabaseFiledsNameJoin());

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

        String parameter = "\\$" + getClassName() + " ";

        if (superClassName != null) {
            if (joinMappedClass != null) {
                String superClassNext = superClassName + joinMappedClass.getClassName() + ".";
                xql = joinMappedClass.getXql(superClassNext, xql);
            }

            parameter = "\\$" + superClassName + getClassName() + " ";

            return xql.toLowerCase().replaceAll(parameter.toLowerCase(), alias + "." + databaseName + " ");
        }

        return xql.toLowerCase().replaceAll(parameter.toLowerCase(), databaseName + " ");
    }

    protected String valueFormat() {
        String ret = "%d";
        if (quotedClasses().contains(getDataType().toString())) ret = "'%s'";
        return ret;
    }

    public String replaceNamedParameter(String query, Object dto) throws NoSuchFieldException, IllegalAccessException {
        if (joinMappedClass != null) {
            joinMappedClass.getIndexField().getField().setAccessible(true);
            return query.replace(String.format(":%s", getDatabaseName()), String.format(valueFormat(), joinMappedClass.getIndexField().getField().get(field.get(dto))));
        }
        field.setAccessible(true);
        return query.replace(String.format(":%s", getDatabaseName()), String.format(valueFormat(), field.get(dto)));
    }
}
