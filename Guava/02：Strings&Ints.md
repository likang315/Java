### Strings&Ints

------

##### Strings【不用】

- com.google.common.base
- 主要可分为三类；

```java
public final class Strings {
    // 第一类：对字符串为 null 或为空的处理
    private Strings() {
    }
		/**
		 * 如果string为null则返回空字符串，否则返回给定的string
		 */
    public static String nullToEmpty(@Nullable String string) {
        return string == null ? "" : string;
    }

  	/**
  	 * 如果字符串为空或null时，返回null，否则返回给定字符串
		 */
    @Nullable
    public static String emptyToNull(@Nullable String string) {
        return isNullOrEmpty(string) ? null : string;
    }
		
    /**
    * 如果字符串为空或null时，返回true，否则返回false
    */
    public static boolean isNullOrEmpty(@Nullable String string) {
        return string == null || string.length() == 0;
    }
   
  	// 生成指定字符串的字符串副本
  
  	/**
  	* 根据传入的minLength进行补充，如果minLength小于原来字符串的长度，则直接返回原来字符串，否则在字符		 * 串开头添加string.length()-minLength个padChar字符
		*/
    public static String padStart(String string, int minLength, char padChar) {
        Preconditions.checkNotNull(string);
        if (string.length() >= minLength) {
            return string;
        } else {
            StringBuilder sb = new StringBuilder(minLength);

            for(int i = string.length(); i < minLength; ++i) {
                sb.append(padChar);
            }
						// 后 append 旧串
            sb.append(string);
            return sb.toString();
        }
    }

  	/**
  	* 根据传入的minLength进行补充，如果minLength小于原来字符串的长度，则直接返回原来字符串，否则在字符		 * 串结尾添加string.length()-minLength个padChar字符
  	*/
    public static String padEnd(String string, int minLength, char padChar) {
        Preconditions.checkNotNull(string);
        if (string.length() >= minLength) {
            return string;
        } else {
            StringBuilder sb = new StringBuilder(minLength);
          	// 先 append 旧串
            sb.append(string);

            for(int i = string.length(); i < minLength; ++i) {
                sb.append(padChar);
            }

            return sb.toString();
        }
    }
		
  	/**
  	* 返回 count 个 string 字符串拼接成的字符串
  	*/
    public static String repeat(String string, int count) {
        Preconditions.checkNotNull(string);
        if (count <= 1) {
            Preconditions.checkArgument(
              count >= 0, "invalid count: %s", new Object[]{count});
            // 注意
            return count == 0 ? "" : string;
        } else {
            int len = string.length();
            long longSize = (long)len * (long)count;
            int size = (int)longSize;
            if ((long)size != longSize) {
                throw new ArrayIndexOutOfBoundsException((
                  new StringBuilder(51)).append(
                  "Required array size too large: ").append(longSize).toString());
            } else {
              	// 字符串的本质就是数组
                char[] array = new char[size];
                string.getChars(0, len, array, 0);
                int n;
              	// << 带符号左移，这样只需要copy count次就行，而且省掉一次for循环
                for(n = len; n < size - n; n <<= 1) {
                    System.arraycopy(array, 0, array, n, n);
                }
                System.arraycopy(array, 0, array, n, size - n);
              
                return new String(array);
            }
        }
    }

    // 查找两个字符串的公共前缀和后缀
    /**
    * 返回a和b两个字符串的公共前缀
    */
    public static String commonPrefix(CharSequence a, CharSequence b) {
        Preconditions.checkNotNull(a);
        Preconditions.checkNotNull(b);
        int maxPrefixLength = Math.min(a.length(), b.length());

        int p;
        for(p = 0; p < maxPrefixLength && a.charAt(p) == b.charAt(p); ++p) {
        }

        if (validSurrogatePairAt(a, p - 1) || validSurrogatePairAt(b, p - 1)) {
            --p;
        }

        return a.subSequence(0, p).toString();
    }

  	/**
  	* 返回字符串a和字符串b的公共后缀
  	*/
    public static String commonSuffix(CharSequence a, CharSequence b) {
        Preconditions.checkNotNull(a);
        Preconditions.checkNotNull(b);
        int maxSuffixLength = Math.min(a.length(), b.length());
				// sffafaf
      	//    afaf
      	// 从后往前遍历得到不等时的字符串flag数
        int s;
        for(s = 0; s < maxSuffixLength &&
            a.charAt(a.length() - s - 1) == b.charAt(b.length() - s - 1); ++s) {
        }

        if (validSurrogatePairAt(a, a.length() - s - 1) || validSurrogatePairAt(b, b.length() - s - 1)) {
            --s;
        }
				// 剪的牛逼~
        return a.subSequence(a.length() - s, a.length()).toString();
    }

   /**
  	* 判断最后两个字符是不是合法的“Java 平台增补字符
  	*/
  	@VisibleForTesting
    static boolean validSurrogatePairAt(CharSequence string, int index) {
        return index >= 0 && index <= string.length() - 2 &&
          Character.isHighSurrogate(string.charAt(index)) && 
          Character.isLowSurrogate(string.charAt(index + 1));
    }
}
```

