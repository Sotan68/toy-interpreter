package interpreter.model.adt;

import interpreter.exceptions.ADTException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyList<T> implements IList<T> {
    private final List<T> list = new ArrayList<>();

    @Override
    public void add(T v) {
        list.add(v);
    }

    @Override
    public T get(int index) throws ADTException {
        if (index < 0 || index >= list.size()) {
            throw new ADTException("index " + index + " out of bounds for list of size " + list.size());
        }
        return list.get(index);
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public List<T> getList() {
        return Collections.unmodifiableList(list);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (T item : list) {
            if (!first) {
                sb.append(", ");
            }
            sb.append(item);
            first = false;
        }
        sb.append("}");
        return sb.toString();
    }
}
