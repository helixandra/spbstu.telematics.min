package ru.spbstu.telematics;

public class Pair<K,V> {

    private K key;
    private V value;

    public Pair(K key, V value) {
        assert key != null;
        assert value != null;

        this.key = key;
        this.value = value;
    }

    public K getKey() { return key; }
    public V getValue() { return value; }

    public void setKey(K key) { this.key = key; }
    public void setValue(V value) { this.value = value; }

    @Override
    public int hashCode() { return key.hashCode() ^ value.hashCode(); }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Pair)) return false;
        Pair pairo = (Pair) o;
        return this.key.equals(pairo.getKey()) &&
                this.value.equals(pairo.getValue());
    }

}
