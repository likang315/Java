### Joiner & Splitter

------

##### Joiner

- com.google.common.base;
- 用指定字符串，将给定数组、集合连接起来；

```java
@GwtCompatible
    public class Joiner {

        /**
         * 指定分隔符
         */
        private final String separator;

        /**
         * 静态方法，不能实例化
         *
         * @param separator 不能为null
         */
        private Joiner(String separator) {
            this.separator = (String) Preconditions.checkNotNull(separator);
        }

        private Joiner(Joiner prototype) {
            this.separator = prototype.separator;
        }

        /**
         * 获取 Joiner 传入字符串
         *
         * @param separator
         * @return
         */
        public static Joiner on(String separator) {
            return new Joiner(separator);
        }

        /**
         * 获取 Joiner 传入字符
         *
         * @param separator
         * @return
         */
        public static Joiner on(char separator) {
            return new Joiner(String.valueOf(separator));
        }

        // 方法都是非静态的，所以只能通过返回的Joiner调用

        /**
         * Iterable 是所有集合【Map 除外】的父类
         *
         * @param parts
         * @return
         */
        public final String join(Iterable<?> parts) {
            return this.join(parts.iterator());
        }

        public final String join(Iterator<?> parts) {
            return this.appendTo(new StringBuilder(), parts).toString();
        }

        public final String join(Object[] parts) {
            return this.join((Iterable)Arrays.asList(parts));
        }

        /**
         * 先判断是否有下一个元素，然后使用分隔符追加，使其最终值不会有多余的分隔符
         *
         * @param appendable StringBuilder
         * @param parts list
         * @param <A>
         * @return
         * @throws IOException
         */
        public <A extends Appendable> A appendTo(A appendable, Iterator<?> parts)
          throws IOException {
            Preconditions.checkNotNull(appendable);
            if (parts.hasNext()) {
                appendable.append(this.toString(parts.next()));

                while(parts.hasNext()) {
                    appendable.append(this.separator);
                    appendable.append(this.toString(parts.next()));
                }
            }

            return appendable;
        }


        public final StringBuilder appendTo(StringBuilder builder, Iterator<?> parts) {
            try {
                this.appendTo((Appendable)builder, (Iterator)parts);
                return builder;
            } catch (IOException var4) {
                throw new AssertionError(var4);
            }
        }


        /**
         * 将给定的字符串替代集合中的 null 值
         * 调用之后再次调用 useForNull、skipNulls 将会抛出异常
         *
         * @param nullText
         * @return
         */
        @CheckReturnValue
        public Joiner useForNull(final String nullText) {
            Preconditions.checkNotNull(nullText);
            return new Joiner(this) {
                CharSequence toString(@Nullable Object part) {
                    return (CharSequence)(part == null
                                          ? nullText : Joiner.this.toString(part));
                }

                public Joiner useForNull(String nullTextx) {
                    throw new UnsupportedOperationException(
                      "already specified useForNull");
                }

                public Joiner skipNulls() {
                    throw new UnsupportedOperationException(
                      "already specified useForNull");
                }
            };
        }

        /**
         * 将自动跳过 null 元素进行拼接
         * 调用之后再次调用 useForNull、skipNulls 将会抛出异常
         * Joiner.on("#").skipNulls().join(list);
         *
         * @return
         */
        @CheckReturnValue
        public Joiner skipNulls() {
            return new Joiner(this) {
                // join() 内部将调用该appendTo()
                public <A extends Appendable> A appendTo(
                  A appendable, Iterator<?> parts) throws IOException {
                    Preconditions.checkNotNull(appendable, "appendable");
                    Preconditions.checkNotNull(parts, "parts");

                    // 不用if 用 while ，可能第一个值为null
                    Object part;
                    while(parts.hasNext()) {
                        part = parts.next();
                        if (part != null) {
                            appendable.append(Joiner.this.toString(part));
                            break;
                        }
                    }

                    while(parts.hasNext()) {
                        part = parts.next();
                        if (part != null) {
                            appendable.append(Joiner.this.separator);
                            appendable.append(Joiner.this.toString(part));
                        }
                    }

                    return appendable;
                }

                public Joiner useForNull(String nullText) {
                    throw new UnsupportedOperationException(
                      "already specified skipNulls");
                }

                public Joiner.MapJoiner withKeyValueSeparator(String kvs) {
                    throw new UnsupportedOperationException(
                      "can't use .skipNulls() with maps");
                }
            };
        }

        /**
         * 内部类，使 key-value 将通过给定字符串连接
         * Joiner.on(";").withKeyValueSeparator("=").join(map)
         *
         * @param keyValueSeparator
         * @return
         */
        @CheckReturnValue
        public Joiner.MapJoiner withKeyValueSeparator(String keyValueSeparator) {
            return new Joiner.MapJoiner(this, keyValueSeparator);
        }
}
```

