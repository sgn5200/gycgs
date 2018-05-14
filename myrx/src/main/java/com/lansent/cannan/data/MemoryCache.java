package com.lansent.cannan.data;

import android.text.TextUtils;
import android.util.LruCache;

/**
 * Description    : 添加内存缓存，大小为运行内存的1/6
 * CreateAuthor: Cannan
 * Create time   : 2017/10/19     10:23
 */

public class MemoryCache {

	private LruCache cache;

	private static MemoryCache instance;

	public static MemoryCache getInstance(){
		if(instance == null){
			instance = new MemoryCache();
		}
		return instance;
	}

	public MemoryCache(){
		int maxSize = (int) (Runtime.getRuntime().maxMemory()/6);
		cache = new LruCache(maxSize);
	}


	public synchronized void put(String key, Object value) {
		if (TextUtils.isEmpty(key)) return;

		if (cache.get(key) != null) {
			cache.remove(key);
		}
		cache.put(key, value);
	}

	public Object get(String key) {
		return cache.get(key);
	}

	public synchronized <T> T get(String key, Class<T> clazz) {
		try {
			return (T) cache.get(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void remove(String key) {
		if (cache.get(key) != null) {
			cache.remove(key);
		}
	}

	public boolean contains(String key) {
		return cache.get(key) != null;
	}

	public void clear() {
		cache.evictAll();
	}

}
