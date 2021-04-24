##### Guava 容器

------

##### Lists

- List 的工具类
  1. 返回一个新的ArrayList;
  2. 可变长数组，传入需要添加到集合中元素，返回一个新的容器;
  3. 传入一个集合后，根据传入的类型，返回一个新的容器;
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

