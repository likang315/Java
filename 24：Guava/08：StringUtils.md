### StringUtils

------

##### 1：前提条件

```xml
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-lang3</artifactId>
    <version>3.9</version>
</dependency>
```

##### 2：源码剖析

1. 是否为空的判断
2. 是否为空白串的判断
3. 移除输入字符序列的首尾空白字符。如果输入字符序列为null，则返回null
4. 不区分大小写比较两个字符串是否相等
5. 忽略大小写，匹配查找字符串

```java
package org.apache.commons.lang3;

public class StringUtils {
    private static final int STRING_BUILDER_SIZE = 256;
 		// 空白字符串
    public static final String SPACE = " ";
 		// 空字符串
    public static final String EMPTY = "";
  	// 换行
    public static final String LF = "\n";
  	// 回车
    public static final String CR = "\r";
  	// 字符串查找未找到时返回的值
    public static final int INDEX_NOT_FOUND = -1;
    private static final int PAD_LIMIT = 8192;
		
  	// 判断输入的字符序列是否为空，当字符序列为null或者长度为0时返回true
    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }
		// 判空取反
    public static boolean isNotEmpty(CharSequence cs) {
        return !isEmpty(cs);
    }

  	// 判断一个字符序列数组中是否有任意一个为空
    public static boolean isAnyEmpty(CharSequence... css) {
        if (ArrayUtils.isEmpty(css)) {
            return false;
        } else {
            CharSequence[] var1 = css;
            int var2 = css.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                CharSequence cs = var1[var3];
                if (isEmpty(cs)) {
                    return true;
                }
            }

            return false;
        }
    }
  
		// 判断字符序列数组是否都不为空
    public static boolean isNoneEmpty(CharSequence... css) {
        return !isAnyEmpty(css);
    }

		// 判断所以字符序列是否为空
    public static boolean isAllEmpty(CharSequence... css) {
        if (ArrayUtils.isEmpty(css)) {
            return true;
        } else {
            CharSequence[] var1 = css;
            int var2 = css.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                CharSequence cs = var1[var3];
                if (isNotEmpty(cs)) {
                    return false;
                }
            }

            return true;
        }
    }
		
  	// 判断输入字符序列是否为空白，所谓的空白字符串包括三种情况
  	// 空串,null,只包含空白字符的串
    public static boolean isBlank(CharSequence cs) {
        int strLen;
        if (cs != null && (strLen = cs.length()) != 0) {
            for(int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(cs.charAt(i))) {
                    return false;
                }
            }
            return true;
        } else {
            return true;
        }
    }
		// 判断输入字符序列是否非空白
    public static boolean isNotBlank(CharSequence cs) {
        return !isBlank(cs);
    }
		
  	// 判断输入的字符序列数组是否有任意一个为空白
    public static boolean isAnyBlank(CharSequence... css) {
        if (ArrayUtils.isEmpty(css)) {
            return false;
        } else {
            CharSequence[] var1 = css;
            int var2 = css.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                CharSequence cs = var1[var3];
                if (isBlank(cs)) {
                    return true;
                }
            }

            return false;
        }
    }
		
  	// 判断输入的字符序列数组是否都不为空白
    public static boolean isNoneBlank(CharSequence... css) {
        return !isAnyBlank(css);
    }
		
  	// 判断是否所有都为空白串
    public static boolean isAllBlank(CharSequence... css) {
        if (ArrayUtils.isEmpty(css)) {
            return true;
        } else {
            CharSequence[] var1 = css;
            int var2 = css.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                CharSequence cs = var1[var3];
                if (isNotBlank(cs)) {
                    return false;
                }
            }

            return true;
        }
    }

  	// 移除输入字符序列的首尾空白字符。如果输入字符序列为null，则返回null
    public static String trim(String str) {
        return str == null ? null : str.trim();
    }
		// 移除字符序列首尾空白字符，若移除后的字符序列为空（""或者null），则返回null，否则返回移除后字符串
    public static String trimToNull(String str) {
        String ts = trim(str);
        return isEmpty(ts) ? null : ts;
    }
  	
  	// 不区分大小写比较两个字符串是否相等
    public static boolean equalsIgnoreCase(CharSequence cs1, CharSequence cs2) {
        if (cs1 == cs2) {
            return true;
        } else if (cs1 != null && cs2 != null) {
            return cs1.length() != cs2.length() 
              ? false
              : CharSequenceUtils.regionMatches(cs1, true, 0, cs2, 0, cs1.length());
        } else {
            return false;
        }
    }

		// 忽略大小写，匹配查找字符串
    public static int indexOfIgnoreCase(CharSequence str, CharSequence searchStr) {
        return indexOfIgnoreCase(str, searchStr, 0);
    }
		// 返回找到的下标值
    public static int indexOfIgnoreCase(CharSequence str, CharSequence searchStr,
                                        int startPos) {
        if (str != null && searchStr != null) {
            if (startPos < 0) {
                startPos = 0;
            }
						// 可能被查找字符串长度小于查找串
            int endLimit = str.length() - searchStr.length() + 1;
            if (startPos > endLimit) {
                return -1;
            // 查找串为空串时，返回的为0 可能和两个字符串一样时冲突
            } else if (searchStr.length() == 0) {
                return startPos;
            } else {
                for(int i = startPos; i < endLimit; ++i) {
                    if (CharSequenceUtils
                        .regionMatches(str, true, i, searchStr, 0, searchStr.length())) {
                        return i;
                    }
                }

                return -1;
            }
        } else {
            return -1;
        }
    }
}
```

