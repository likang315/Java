### Maps

------

- map 的工具类
  1. 返回一个新的HashMap;
  2. 可变长数组，传入需要添加到集合中元素，返回一个新的容器;
  3. 传入一个集合后，根据传入的类型，返回一个新的容器;
  4. 传入一个迭代器，返回一个新的HashMap;
  5. 返回一个优化后容量的HashMap；
- filterKeys
  - 使用keyPredicate函数接口制定过滤规则，对Map进行过滤
- filterValues
  - 用valuePredicate函数接口制定过滤规则，对Map进行过滤
- 用entryPredicate函数接口制定过滤规则，对Map进行过滤
  - 用entryPredicate函数接口制定过滤规则，对Map进行过滤
- fromProperties
  - 从配置文件中读取数据，创建一个不可变的Map

```java
@GwtCompatible(
    emulated = true
)
public final class Maps {
    static final MapJoiner STANDARD_JOINER;

    private Maps() {
    }

		// 直接返回一个新的HashMap
    public static <K, V> HashMap<K, V> newHashMap() {
        return new HashMap();
    }

  	// 返回一个长度优化过的的HashMap
    public static <K, V> HashMap<K, V> newHashMapWithExpectedSize(int expectedSize) {
        return new HashMap(capacity(expectedSize));
    }

  	// 传入一个Map变量，返回一个HashMap
    public static <K, V> HashMap<K, V> newHashMap(Map<? extends K, ? extends V> map) {
        return new HashMap(map);
    }

  	// 返回一个LinkedHashMap
    public static <K, V> LinkedHashMap<K, V> newLinkedHashMap() {
        return new LinkedHashMap();
    }

  	// 传入一个Map变量，返回一个HashMap
    public static <K, V> LinkedHashMap<K, V> newLinkedHashMap(
      Map<? extends K, ? extends V> map) {
        return new LinkedHashMap(map);
    }
  
		// 直接返回一个新的TreeMap
    public static <K extends Comparable, V> TreeMap<K, V> newTreeMap() {
        return new TreeMap();
    }
		// 传入一个Map变量，返回一个TreeMap，并将Map的值赋值给TreeMap
    public static <K, V> TreeMap<K, V> newTreeMap(SortedMap<K, ? extends V> map) {
        return new TreeMap(map);
    }
	
  	// 传入指定排序器的 TreeMap
    public static <C, K extends C, V> TreeMap<K, V> newTreeMap(
      @Nullable Comparator<C> comparator) {
        return new TreeMap(comparator);
    }

  	// 使用keyPredicate函数接口制定过滤规则，对Map进行过滤
    public static <K, V> Map<K, V> filterKeys(Map<K, V> unfiltered,
                                              Predicate<? super K> keyPredicate) {
        if (unfiltered instanceof SortedMap) {
          	// 如果这个Map属于SortedMap，则交给其他的重载方法进行处理
            return filterKeys((SortedMap)unfiltered, keyPredicate);
        } else if (unfiltered instanceof BiMap) {
            return filterKeys((BiMap)unfiltered, keyPredicate);
        } else {
            Preconditions.checkNotNull(keyPredicate);
            Predicate<Entry<K, ?>> entryPredicate = keyPredicateOnEntries(keyPredicate);
            return (Map)(unfiltered instanceof Maps.AbstractFilteredMap
                         ? filterFiltered((
                           Maps.AbstractFilteredMap)unfiltered, entryPredicate)
                         : new Maps.FilteredKeyMap((Map)Preconditions.checkNotNull(
                           unfiltered), keyPredicate, entryPredicate));
        }
    }

  	// 用valuePredicate函数接口制定过滤规则，对Map进行过滤
    public static <K, V> Map<K, V> filterValues(Map<K, V> unfiltered, 
                                                Predicate<? super V> valuePredicate) {
        if (unfiltered instanceof SortedMap) {
            return filterValues((SortedMap)unfiltered, valuePredicate);
        } else {
            return (Map)(unfiltered instanceof BiMap 
                         ? filterValues((BiMap)unfiltered, valuePredicate)
                         : filterEntries(unfiltered, valuePredicateOnEntries(
                           valuePredicate)));
        }
    }

  	// 用entryPredicate函数接口制定过滤规则，对Map进行过滤
    public static <K, V> Map<K, V> filterEntries(
      Map<K, V> unfiltered, Predicate<? super Entry<K, V>> entryPredicate) {
        if (unfiltered instanceof SortedMap) {
            return filterEntries((SortedMap)unfiltered, entryPredicate);
        } else if (unfiltered instanceof BiMap) {
            return filterEntries((BiMap)unfiltered, entryPredicate);
        } else {
            Preconditions.checkNotNull(entryPredicate);
            return (Map)(unfiltered instanceof Maps.AbstractFilteredMap
                         ? filterFiltered((Maps.AbstractFilteredMap)unfiltered,
                                          entryPredicate)
                         : new Maps.FilteredEntryMap(
                           (Map)Preconditions.checkNotNull(unfiltered), entryPredicate));
        }
    }
  
  // 从配置文件中读取数据，创建一个不可变的Map
  public static ImmutableMap<String, String> fromProperties(Properties properties) {
    Builder builder = ImmutableMap.builder();
    Enumeration e = properties.propertyNames();
    // 从properties获取的key和value，赋值到builder中
    while(e.hasMoreElements()) {
        String key = (String)e.nextElement();
        builder.put(key, properties.getProperty(key));
    }
    // 返回一个不可变的Map
    return builder.build();
	}

}
```