##### Splitter

- com.google.common.base;
- 将给定字符串用指定分隔符切割
- Splitter.on("|").omitEmptyStrings().trimResults().split(s2).forEach(System.out::println);

```java
@GwtCompatible(
  emulated = true
)
public final class Splitter {
  /**
	 * 将结果集中的每个字符串前缀和后缀都去除trimmer，直到前缀或后缀没有这个字符
  */
  private final CharMatcher trimmer;

  /**
	 * 是否移除结果集中的空集，true为移除结果集中的空集，false为不用移除结果集中的空集
   */
  private final boolean omitEmptyStrings;

  /**
   * 使用哪种策略模式
   */
  private final com.google.common.base.Splitter.Strategy strategy;

  /**
	 * 最多可将结果集分为几个集合
   */
  private final int limit;

  private Splitter(com.google.common.base.Splitter.Strategy strategy) {
    this(strategy, false, CharMatcher.NONE, 2147483647);
  }

  private Splitter(com.google.common.base.Splitter.Strategy strategy,
                   boolean omitEmptyStrings, CharMatcher trimmer, int limit) {
    this.strategy = strategy;
    this.omitEmptyStrings = omitEmptyStrings;
    this.trimmer = trimmer;
    this.limit = limit;
  }

  public static com.google.common.base.Splitter on(char separator) {
    return on(CharMatcher.is(separator));
  }

  // 静态创建Splitter函数可以按照4类进行分析（接收字符、字符串、正则表达式的和按指定长度分割构造器）

  /**
	 * 接收一个CharMatcher的构造器
   *
   * @param separatorMatcher
   * @return
   */
  public static com.google.common.base.Splitter on(
    final CharMatcher separatorMatcher) {
    Preconditions.checkNotNull(separatorMatcher);
    // 返回一个Splitter对象，传入Strategy对象，并对Strategy接口进行实现
    return new com.google.common.base.Splitter(
      new com.google.common.base.Splitter.Strategy() {
        public com.google.common.base.Splitter.SplittingIterator
          iterator(com.google.common.base.Splitter splitter, CharSequence toSplit) {
          return new com.google.common.base.Splitter.SplittingIterator(splitter, toSplit)
          {
          	// 返回从start开始的第一个分隔符的开始位置
            int separatorStart(int start) {
            return separatorMatcher.indexIn(this.toSplit, start);
          }

          // 返回当前分割符的末尾位置
          int separatorEnd(int separatorPosition) {
            return separatorPosition + 1;
          }
        };
      }
    });
  }

  /**
         * 传入一个字符串作为分隔符
         *
         * @param separator
         * @return
         */
  public static com.google.common.base.Splitter on(final String separator) {
    Preconditions.checkArgument(separator.length() != 0, "The separator may not be the empty string.");
    return new com.google.common.base.Splitter(new com.google.common.base.Splitter.Strategy() {
      public com.google.common.base.Splitter.SplittingIterator iterator(com.google.common.base.Splitter splitter, CharSequence toSplit) {
        return new com.google.common.base.Splitter.SplittingIterator(splitter, toSplit) {
          public int separatorStart(int start) {
            int separatorLength = separator.length();
            int p = start;

            label24:
            for (int last = this.toSplit.length() - separatorLength; p <= last; ++p) {
              for (int i = 0; i < separatorLength; ++i) {
                if (this.toSplit.charAt(i + p) != separator.charAt(i)) {
                  continue label24;
                }
              }

              return p;
            }

            return -1;
          }

          public int separatorEnd(int separatorPosition) {
            return separatorPosition + separator.length();
          }
        };
      }
    });
  }

  /**
   * 接收正则表达式构造器
   *
   * @param separatorPattern
	 * @return
	 */
  @GwtIncompatible("java.util.regex")
  public static com.google.common.base.Splitter on(final Pattern separatorPattern) {
    Preconditions.checkNotNull(separatorPattern);
    Preconditions.checkArgument(!separatorPattern.matcher("").matches(),
                                "The pattern may not match the empty string: %s",
                                new Object[]{separatorPattern});
    // 策略模式
    return new com.google.common.base.Splitter(
      new com.google.common.base.Splitter.Strategy() {
        public com.google.common.base.Splitter.SplittingIterator
          iterator(com.google.common.base.Splitter splitter, CharSequence toSplit) {
          final Matcher matcher = separatorPattern.matcher(toSplit);
          return new com.google.common.base.Splitter.SplittingIterator(splitter, toSplit) 
          {
            public int separatorStart(int start) {
              return matcher.find(start) ? matcher.start() : -1;
            }

            public int separatorEnd(int separatorPosition) {
              return matcher.end();
            }
        };
      }
    });
  }

   /**
  	* 按指定长度分割的构造器
    *
    * @param length
    * @return
    */
  public static com.google.common.base.Splitter fixedLength(final int length) {
    Preconditions.checkArgument(length > 0, "The length may not be less than 1");
    return new com.google.common.base.Splitter(
      new com.google.common.base.Splitter.Strategy() {
        public com.google.common.base.Splitter.SplittingIterator
          iterator(com.google.common.base.Splitter splitter, CharSequence toSplit) {
          return new com.google.common.base.Splitter.SplittingIterator(splitter, toSplit)
          {
            // 跨步 length 长度
            public int separatorStart(int start) {
              int nextChunkStart = start + length;
              return nextChunkStart < this.toSplit.length() ? nextChunkStart : -1;
            }

            public int separatorEnd(int separatorPosition) {
              return separatorPosition;
            }
        };
      }
    });
  }

   /**
    * 切割字符串，返回值为容器的
    *
    * @param sequence
    * @return
    */
  public Iterable<String> split(final CharSequence sequence) {
    Preconditions.checkNotNull(sequence);
    return new Iterable<String>() {
      public Iterator<String> iterator() {
        return com.google.common.base.Splitter.this.splittingIterator(sequence);
      }

      public String toString() {
        return Joiner.on(", ").appendTo(
          (new StringBuilder()).append('['), this).append(']').toString();
      }
    };
  }

	 /**
 		* 根据策略调用相应的构造器
    * @param sequence
    * @return
    */
  private Iterator<String> splittingIterator(CharSequence sequence) {
    return this.strategy.iterator(this, sequence);
  }

	 /**
    * 将最终结果转为不可变List
    *
    * @param sequence
    * @return
    */
  @Beta
  public List<String> splitToList(CharSequence sequence) {
    Preconditions.checkNotNull(sequence);
    Iterator<String> iterator = this.splittingIterator(sequence);
    ArrayList result = new ArrayList();

    while (iterator.hasNext()) {
      result.add(iterator.next());
    }

    return Collections.unmodifiableList(result);
  }

   /**
    * 移去结果中的空字符串, 省略掉空串
    *
    * @return
    */
  @CheckReturnValue
  public com.google.common.base.Splitter omitEmptyStrings() {
    return new com.google.common.base.Splitter(
      this.strategy, true, this.trimmer, this.limit);
  }

   /**
    * 最多切割多少次
    *
    * @param limit
    * @return
    */
  @CheckReturnValue
  public com.google.common.base.Splitter limit(int limit) {
    Preconditions.checkArgument(limit > 0, "must be greater than zero: %s",
                                new Object[]{limit});
    return new com.google.common.base.Splitter(
      this.strategy, this.omitEmptyStrings, this.trimmer, limit);
  }

	 /**
    * 将结果集中每一项前后缀空格删除
    *
    * @return
    */
  @CheckReturnValue
  public com.google.common.base.Splitter trimResults() {
    return this.trimResults(CharMatcher.WHITESPACE);
  }

   /**
    * 将结果集中每一项前后缀指定字符删除
    *
    * @param trimmer
    * @return
    */
  @CheckReturnValue
  public com.google.common.base.Splitter trimResults(CharMatcher trimmer) {
    Preconditions.checkNotNull(trimmer);
    return new com.google.common.base.Splitter(
      this.strategy, this.omitEmptyStrings, trimmer, this.limit);
  }



  @CheckReturnValue
  @Beta
  public com.google.common.base.Splitter.MapSplitter withKeyValueSeparator(
    String separator) {
    return this.withKeyValueSeparator(on(separator));
  }

  @CheckReturnValue
  @Beta
  public com.google.common.base.Splitter.MapSplitter withKeyValueSeparator(
    char separator) {
    return this.withKeyValueSeparator(on(separator));
  }

   /**
    * 将 key-value 通过指定分割符切割
    * 
    * @param keyValueSplitter
    * @return
    */
  @CheckReturnValue
  @Beta
  public com.google.common.base.Splitter.MapSplitter withKeyValueSeparator(
    com.google.common.base.Splitter keyValueSplitter) {
    return new com.google.common.base.Splitter.MapSplitter(this, keyValueSplitter);
  }
  
  private interface Strategy {
    Iterator<String> iterator(com.google.common.base.Splitter var1, CharSequence var2);
  }
}
```

