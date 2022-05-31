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

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface Multimap<K, V> {

    Set<K> keySet();

    Collection<V> get(K key);

    boolean put(K key, V value);

    Map<K, Collection<V>> asMap();

    boolean isEmpty();

    Collection<V> values();
}