##### Ints

- com.google.common.primitives;
- int 的工具类：包含Int数组的工具操作，

```java
@GwtCompatible(
    emulated = true
)
public final class Ints {
    public static final int BYTES = 4;
    public static final int MAX_POWER_OF_TWO = 1073741824;
    private static final byte[] asciiDigits = new byte[128];
		/**
		 * 静态方法，单例模式，只能作为工具类
		 */
    private Ints() {
    }
		
  	/**
  	 * 检查给定值是否超出int值范围
  	 */
    public static int checkedCast(long value) {
        int result = (int)value;
        if ((long)result != value) {
            throw new IllegalArgumentException((
              new StringBuilder(34)).append("Out of range: ").append(value).toString());
        } else {
            return result;
        }
    }

  	/**
  	 * 如果a<b返回-1, 如果a>b返回1, 如果a=b返回0
  	 */
    public static int compare(int a, int b) {
        return a < b ? -1 : (a > b ? 1 : 0);
    }

  	/**
  	 * 如果array中存在target返回true，反之返回false
  	 */
    public static boolean contains(int[] array, int target) {
        int[] arr$ = array;
        int len$ = array.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            int value = arr$[i$];
            if (value == target) {
                return true;
            }
        }

        return false;
    }
		
    /**
     * 获取target在数组中首次出现的的下表，若返回-1，没找到
     */
    public static int indexOf(int[] array, int target) {
        return indexOf(array, target, 0, array.length);
    }

    private static int indexOf(int[] array, int target, int start, int end) {
        for(int i = start; i < end; ++i) {
            if (array[i] == target) {
                return i;
            }
        }

        return -1;
    }
		
  	/**
  	 * 获取target在数组中从后往前首次出现的下表
     */
    public static int lastIndexOf(int[] array, int target) {
        return lastIndexOf(array, target, 0, array.length);
    }

    private static int lastIndexOf(int[] array, int target, int start, int end) {
        for(int i = end - 1; i >= start; --i) {
            if (array[i] == target) {
                return i;
            }
        }

        return -1;
    }

  	/**
  	 * 获取多个参数中的最小值
  	 */
    public static int min(int... array) {
        Preconditions.checkArgument(array.length > 0);
        int min = array[0];
				// 可变长参数其实就是数组
        for(int i = 1; i < array.length; ++i) {
            if (array[i] < min) {
                min = array[i];
            }
        }

        return min;
    }
		
  	/**
  	 * 获取多个参数中的最大值
  	 */
    public static int max(int... array) {
        Preconditions.checkArgument(array.length > 0);
        int max = array[0];

        for(int i = 1; i < array.length; ++i) {
            if (array[i] > max) {
                max = array[i];
            }
        }

        return max;
    }

		/**
  	 * 返回由指定数组支持的固定大小的列表，类似Arrays.asList(Object[]),非常常用
  	 */
    public static List<Integer> asList(int... backingArray) {
        return (List)(backingArray.length == 0 ? 
                      Collections.emptyList() : new Ints.IntArrayAsList(backingArray));
    }
}
```

















