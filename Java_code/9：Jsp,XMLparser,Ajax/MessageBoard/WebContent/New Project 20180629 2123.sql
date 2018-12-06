-- MySQL Administrator dump 1.4
--
-- ------------------------------------------------------
-- Server version	5.7.16


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


--
-- Create schema askbook
--

CREATE DATABASE IF NOT EXISTS askbook;
USE askbook;

--
-- Definition of table `admin`
--

DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `email` varchar(45) NOT NULL,
  `upwd` varchar(65) NOT NULL,
  `pic` varchar(65) DEFAULT NULL,
  `name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `admin`
--

/*!40000 ALTER TABLE `admin` DISABLE KEYS */;
INSERT INTO `admin` (`id`,`email`,`upwd`,`pic`,`name`) VALUES 
 (1,'admin@qq.com','21232f297a57a5a743894a0e4a801fc3','20180629195846603_177.jpg','管理员'),
 (2,'qaq@qqcom','563ba72ccf2593689f6d215b93eb48b3','20180629202526187_538.jpg','æ¸£æ¸£è¾'),
 (3,'123@qq.cim','202cb962ac59075b964b07152d234b70','20180629202731133_650.jpg','æ¸£æ¸£è¾'),
 (4,'123@qq.com','202cb962ac59075b964b07152d234b70','20180629205004143_305.jpg','渣渣辉'),
 (5,'lihua@qq.com','a95aa4e62b22c9bc5bca4e83cadfaa82','','lihua'),
 (6,'lihua@qq.com','a95aa4e62b22c9bc5bca4e83cadfaa82','20180629210229849_590.jpg','lihua'),
 (7,'Huoyani@qq.com','d2895945dbba0c9c46c9cca1d635593f','20180629210242924_553.jpg','huoyani'),
 (8,'zh@qq.com','e10adc3949ba59abbe56e057f20f883e','20180629210315850_88.jpg','fxyb'),
 (9,'ys@qq.com','25f9e794323b453885f5181f1b624d0b','20180629210332730_625.jpg','yss'),
 (10,'fth@qq.com','202cb962ac59075b964b07152d234b70','','fth'),
 (11,'wanghua@qq.com','e10adc3949ba59abbe56e057f20f883e','20180629210354586_919.jpg','王花花'),
 (12,'Anfield@qq.com','e10adc3949ba59abbe56e057f20f883e','20180629210449781_375.jfif','Anfield'),
 (13,'123@123.com','202cb962ac59075b964b07152d234b70','20180629210441444_889.png','advs'),
 (14,'123456789@qq.com','e10adc3949ba59abbe56e057f20f883e','20180629210456316_565.png','耿潇'),
 (15,'1654@qq.com','202cb962ac59075b964b07152d234b70','20180629210530835_435.jpg','小小鸟'),
 (16,'haijun@qq.com','48787852d88d2c2616083338134b14ff','20180629210533163_849.jpg','落花流水'),
 (17,'123456@qq.com','202cb962ac59075b964b07152d234b70','','LX');
/*!40000 ALTER TABLE `admin` ENABLE KEYS */;


--
-- Definition of table `msg`
--

DROP TABLE IF EXISTS `msg`;
CREATE TABLE `msg` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `admin_id` int(10) unsigned DEFAULT NULL,
  `title` varchar(45) NOT NULL,
  `txt` text,
  `createtime` datetime DEFAULT NULL,
  `visit` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `msg`
--

/*!40000 ALTER TABLE `msg` DISABLE KEYS */;
INSERT INTO `msg` (`id`,`admin_id`,`title`,`txt`,`createtime`,`visit`) VALUES 
 (1,1,'ç¬¬ä¸æ¡çè¨ï¼','ç¬¬ä¸æ¡çè¨ï¼ç<em>ç</em>è½å¦<strong>æå</strong>','2018-06-29 20:46:16',NULL),
 (2,1,'第二条','试度','2018-06-29 20:49:00',NULL),
 (3,1,'第三条留言','<p>\r\n	第三是加是是是国j\r\n</p>\r\n<p>\r\n	是是\r\n</p>','2018-06-29 20:50:23',NULL),
 (4,4,'大家好，我是渣渣辉','','2018-06-29 20:50:36',NULL),
 (5,4,'大家好，我是渣渣辉','大家好，我是渣渣辉','2018-06-29 20:50:59',NULL),
 (6,1,'第四条','<p>\r\n	第四条jlsajflsf\r\n</p>\r\n<p>\r\n	ssf奇才\r\n</p>\r\n<p>\r\n	<br />\r\n</p>','2018-06-29 20:51:24',NULL),
 (7,1,'第五条，，，','<p>\r\n	第五条。。。\r\n</p>\r\n<p>\r\n	昌\r\n</p>\r\n<p>\r\n	<br />\r\n</p>','2018-06-29 20:54:11',NULL),
 (8,1,'第五条，，，','<p>\r\n	第五条。。。\r\n</p>\r\n<p>\r\n	昌\r\n</p>\r\n<p>\r\n	<br />\r\n</p>','2018-06-29 20:54:15',NULL),
 (9,1,'标杆','顶替要','2018-06-29 20:55:29',NULL),
 (10,1,'aaaaaaaaaaaa','<p>\r\n	JSDLFKJDSF DSF\r\n</p>\r\n<p>\r\n	SFJSF;\r\n</p>','2018-06-29 20:56:25',NULL),
 (11,1,'ffffffffffff','fffffffffffff','2018-06-29 20:58:10',NULL),
 (12,1,'jjlskjflsdf','dslfdsjfsdl','2018-06-29 21:00:41',NULL),
 (13,4,'大家好，我是渣渣辉','大家好，我是渣渣辉','2018-06-29 21:01:07',NULL),
 (14,5,'','啦啦啦啦啦啦啦啦<br />','2018-06-29 21:02:59',NULL),
 (15,5,'','啦啦啦啦啦啦啦啦<br />','2018-06-29 21:03:09',NULL),
 (16,10,'位移是个猪','是的没错','2018-06-29 21:04:14',NULL),
 (17,7,'jiandang95zhounian','jiangdang95zhounianjinianri&nbsp; jiangjiahe<br />','2018-06-29 21:04:23',NULL),
 (18,8,'有没有延安的大佬','听说在延安某县出现了灭霸，是不是很叼？','2018-06-29 21:04:31',NULL),
 (19,10,'位移是个猪','是的没错','2018-06-29 21:04:50',NULL),
 (20,9,'请不要输入问题','<div align=\"center\">\r\n	<ul>\r\n		<li>\r\n			请不要输入你的<strong>问题</strong><em>！！</em>\r\n		</li>\r\n	</ul>\r\n</div>','2018-06-29 21:05:39',NULL),
 (21,11,'你好','<span style=\"background-color:#337FE5;\">啦啦啦，徐媛你渴不？</span><span style=\"background-color:#FF9900;\"><span style=\"background-color:#FF9900;\"></span></span>','2018-06-29 21:05:42',NULL),
 (22,13,'啊哈','啊哈','2018-06-29 21:05:45',NULL),
 (23,14,'大家好','一年','2018-06-29 21:05:57',NULL);
/*!40000 ALTER TABLE `msg` ENABLE KEYS */;




/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
