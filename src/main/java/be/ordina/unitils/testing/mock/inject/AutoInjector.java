package be.ordina.unitils.testing.mock.inject;

import java.util.Map;

/**
 * @author Filip Neven
 */
public interface AutoInjector {

    void autoInject(Object object, Map<String, Object> toInject);

}