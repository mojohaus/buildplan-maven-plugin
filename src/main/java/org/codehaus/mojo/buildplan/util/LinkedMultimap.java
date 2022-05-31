/**
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
package org.codehaus.mojo.buildplan.util;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class LinkedMultimap<K, V> implements Multimap<K, V> {

    private final Map<K, Collection<V>> map = new LinkedHashMap<>();

    @Override
    public Set<K> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<V> get(K key) {
        return map.get(key);
    }

    @Override
    public boolean put(K key, V value) {
        map.computeIfAbsent(key, k -> new ArrayList<>());
        return map.get(key).add(value);
    }

    @Override
    public Map<K, Collection<V>> asMap() {
        return map;
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public Collection<V> values() {
        return map.values().stream()
            .flatMap(Collection::stream)
            .collect(toList());
    }
}
