package ru.spbstu.telematics;

import java.util.*;

public class MyHashMap<K,V> {
    static final int MAX_CAPACITY = 1 << 20;
    static final int DEFAULT_CAPACITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;


    private Entry<K,V>[] table; // массив ссылок на списки значений
    private int size; // количество элементов
    private int threshold; // предельное кол-во элементов, после которого увеличивается размер hashmap

    private final float loadFactor; // коэф загрузки, для вычисления threshold

    static class Entry<K, V> implements Map.Entry<K, V> {
        final int hash;
        final K key;
        V value;
        Entry<K, V> next;

        public Entry(int hash, K key, V value, Entry<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public final K getKey() {
            return key;
        }
        public final V getValue() {
            return value;
        }
        public final V setValue(V newValue) {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }

    }

    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    public MyHashMap() {
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        this.threshold = (int) (DEFAULT_LOAD_FACTOR * DEFAULT_CAPACITY);
        table = new Entry[DEFAULT_CAPACITY];
    }

    public MyHashMap(int init_capacity, float init_loadFactor) {
        if (init_capacity < 0)
            throw new IllegalArgumentException("Illegal initial capacity: " + init_capacity);
        if (init_capacity > MAX_CAPACITY) init_capacity = MAX_CAPACITY;
        if (init_loadFactor <= 0)
            throw new IllegalArgumentException("Illegal initial load factor: " + init_loadFactor);

        int capacity = 1;
        while (capacity < init_capacity)
            capacity <<= 1;

        this.loadFactor = init_loadFactor;
        this.threshold = (int) (capacity * init_loadFactor);
        table = new Entry[capacity];
    }

    public MyHashMap(int init_capacity) {
        this(init_capacity, DEFAULT_LOAD_FACTOR);
    }


    public V put(K key, V value) {
        if (key == null) {
            return putForNullKey(value);
        }
        int hash = hash(key.hashCode());
        int index = hash & (table.length - 1);
        for (Entry<K, V> e = table[index]; e != null; e = e.next) {
            if (e.hash == hash && Objects.equals(key,e.key)) {
                V oldValue = e.value;
                e.value = value;
                return oldValue;
            }
        }
        putEntry(hash, key, value, index);
        return null;
    }

    private V putForNullKey(V value) {
        for (Entry<K, V> e = table[0]; e != null; e = e.next) {
            if (e.key == null) {
                V oldValue = e.value;
                e.value = value;
                return oldValue;
            }
        }
        putEntry(0, null, value, 0);
        return null;
    }

    private void putEntry(int hash, K key, V value, int index) {
        Entry<K, V> e = table[index]; // save to make it next entry
        table[index] = new Entry<K, V>(hash, key, value, e);
        if (size++ >= threshold)
            resize(2 * table.length);
    }

    private void resize(int newCapacity) {
        Entry<K, V>[] oldTable = table;
        int oldCapacity = table.length;
        if (newCapacity >= MAX_CAPACITY) {
            threshold = MAX_CAPACITY;
            return;
        }
        Entry<K, V>[] newTable = new Entry[newCapacity];
        moveTo(newTable);
        table = newTable;
        threshold = (int) (newCapacity * loadFactor);
        return;
    }

    private void moveTo(Entry[] newTable) {
        int newCapacity = newTable.length;
        for (int i = 0; i < table.length; ++i) {
            Entry<K, V> e = table[i];
            if (e != null) {
                do {
                    Entry<K, V> next = e.next;
                    int newIndex = e.hash & (newCapacity - 1);
                    e.next = newTable[newIndex]; // save to the top of the list of new index
                    newTable[newIndex] = e;
                    e = next;
                } while (e != null);
            }
        }
    }


    private Entry<K,V> getEntry(Object key){
        if (size == 0) return null;
        int hash = 0;
        if (key != null) hash = hash(key.hashCode());
        int index = hash & (table.length - 1);
        for (Entry<K,V> e = table[index]; e!=null; e=e.next){
            if (e.hash == hash && (Objects.equals(key, e.key))){
                return e;
            }
        }
        return null;
    }

    public V get(Object key) {
        Entry<K,V> e = getEntry(key);
        V value = (e == null) ? null : e.value;
        return value;
    }

    public boolean containsKey(Object key){
        return getEntry(key) != null;
    }

    public boolean containsValue(Object value){
        Entry<K,V> e;
        for (int i =0;i< table.length;++i){
            e = table[i];
            while (e != null){
                if (Objects.equals(value,e.value))  return true;
                e = e.next;
            }
        }
        return false;
    }


    public int size() {
        return size;
    }

    public V remove(Object key) {
        if (size == 0) return null;
        int hash = 0;
        if (key != null) hash = hash(key.hashCode());

        int index = hash & (table.length - 1);
        Entry<K, V> prev = table[index];
        Entry<K, V> cur = prev;

        while (cur != null) {
            Entry<K, V> next = cur.next;
            if (cur.hash == hash && (Objects.equals(key, cur.key))) {
                if (prev == cur)
                    table[index] = next;
                else
                    prev.next = next;
                size--;
                return cur.value;
            }
            prev = cur;
            cur = next;
        }
        return null;
    }

