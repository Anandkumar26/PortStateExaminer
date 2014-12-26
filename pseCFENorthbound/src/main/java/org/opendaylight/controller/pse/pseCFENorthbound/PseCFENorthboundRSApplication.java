package org.opendaylight.controller.pse.pseCFENorthbound;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

public class PseCFENorthboundRSApplication extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<Class<?>>();
        classes.add(PseCFENorthbound.class);
        return classes;
    }
}
