/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.anyframe.simpleweb.beans.support;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ConcurrentCache
 * @author Changje Kim
 */
public final class ConcurrentCache<K, V> {

	private final int size;

	private final Map<K, V> eden;

	private final Map<K, V> longTerm;

	public ConcurrentCache(int size) {
		this.size = size;
		this.eden = new ConcurrentHashMap<K, V>(size);
		this.longTerm = new WeakHashMap<K, V>(size);
	}

	public V get(K k) {
		V v = this.eden.get(k);
		if (v == null) {
			v = this.longTerm.get(k);
			if (v != null) {
				this.eden.put(k, v);
			}
		}
		return v;
	}

	public void put(K k, V v) {
		if (this.eden.size() >= size) {
			this.longTerm.putAll(this.eden);
			this.eden.clear();
		}
		this.eden.put(k, v);
	}
}
