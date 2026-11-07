/*
 * Copyright (C) 2012 Jean-Christophe Gay (contact@jeanchristophegay.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.codehaus.mojo.buildplan.display;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import org.apache.maven.lifecycle.DefaultLifecycles;
import org.apache.maven.lifecycle.Lifecycle;

/**
 * Maven 4 reports every execution under its own phase names, {@code after:resources} where Maven 3 says
 * {@code process-resources}, for POM bindings as well as default ones. The build plan keeps the Maven 3
 * vocabulary, so the Maven 4 names are mapped back through the alias table Maven 4 publishes on its
 * lifecycle model. That model is only reachable through the Maven 4 API, which this plugin does not
 * compile against, so the lookup is reflective and a no-op on Maven 3.
 */
final class PhaseNames {

    private static final Map<DefaultLifecycles, Map<String, String>> CACHE =
            Collections.synchronizedMap(new WeakHashMap<>());

    private PhaseNames() {}

    static String toMaven3Name(String phase, DefaultLifecycles defaultLifecycles) {
        if (phase == null || defaultLifecycles == null) {
            return phase;
        }
        return CACHE.computeIfAbsent(defaultLifecycles, PhaseNames::aliases).getOrDefault(phase, phase);
    }

    private static Map<String, String> aliases(DefaultLifecycles defaultLifecycles) {
        Map<String, String> result = new HashMap<>();
        for (Lifecycle lifecycle : defaultLifecycles.getLifeCycles()) {
            try {
                Object delegate = invoke(lifecycle, "getDelegate");
                for (Object alias : (Collection<?>) invoke(delegate, "aliases")) {
                    result.put((String) invoke(alias, "v4Phase"), (String) invoke(alias, "v3Phase"));
                }
            } catch (ReflectiveOperationException | ClassCastException e) {
                // Maven 3: no delegate, no aliases, nothing to map.
            }
        }
        return result;
    }

    /** Invokes a no-arg method through a public type, since the implementing class may not be public. */
    private static Object invoke(Object target, String name) throws ReflectiveOperationException {
        for (Class<?> type = target.getClass(); type != null; type = type.getSuperclass()) {
            if (Modifier.isPublic(type.getModifiers())) {
                try {
                    return type.getMethod(name).invoke(target);
                } catch (NoSuchMethodException e) {
                    // try the interfaces
                }
            }
            for (Class<?> iface : type.getInterfaces()) {
                if (Modifier.isPublic(iface.getModifiers())) {
                    for (Method method : iface.getMethods()) {
                        if (method.getName().equals(name) && method.getParameterCount() == 0) {
                            return method.invoke(target);
                        }
                    }
                }
            }
        }
        throw new NoSuchMethodException(target.getClass().getName() + "." + name + "()");
    }
}
