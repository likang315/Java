package com.pojo;

import java.math.BigDecimal;

/**
 * 商品信息
 * @author likang
 *
 */
public class Goods {
	 private int id;
	 private String	name;
	 private BigDecimal	price;
	 private int total;
	 private String	pic;
	 private int counter;  //买了几个
 
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public BigDecimal getPrice() {
			return price;
		}
		public void setPrice(BigDecimal price) {
			this.price = price;
		}
		public int getTotal() {
			return total;
		}
		public void setTotal(int total) {
			this.total = total;
		}
		public String getPic() {
			return pic;
		}
		public void setPic(String pic) {
			this.pic = pic;
		}
		public int getCounter() {
			return counter;
		}
		public void setCounter(int counter) {
			this.counter = counter;
		}
		 
}
