package ru.spbstu.telematics;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MyHashMapTest {
    private HashMap<Integer, String> expected = new HashMap<>();
    private MyHashMap<Integer, String> actual = new MyHashMap<>();

    @Test
    public void testInit() {
        assertTrue(expected.isEmpty());
        assertTrue(expected.size() == 0);
    }

    @Test
    public void testPutElements() {
        String expName1 = expected.put(224455, "Masha Ivanova");
        String expName2 = expected.put(778899, "Fedya Popov");
        String actName1 = actual.put(224455, "Masha Ivanova");
        String actName2 = actual.put(778899, "Fedya Popov");

        assertEquals(expName1, actName1);
        assertEquals(expName2, actName2);
        assertEquals(expected.get(224455), actual.get(224455));
        assertEquals(expected.get(778899), actual.get(778899));
        assertEquals(expected.size(), actual.size());
    }

    @Test
    public void putNullKey() {
        expected.put(null, "Name1");
        actual.put(null, "Name1");
        assertEquals(expected.get(null), actual.get(null));
        expected.put(null, "Name2");
        actual.put(null, "Name2");
        assertEquals(expected.get(null), actual.get(null));
    }

    @Test
    public void testCollision(){
        actual.put(4, "test1");
        actual.put(224455, "test2");

        assertEquals(actual.get(4), "test1");
        assertEquals(actual.get(224455), "test2");
    }

    @Test
    public void testThreshold(){
        MyHashMap<Integer,String> actual2 = new MyHashMap<>(4, 0.25f);
        assertEquals(actual2.getCapacity(), 4);
        actual2.put(10, "element");
        assertEquals(actual2.getCapacity(), 8);
    }


    @Test
    public void testSetElement() {
        String expName2 = expected.put(778899, "Fedya Popov");
        String actName2 = actual.put(778899, "Fedya Popov");

        String expName = expected.put(778899, "Another Name");
        String actName = actual.put(778899, "Another Name");

        assertEquals(expName, actName);
        assertEquals(expected.get(778899), actual.get(778899));
    }

    @Test
    public void testContains() {
        String expName1 = expected.put(224455, "Masha Ivanova");

        assertTrue(expected.containsKey(224455));
        assertFalse(expected.containsKey(543210));

        assertTrue(expected.containsValue("Masha Ivanova"));
        assertFalse(expected.containsValue("Some Name"));
    }

    @Test
    public void testRemove() {
        expected.put(224455, "Masha Ivanova");
        expected.put(778899, "Fedya Popov");
        expected.put(661144, "Pasha Sidorov");
        actual.put(224455, "Masha Ivanova");
        actual.put(778899, "Fedya Popov");
        actual.put(661144, "Pasha Sidorov");

        String expName = expected.remove(778899);
        String actName = actual.remove(778899);

        assertEquals(expName, actName);
        assertEquals(expected.containsKey(778899), actual.containsKey(778899));
    }

    @Test
    public void testClear() {
        expected.put(224455, "Masha Ivanova");
        expected.put(778899, "Fedya Popov");
        expected.put(661144, "Pasha Sidorov");
        actual.put(224455, "Masha Ivanova");
        actual.put(778899, "Fedya Popov");
        actual.put(661144, "Pasha Sidorov");

        assertEquals(expected.size(), actual.size());

        expected.clear();
        actual.clear();

        assertEquals(expected.isEmpty(), actual.isEmpty());
    }

    @Test
    public void testIteratorAndKeyView() {
        expected.put(224455, "Masha Ivanova");
        expected.put(778899, "Fedya Popov");
        expected.put(661144, "Pasha Sidorov");
        actual.put(224455, "Masha Ivanova");
        actual.put(778899, "Fedya Popov");
        actual.put(661144, "Pasha Sidorov");

        ArrayList<Integer> expKeys = new ArrayList<>();
        for (Integer key : expected.keySet()) {
            expKeys.add(key);
        }
        ArrayList<Integer> actKeys = new ArrayList<>();
        for (Integer key : actual.keySet()) {
            actKeys.add(key);
        }
        assertEquals(expKeys, actKeys);
    }

    @Test
    public void testIteratorAndValueView() {
        expected.put(224455, "Masha Ivanova");
        expected.put(778899, "Fedya Popov");
        expected.put(661144, "Pasha Sidorov");
        actual.put(224455, "Masha Ivanova");
        actual.put(778899, "Fedya Popov");
        actual.put(661144, "Pasha Sidorov");

        ArrayList<String> expValues = new ArrayList<>();
        for (String value : expected.values()) {
            expValues.add(value);
        }
        ArrayList<String> actValues = new ArrayList<>();
        for (String value : actual.values()) {
            actValues.add(value);
        }
        assertEquals(expValues, actValues);
    }

    @Test
    public void testIteratorAndEntryView() {
        expected.put(224455, "Masha Ivanova");
        expected.put(778899, "Fedya Popov");
        expected.put(661144, "Pasha Sidorov");
        actual.put(224455, "Masha Ivanova");
        actual.put(778899, "Fedya Popov");
        actual.put(661144, "Pasha Sidorov");

        ArrayList<Integer> expKeys = new ArrayList<>();
        ArrayList<String> expValues = new ArrayList<>();
        for (Map.Entry<Integer, String> entry : expected.entrySet()) {
            expKeys.add(entry.getKey());
            expValues.add(entry.getValue());
        }

        ArrayList<Integer> actKeys = new ArrayList<>();
        ArrayList<String> actValues = new ArrayList<>();
        for (MyHashMap.Entry<Integer, String> entry : actual.entrySet()) {
            actKeys.add(entry.getKey());
            actValues.add(entry.getValue());
        }

        assertEquals(expKeys, actKeys);
        assertEquals(expValues, actValues);
    }

}
