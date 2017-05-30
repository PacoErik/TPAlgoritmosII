package utn.domain;

import org.mockito.cglib.proxy.InvocationHandler;
import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.cglib.proxy.MethodProxy;
import utn.Utn;
import utn.UtnConnectionFactory;

import java.lang.reflect.Method;

/**
 * Created by TATIANA on 29/5/2017.
 */
public class LazyInterceptor implements MethodInterceptor {

    private MappedClass mappedClass;

    public LazyInterceptor(MappedClass mappedClass) {
        this.mappedClass = mappedClass;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        for (Relationship r : mappedClass.getRelationships()) {
            if (method.getName().equals(r.getGetterClassName())) {
                r.getField().setAccessible(true);
                if (r.getField().get(o) == null) {
                    r.getField().set(o, Utn.query(UtnConnectionFactory.getConnection(),
                            r.getMappedClass().getMappedClass(), "$" + r.getAttribute() + " = ?",
                            mappedClass.getIndexField().getField().get(o)));
                }

                return r.getField().get(o);
            }
        }

        return methodProxy.invokeSuper(o, objects);
    }
}