    public void clear(){
        Entry<K,V>[] tab = table;
        if (tab != null && size > 0){
            for (int i=0; i<tab.length; ++i)
                tab[i]=null;
            size =0;
        }
    }

    public boolean isEmpty(){
        return size == 0;
    }

    //Version of remove for EntrySet
    final Entry<K, V> removeMapping(final Object o) {
        if (!(o instanceof Map.Entry))
            return null;
        final Map.Entry<K, V> entry = (Map.Entry<K, V>) o;
        final Object key = entry.getKey();
        final int hash = key == null ? 0 : hash(key.hashCode());
        final int i = hash & (table.length - 1);
        Entry<K, V> prev = table[i];
        Entry<K, V> e = prev;
        while (e != null) {
            final Entry<K, V> next = e.next;
            if (e.hash == hash && e.equals(entry)) {
                size--;
                if (prev == e)
                    table[i] = next;
                else
                    prev.next = next;
                return e;
            }
            prev = e;
            e = next;
        }
        return e;
    }

    
    private abstract class HashIterator<E> implements Iterator<E> {
        Entry<K, V> next;
        int index;
        Entry<K, V> current;

        HashIterator() {
            if (size > 0) {
                final Entry[] t = table;
                do {} while(index < t.length && (next = t[index++]) == null);
            }
        }
        @Override public final boolean hasNext() {
            return next != null;
        }
        final Entry<K, V> nextEntry() {
            final Entry<K, V> e = next;
            if (e == null)
                throw new NoSuchElementException();
            if ((next = e.next) == null) {
                final Entry[] t = table;
                do {} while (index < t.length && (next = t[index++]) == null);
            }
            current = e;
            return e;
        }
        @Override public void remove() {
            if (current == null)
                throw new IllegalStateException();
            final Object k = current.key;
            current = null;
            MyHashMap.this.remove(k);
        }
    }

    final class ValueIterator extends HashIterator<V> {
        @Override public V next() {
            return nextEntry().value;
        }
    }

    final class KeyIterator extends HashIterator<K> {
        @Override public K next() {
            return nextEntry().getKey();
        }
    }

    final class EntryIterator extends HashIterator<MyHashMap.Entry<K, V>> {
        @Override public MyHashMap.Entry<K, V> next() {
            return nextEntry();
        }
    }


    Iterator<K> newKeyIterator() {
        return new KeyIterator();
    }
    Iterator<V> newValueIterator() {
        return new ValueIterator();
    }
    Iterator<MyHashMap.Entry<K, V>> newEntryIterator() {
        return new EntryIterator();
    }


    private Set<MyHashMap.Entry<K, V>> entrySet = null;
    private Set<K> keySet;
    private Collection<V> values;

    public Set<K> keySet() {
        final Set<K> ks = keySet;
        return ks != null ? ks : (keySet = new KeySet());
    }

    final class KeySet extends AbstractSet<K> {
        @Override public Iterator<K> iterator() {
            return newKeyIterator();
        }
        @Override public int size() {
            return size;
        }
        @Override public boolean contains(final Object o) {
            return containsKey(o);
        }
        @Override public boolean remove(final Object o) {
            return MyHashMap.this.remove(o) != null;
        }
        @Override public void clear() {
            MyHashMap.this.clear();
        }
    }



    public Collection<V> values() {
        final Collection<V> vs = values;
        return vs != null ? vs : (values = new Values());
    }

    private final class Values extends AbstractCollection<V> {
        @Override public Iterator<V> iterator() {
            return newValueIterator();
        }
        @Override public int size() {
            return size;
        }
        @Override public boolean contains(final Object o) {
            return containsValue(o);
        }
        @Override public void clear() {
            MyHashMap.this.clear();
        }
    }


    public Set<MyHashMap.Entry<K, V>> entrySet() {
        return entrySet0();
    }
    private Set<MyHashMap.Entry<K, V>> entrySet0() {
        final Set<MyHashMap.Entry<K, V>> es = entrySet;
        return es != null ? es : (entrySet = new EntrySet());
    }

    final class EntrySet extends AbstractSet<MyHashMap.Entry<K, V>> {
        @Override public Iterator<MyHashMap.Entry<K, V>> iterator() {
            return newEntryIterator();
        }
        @Override public boolean contains(final Object o) {
            if (!(o instanceof MyHashMap.Entry))
                return false;
            final MyHashMap.Entry<K, V> e = (MyHashMap.Entry<K, V>) o;
            final Entry<K, V> candidate = getEntry(e.getKey());
            return candidate != null && candidate.equals(e);
        }
        @Override public boolean remove(final Object o) {
            return removeMapping(o) != null;
        }
        @Override public int size() {
            return size;
        }
        @Override public void clear() {
            MyHashMap.this.clear();
        }
    }

}
