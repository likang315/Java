package tag08;

import java.text.MessageFormat;
import java.util.Date;

public class Test {

	public static void main(String[] args) {
		String str=" Welcome,{0},{1}!";
		String newstr=MessageFormat.format(str, "ÀîËÄ",new Date());
        System.out.println(newstr);
	}

}
