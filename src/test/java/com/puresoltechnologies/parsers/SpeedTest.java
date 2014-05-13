package com.puresoltechnologies.parsers;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

import org.junit.Test;

public class SpeedTest {

	@Test
	public void testLists() {
		List<Integer> list1 = new CopyOnWriteArrayList<Integer>();
		long start = System.nanoTime();
		for (int i = 0; i < 10000; i++) {
			list1.add(i);
		}
		long stop = System.nanoTime();
		double sec1 = (stop - start) / 1e9;
		System.out.println(sec1);

		List<Integer> list2 = new ArrayList<Integer>();
		start = System.nanoTime();
		for (int i = 0; i < 10000; i++) {
			list2.add(i);
		}
		stop = System.nanoTime();
		double sec2 = (stop - start) / 1e9;
		System.out.println(sec2);

		assertTrue(sec1 > sec2 * 10);
	}

	@Test
	public void testSets() {
		Set<Integer> set1 = new CopyOnWriteArraySet<Integer>();
		long start = System.nanoTime();
		for (int i = 0; i < 10000; i++) {
			set1.add(i);
		}
		long stop = System.nanoTime();
		double sec1 = (stop - start) / 1e9;
		System.out.println(sec1);

		Set<Integer> set2 = new LinkedHashSet<Integer>();
		start = System.nanoTime();
		for (int i = 0; i < 10000; i++) {
			set2.add(i);
		}
		stop = System.nanoTime();
		double sec2 = (stop - start) / 1e9;
		System.out.println(sec2);

		Set<Integer> set3 = new HashSet<Integer>();
		start = System.nanoTime();
		for (int i = 0; i < 10000; i++) {
			set3.add(i);
		}
		stop = System.nanoTime();
		double sec3 = (stop - start) / 1e9;
		System.out.println(sec3);

		assertTrue(sec1 > sec2 * 5);
		assertTrue(sec1 > sec3 * 5);
	}

	@Test
	public void testMaps() {
		Map<Integer, Integer> map1 = new ConcurrentHashMap<Integer, Integer>();
		long start = System.nanoTime();
		for (int i = 0; i < 10000; i++) {
			map1.put(i, i);
		}
		long stop = System.nanoTime();
		double sec1 = (stop - start) / 1e9;
		System.out.println(sec1);

		Map<Integer, Integer> map2 = new HashMap<Integer, Integer>();
		start = System.nanoTime();
		for (int i = 0; i < 10000; i++) {
			map2.put(i, i);
		}
		stop = System.nanoTime();
		double sec2 = (stop - start) / 1e9;
		System.out.println(sec2);

		assertTrue(sec1 > sec2);
	}

}
