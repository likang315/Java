##### Guava 容器

------

##### Lists

- List 的工具类
  1. 返回一个新的ArrayList;
  2. 可变长数组，传入需要添加到集合中元素，返回一个新的容器;
  3. 传入一个集合后，根据传入的类型，返回一个新的结合;
  4. 传入一个迭代器，返回一个新的List;
  5. 返回一个优化后容量的List;
  6. transform
     - 从某个类型的List转换为另一种类型的List，传入一个变change方法;
  7. partition
     - 对List进行分批输出，分页，其实就是截取List;
  8. reverse
     - 对List中的元素进行逆序;

```java
@GwtCompatible(
    emulated = true
)
public final class Lists {
  	// 私有构造函数
    private Lists() {
    }

  	// 直接返回一个新的ArrayList容器
    @GwtCompatible(
        serializable = true
    )
    public static <E> ArrayList<E> newArrayList() {
        return new ArrayList();
    }

  	// 可变长数组，传入需要添加到集合中元素，返回一个新的容器
    @GwtCompatible(
        serializable = true
    )
    public static <E> ArrayList<E> newArrayList(E... elements) {
        Preconditions.checkNotNull(elements);
        int capacity = computeArrayListCapacity(elements.length);
        ArrayList<E> list = new ArrayList(capacity);
        Collections.addAll(list, elements);
        return list;
    }
		
  	// 计算集合的容量，超过最大值、小于最小值是都是最大最小值，否则返回实际容量
    @VisibleForTesting
    static int computeArrayListCapacity(int arraySize) {
        CollectPreconditions.checkNonnegative(arraySize, "arraySize");
        return Ints.saturatedCast(5L + (long)arraySize + (long)(arraySize / 10));
    }

  	// 传入一个集合后，根据传入的类型，返回一个新的结合
    @GwtCompatible(
        serializable = true
    )
    public static <E> ArrayList<E> newArrayList(Iterable<? extends E> elements) {
        Preconditions.checkNotNull(elements);
        return elements instanceof Collection ? new ArrayList(
          Collections2.cast(elements)) : newArrayList(elements.iterator());
    }

    @GwtCompatible(
        serializable = true
    )
  	// 传入一个迭代器，返回一个新的List
    public static <E> ArrayList<E> newArrayList(Iterator<? extends E> elements) {
        ArrayList<E> list = newArrayList();
        Iterators.addAll(list, elements);
        return list;
    }

  	// 返回一个优化后容量的List
    @GwtCompatible(
        serializable = true
    )
    public static <E> ArrayList<E> newArrayListWithExpectedSize(int estimatedSize) {
        return new ArrayList(computeArrayListCapacity(estimatedSize));
    }
  
 // .............................原理同上.............................................

    @GwtCompatible(
        serializable = true
    )
    public static <E> LinkedList<E> newLinkedList() {
        return new LinkedList();
    }

    @GwtCompatible(
        serializable = true
    )
    public static <E> LinkedList<E> newLinkedList(Iterable<? extends E> elements) {
        LinkedList<E> list = newLinkedList();
        Iterables.addAll(list, elements);
        return list;
    }

		// 从某个类型的List转换为另一种类型的List，传入一个变change方法
    public static <F, T> List<T> transform(List<F> fromList,
                                           Function<? super F, ? extends T> function) {
        return (List)(fromList instanceof RandomAccess
                      ? new Lists.TransformingRandomAccessList(fromList, function)
                      : new Lists.TransformingSequentialList(fromList, function));
    }

  	// 对List进行分批输出，分页，其实就是截取List
    public static <T> List<List<T>> partition(List<T> list, int size) {
        Preconditions.checkNotNull(list);
        Preconditions.checkArgument(size > 0);
        return (List)(list instanceof RandomAccess
                      ? new Lists.RandomAccessPartition(list, size)
                      : new Lists.Partition(list, size));
    }
	
  	// 对List中的元素进行逆序
    public static <T> List<T> reverse(List<T> list) {
        if (list instanceof ImmutableList) {
            return ((ImmutableList)list).reverse();
        } else if (list instanceof Lists.ReverseList) {
            return ((Lists.ReverseList)list).getForwardList();
        } else {
            return (List)(list instanceof RandomAccess
                          ? new Lists.RandomAccessReverseList(list)
                          : new Lists.ReverseList(list));
        }
    }

  	// 其他方法List原生代码足以
}

```

##### Sets

```java
@GwtCompatible(
    emulated = true
)
public final class Sets {
    private Sets() {
    }

  	// 创建一个不可变的Set
    @GwtCompatible(
        serializable = true
    )
    public static <E extends Enum<E>> ImmutableSet<E> immutableEnumSet(
      E anElement, E... otherElements) {
      	// 将一个元素和其他元素插入到一个Set中
        return ImmutableEnumSet.asImmutable(EnumSet.of(anElement, otherElements));
    }
	
  	// 根据一个集合创建一个不可变的Set
    @GwtCompatible(
        serializable = true
    )
    public static <E extends Enum<E>> ImmutableSet<E> immutableEnumSet(
      Iterable<E> elements) {
        if (elements instanceof ImmutableEnumSet) {
            return (ImmutableEnumSet)elements;
        } else if (elements instanceof Collection) {
            Collection<E> collection = (Collection)elements;
          	// 如果是一个Collection且不为空，则直接使用ImmutableEnumSet.asImmutable方法转化为ImmutableEnumSet
            return collection.isEmpty()
              ? ImmutableSet.of()
              : ImmutableEnumSet.asImmutable(EnumSet.copyOf(collection));
        } else {
          	// 获取他的迭代器，然后制作一个ImmutableEnumSet
            Iterator<E> itr = elements.iterator();
            if (itr.hasNext()) {
                EnumSet<E> enumSet = EnumSet.of((Enum)itr.next());
                Iterators.addAll(enumSet, itr);
                return ImmutableEnumSet.asImmutable(enumSet);
            } else {
                return ImmutableSet.of();
            }
        }
    }

		// 创建一个HsshSet
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

  	// .创建一个大小优化的HashSet
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

    public static <E> Sets.SetView<E> union(
      final Set<? extends E> set1, final Set<? extends E> set2) {
        Preconditions.checkNotNull(set1, "set1");
        Preconditions.checkNotNull(set2, "set2");
        final Set<? extends E> set2minus1 = difference(set2, set1);
        return new Sets.SetView<E>() {
            public int size() {
                return set1.size() + set2minus1.size();
            }

            public boolean isEmpty() {
                return set1.isEmpty() && set2.isEmpty();
            }

            public Iterator<E> iterator() {
                return Iterators.unmodifiableIterator(Iterators.concat(set1.iterator(), set2minus1.iterator()));
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

  	// 传入两个Set，返回一个以两个Set的互不重叠的部分作为视图
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

            public boolean isEmpty() {
                return set2.containsAll(set1);
            }

            public boolean contains(Object element) {
                return set1.contains(element) && !set2.contains(element);
            }
        };
    }

    public static <E> Sets.SetView<E> symmetricDifference(
      Set<? extends E> set1, Set<? extends E> set2) {
        Preconditions.checkNotNull(set1, "set1");
        Preconditions.checkNotNull(set2, "set2");
        return difference(union(set1, set2), intersection(set1, set2));
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

