package com.forsrc.common.pool;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by FengJianJun on 2017/5/22.
 */
public class Pool {

	//<<---------------------------------------- initialize ----------------------------------------

	//>>---------------------------------------- initialize ----------------------------------------

	//<<---------------------------------------- public ----------------------------------------

	//<<<---------------------------------------- create ----------------------------------------

	public static <T> List<T> createLinkedList() {
		return Collections.synchronizedList(new LinkedList<>());
	}

	public static <T> List<T> createArrayList() {
		//在两个线程下 Collections.synchronizedMap 访问时间大概是 CopyOnWriteArrayList 的5倍，但在64线程的时候就变成了200倍+
		//在线程数目增加时 CopyOnWriteArrayList 的写操作性能下降非常严重，而 Collections.synchronizedList 下降并不明显。
		return Collections.synchronizedList(new LinkedList<>());
	}

	public static <T> List<T> createCopyOnWriteArrayList() {
		return new CopyOnWriteArrayList<>();
	}

	public static <K, V> Map<K, V> createMap() {
		return new ConcurrentHashMap<>();
	}

	//>>>---------------------------------------- create ----------------------------------------

	//<<<---------------------------------------- common ----------------------------------------

	public static <T> void add(List<T> list, T t) {
		list.add(t);
	}

	public static <T> T get(List<T> list, int index) {
		return list.get(index);
	}

	public static <T> T remove(List<T> list) {
		try {
			return list.remove(0);
		} catch (Exception e) {
			return null;
		}
	}

	public static <T> int size(List<T> list) {
		return list.size();
	}

	public static <K, V> void add(Map<K, V> map, K k, V v) {
		map.put(k, v);
	}

	public static <K, V> V get(Map<K, V> map, K k) {
		return map.get(k);
	}

  public static <K, V> V remove(Map<K, V> map, K k) {
    return map.remove(k);
  }

	public static <K, V> int size(Map<K, V> map) {
		return map.size();
	}

	//>>>---------------------------------------- common ----------------------------------------

	//>>---------------------------------------- public ----------------------------------------

	//<<---------------------------------------- protected ----------------------------------------

	//>>---------------------------------------- protected ----------------------------------------

	//<<---------------------------------------- private ----------------------------------------

	//<<<---------------------------------------- inner ----------------------------------------

	//>>>---------------------------------------- inner ----------------------------------------

	//<<<---------------------------------------- tool ----------------------------------------

	//>>>---------------------------------------- tool ----------------------------------------

	//>>---------------------------------------- private ----------------------------------------

	//<<---------------------------------------- get ----------------------------------------

	//>>---------------------------------------- get ----------------------------------------

	//<<---------------------------------------- set ----------------------------------------

	//>>---------------------------------------- set ----------------------------------------

	//<<---------------------------------------- get set ----------------------------------------

	//>>---------------------------------------- get set ----------------------------------------


}
