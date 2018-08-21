// The MIT License
//
// Copyright (c) 2004 Evren Sirin
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to
// deal in the Software without restriction, including without limitation the
// rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
// sell copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
// FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
// IN THE SOFTWARE.

/*
 * Created on Dec 29, 2004
 */
package impl.owl;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * @author Evren Sirin
 *
 */
public class WrappingList implements List {
    private List baseList = null;

    public WrappingList(List baseList) {
        this.baseList = baseList;
    }
    
    public List getBaseList() {
        return baseList;
    }

    public boolean equals(Object obj) {
        if(obj == null) return false;

        if(!(obj instanceof WrappingList)) 
            return false;

        WrappingList other = (WrappingList) obj;

        return baseList.equals(other.baseList);
    }

    public int size() {
        return baseList.size();
    }

    public boolean isEmpty() {
        return baseList.isEmpty();
    }

    public boolean contains(Object o) {
        return baseList.contains(o);
    }

    public Iterator iterator() {
        return baseList.iterator();
    }

    public Object[] toArray() {
        return baseList.toArray();
    }

    public Object[] toArray(Object a[]) {
        return baseList.toArray(a);
    }

    public boolean add(Object o) {
        return baseList.add(o);
    }

    public boolean remove(Object o) {
        return baseList.remove(o);
    }

    public boolean containsAll(Collection c) {
        return baseList.containsAll(c);
    }

    public boolean addAll(Collection c) {
        return baseList.addAll(c);
    }

    public boolean addAll(int index, Collection c) {
        return baseList.addAll(index, c);
    }

    public boolean removeAll(Collection c) {
        return baseList.removeAll(c);
    }

    public boolean retainAll(Collection c) {
        return baseList.retainAll(c);
    }

    public void clear() {
        baseList.clear();
    }

    public int hashCode() {
        return baseList.hashCode();
    }

    public Object get(int index) {
        return baseList.get(index);
    }

    public Object set(int index, Object element) {
        return baseList.set(index, element);
    }

    public void add(int index, Object element) {
        baseList.add(index, element);
    }

    public Object remove(int index) {
        return baseList.remove(index);
    }

    public int indexOf(Object o) {
        return baseList.indexOf(o);
    }

    public int lastIndexOf(Object o) {
        return baseList.lastIndexOf(o);
    }

    public ListIterator listIterator() {
        return baseList.listIterator();
    }

    public ListIterator listIterator(int index) {
        return baseList.listIterator(index);
    }

    public List subList(int fromIndex, int toIndex) {
        List list = baseList.subList(fromIndex, toIndex);
        List result = new WrappingList(list);
        return result;
    }

    public String toString() {
        return baseList.toString();
    }
}