##### CharMatcher

- 处理字符串的类，常用方法分为四类；
- 获取matcher，然后调用方法；

###### 得到匹配指定规则的Matcher

1. CharMatcher is(char match)：
   - 返回匹配指定字符的Matcher
2. CharMatcher isNot(char match)：
   - 返回不匹配指定字符的Matcher
3. CharMatcher anyOf(CharSequence sequence)：
   - 返回匹配sequence中任意字符的Matcher
4. CharMatcher noneOf(CharSequence sequence)：
   - 返回不匹配sequence中任何一个字符的Matcher
5. CharMatcher inRange(char startInclusive, char endInclusive)
   - 返回匹配范围内任意字符的Matcher，26个英文字母顺序
6. CharMatcher and(CharMatcher other)
   - 返回与other匹配条件组合做与来判断Matcher，即取两个Matcher的交集
7. CharMatcher or(CharMatcher other)
   - 返回与other匹配条件组合做或来判断Matcher，即取两个Matcher的并集

###### 判断字符串是否匹配

1. Boolean matchesAnyOf(CharSequence sequence)
   - 只要sequence中有任意字符能匹配Matcher，返回true
2. Boolean matchesAllOf(CharSequence sequence)
   - sequence中所有字符能匹配Matcher，返回true
3. Boolean matchesNoneOf(CharSequence sequence)
   - sequence中所有字符都不匹配Matcher时，返回true

###### 获取字符串与Matcher匹配的位置信息

1. int indexIn(CharSequence sequence):
   - 返回sequence中匹配到的第一个字符的坐标
2. int indexIn(CharSequence sequence, int start)
   - 返回从start开始,在sequence中匹配到的第一个字符的坐标
3. int lastIndexIn(CharSequence sequence)
   - 返回sequence中最后一次匹配到的字符的坐标
4. int countIn(CharSequence sequence)
   - 返回sequence中匹配到的字符计数

###### 对字符串进行怎样匹配处理

1. String removeFrom(CharSequence sequence)
   - 删除sequence中匹配到到的字符并返回
2. String retainFrom(CharSequence sequence) 
   - 保留sequence中匹配到的字符并返回
3. String replaceFrom(CharSequence sequence, char replacement):
   - 替换sequence中匹配到的字符并返回
4. String trimFrom(CharSequence sequence)
   - 删除sequence中首尾匹配到的字符并返回
5. String **collapseFrom**(CharSequence sequence, char replacement)
   - 将匹配到的(连续字符)部分替换成replacement

