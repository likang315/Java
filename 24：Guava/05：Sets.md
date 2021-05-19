##### Sets

------

- Set 的工具类

1. 返回一个新的HashSet;
2. 可变长数组，传入需要添加到集合中元素，返回一个新的容器;
3. 传入一个集合后，根据传入的类型，返回一个新的容器;
4. 传入一个迭代器，返回一个新的List;
5. 返回一个优化后容量的LinkedHashSet；

- difference
  - 传入两个Set，返回set1中set2没有的元素
- union
  -  返回两个set的并集作为视图
- intersection
  - 返回两个set的交集作为视图
- filter
  - 传入一个Set和一个过滤规则，返回一个过滤后的Set

```java
@GwtCompatible(
    emulated = true
)
public final class Sets {
    private Sets() {
    }
  
		// 创建一个HashSet
    public static <E> HashSet<E> newHashSet() {
        return new HashSet();
    }

  	// 传入一个可变长参数，返回一个HashSet
    public static <E> HashSet<E> newHashSet(E... elements) {
      	// 创建一个大小优化的为elements.length的HashSet
        HashSet<E> set = newHashSetWithExpectedSize(elements.length);
      	// 遍历每个元素add
        Collections.addAll(set, elements);
        return set;
    }

  	// 创建一个大小优化的HashSet
    public static <E> HashSet<E> newHashSetWithExpectedSize(int expectedSize) {
        return new HashSet(Maps.capacity(expectedSize));
    }
	
  	// 根据传入的集合创建一个HashSet
    public static <E> HashSet<E> newHashSet(Iterable<? extends E> elements) {
        return elements instanceof Collection ?
          new HashSet(Collections2.cast(elements)) : newHashSet(elements.iterator());
    }

  	// 根据传入的迭代器，迭代每个元素创建一个HashSet
    public static <E> HashSet<E> newHashSet(Iterator<? extends E> elements) {
        HashSet<E> set = newHashSet();
        Iterators.addAll(set, elements);
        return set;
    }

  	// 创建一个 LinkedHashSet 返回
    public static <E> LinkedHashSet<E> newLinkedHashSet() {
        return new LinkedHashSet();
    }

  	// 获取一个大小优化过的的LinkedHashSet
    public static <E> LinkedHashSet<E> newLinkedHashSetWithExpectedSize(
      int expectedSize) {
        return new LinkedHashSet(Maps.capacity(expectedSize));
    }

  	// 根据传入的集合，返回一个LinkedHashSet
    public static <E> LinkedHashSet<E> newLinkedHashSet(Iterable<? extends E> elements) {
        if (elements instanceof Collection) {
            return new LinkedHashSet(Collections2.cast(elements));
        } else {
          	// 创建一个新的集合，迭代放入
            LinkedHashSet<E> set = newLinkedHashSet();
            Iterables.addAll(set, elements);
            return set;
        }
    }
  
		// 返回一个TreeSet
    public static <E extends Comparable> TreeSet<E> newTreeSet() {
        return new TreeSet();
    }

  	// 传入一个集合，返回一个TreeSet，并将集合中的元素赋值给TreeSet
    public static <E extends Comparable> TreeSet<E> newTreeSet(
      Iterable<? extends E> elements) {
        TreeSet<E> set = newTreeSet();
        Iterables.addAll(set, elements);
        return set;
    }

  	// 创建一个根据Comparator的排序的TreeSet
    public static <E> TreeSet<E> newTreeSet(Comparator<? super E> comparator) {
        return new TreeSet((Comparator)Preconditions.checkNotNull(comparator));
    }
  
  	// 传入两个Set，返回set1中set2没有的元素
    public static <E> Sets.SetView<E> difference(final Set<E> set1, final Set<?> set2) {
        Preconditions.checkNotNull(set1, "set1");
        Preconditions.checkNotNull(set2, "set2");
      	// 创建一个过滤规则:不能包含set2中的元素
        final Predicate<Object> notInSet2 = Predicates.not(Predicates.in(set2));
        return new Sets.SetView<E>() {
            public Iterator<E> iterator() {
              	// 过滤掉 notInSet2 中已有的元素
                return Iterators.filter(set1.iterator(), notInSet2);
            }

            public int size() {
                return Iterators.size(this.iterator());
            }
						// 如果set1和set2中全部相等，就为空
            public boolean isEmpty() {
                return set2.containsAll(set1);
            }

            public boolean contains(Object element) {
                return set1.contains(element) && !set2.contains(element);
            }
        };
    }

  	// 返回两个set的并集作为视图
    public static <E> Sets.SetView<E> union(
      final Set<? extends E> set1, final Set<? extends E> set2) {
        Preconditions.checkNotNull(set1, "set1");
        Preconditions.checkNotNull(set2, "set2");
      	// 获取set2中不包含set1的所有元素
        final Set<? extends E> set2minus1 = difference(set2, set1);
        return new Sets.SetView<E>() {
            public int size() {
                return set1.size() + set2minus1.size();
            }

            public boolean isEmpty() {
                return set1.isEmpty() && set2.isEmpty();
            }

            public Iterator<E> iterator() {
                return Iterators.unmodifiableIterator(
                  Iterators.concat(set1.iterator(), set2minus1.iterator()));
            }

            public boolean contains(Object object) {
                return set1.contains(object) || set2.contains(object);
            }

            public <S extends Set<E>> S copyInto(S set) {
                set.addAll(set1);
                set.addAll(set2);
                return set;
            }

            public ImmutableSet<E> immutableCopy() {
                return (new Builder()).addAll(set1).addAll(set2).build();
            }
        };
    }

  	// 获取两个Set的交集作为视图
    public static <E> Sets.SetView<E> intersection(
      final Set<E> set1, final Set<?> set2) {
        Preconditions.checkNotNull(set1, "set1");
        Preconditions.checkNotNull(set2, "set2");
        final Predicate<Object> inSet2 = Predicates.in(set2);
        return new Sets.SetView<E>() {
            public Iterator<E> iterator() {
                return Iterators.filter(set1.iterator(), inSet2);
            }

            public int size() {
                return Iterators.size(this.iterator());
            }

            public boolean isEmpty() {
                return !this.iterator().hasNext();
            }

            public boolean contains(Object object) {
                return set1.contains(object) && set2.contains(object);
            }

            public boolean containsAll(Collection<?> collection) {
                return set1.containsAll(collection) && set2.containsAll(collection);
            }
        };
    }

  	// 传入一个Set和一个过滤规则，返回一个过滤后的Set
    public static <E> Set<E> filter(Set<E> unfiltered, Predicate<? super E> predicate) {
        if (unfiltered instanceof SortedSet) {
            return filter((SortedSet)unfiltered, predicate);
        } else if (unfiltered instanceof Sets.FilteredSet) {
            Sets.FilteredSet<E> filtered = (Sets.FilteredSet)unfiltered;
            Predicate<E> combinedPredicate = Predicates.and(
              filtered.predicate, predicate);
            return new Sets.FilteredSet((Set)filtered.unfiltered, combinedPredicate);
        } else {
            return new Sets.FilteredSet((Set)Preconditions.checkNotNull(
              unfiltered), (Predicate)Preconditions.checkNotNull(predicate));
        }
    }
}
```