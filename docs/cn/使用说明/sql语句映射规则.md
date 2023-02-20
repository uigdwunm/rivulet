# SQL语句编写规则
## 1、概述
+ 整体原则是类似原生语句的格式。

### 测试方法
```java
class Test {
    /**
     * RivuletManager对象中专门预留了一个方法用于测试编写的desc是否符合预期。
     * testParse()，得到一个fish
     * 通过工具类拿到fish中的语句MySQLPrintUtils。
     **/
    public void test(RivuletManager rivuletManager) {
       Fish fish = rivuletManager.testParse(queryProvince());
       String statement = MySQLPrintUtils.commonPrint(fish);
       System.out.println(statement);
    }
}

```

## 2、构造详解
准备两个表对象
```java

@SQLTable("t_province")
public class ProvinceDO {

   @PrimaryKey
   @SQLColumn("code")
   @MySQLInt
   private Integer code;

   @SQLColumn
   @MySQLVarchar(length = 16)
   @Comment("省份名称")
   private String name;

   public Integer getCode() {
      return code;
   }

   public void setCode(Integer code) {
      this.code = code;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }
}
```
```java

@SQLTable("t_city")
public class CityDO {

   @PrimaryKey
   @SQLColumn("code")
   @MySQLInt
   private Integer code;

   @SQLColumn
   @MySQLVarchar(length = 16)
   @Comment("城市名称")
   private String name;

   @SQLColumn("province_code")
   @MySQLInt
   @Comment("所属省份code")
   private Integer provinceCode;

   public Integer getCode() {
      return code;
   }

   public void setCode(Integer code) {
      this.code = code;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public Integer getProvinceCode() {
      return provinceCode;
   }

   public void setProvinceCode(Integer provinceCode) {
      this.provinceCode = provinceCode;
   }
}
```
1. 首先用SQLQueryBuilder开启一个sqlDesc的构造。
2. 通过IDE提示引出第一个query方法。
   1. query方法包含两个参数。
   2. 第一个参数表示from的表模型对象class，如果是[联表查询](#联表查询)，则是[联表查询](#联表查询)的对象模型。
   3. 第二个参数表示select结果的模型对象class，就是查询完成的结果，封装到哪个对象中。
   4. from模型和select模型可以是同一个，模型相同时可以不指定select字段映射结果，默认返回全部字段。
```java
class Demo {
   public WholeDesc query() {
      // select * from t_province
      return SQLQueryBuilder.query(ProvinceDO.class, ProvinceDO.class).build();
   }
}
```
3. query完成后，是select方法，可以指定返回哪些字段。
   1. 如果select模型和from模型不一致，则必须指定select方法。
   2. 方法传入的是Mapping的变长数组，直接用Mapping方法of引出。
   3. Mapping.of传入的第一个参数是，select模型的set方法，查出结果后会调用这个set方法设置参数，第二个参数是from模型的get方法，对应到字段（必须是get方法，is开头的也不识别，会通过get方法名定位字段名，必须一致）
   4. 第二个参数其实并不限于字段，我们实际使用中可能会有sql函数，甚至子查询在select字段中，这些统称为[singleValue](#singleValue)
```java
class Demo {
   public WholeDesc query() {
      // select code, name from t_province
      return SQLQueryBuilder.query(ProvinceDO.class, ProvinceDO.class)
              .select( 
                  Mapping.of(ProvinceDO::setCode, ProvinceDO::getCode), 
                  Mapping.of(ProvinceDO::setName, ProvinceDO::getName)
              ).build();
   }
}
```
4. where方法，如果需要查询条件则要指定where方法。
```java
class Demo {
   public WholeDesc query() {
      // select code, name from t_province where code = code
      return SQLQueryBuilder.query(ProvinceDO.class, ProvinceDO.class)
              .select( 
                  Mapping.of(ProvinceDO::setCode, ProvinceDO::getCode), 
                  Mapping.of(ProvinceDO::setName, ProvinceDO::getName)
              ).where( 
                  Condition.Equal.of(CheckCondition.notNull(provinceCodeParam), ProvinceDO::getCode, ProvinceDO::getCode)
              ).build();
   }
}
```

### 联表查询
示例：
```java
public class CityProvinceJoin implements QueryComplexModel {

   private CityDO cityDO;

   private ProvinceDO provinceDO;

   /**
    * 下面方法会得到这个语句
    *    from t_city c left join t_province p on p.code = c.province_code
    **/
   @Override
   public ComplexDescriber register() {
       ComplexDescriber describer = ComplexDescriber.from(cityDO);

       // 联表条件
       describer.leftJoin(provinceDO).on(JoinCondition.Equal.of(provinceDO::getCode, cityDO::getProvinceCode));

       return describer;
   }

   public CityDO getCityDO() {
       return cityDO;
   }

   public ProvinceDO getProvinceDO() {
       return provinceDO;
   }
}
```
1. 先建立一个联表描述类,需要继承QueryComplexModel。
2. 将需要联表的表，记录为成员变量
3. 在register方法中，用ComplexDescriber来描述多个模型的联表关系
4. 首先必须存在的from对象，这里不像SQLQueryBuilder一样传入表模型class对象，而是传入成员变量表对象。
5. 然后继续调用不同的join来描述连接类型。

### singleValue
在sql的语法中，无论是select的子项，还是where的子项，都可以是多种形态的值，我大致分了四类：
1. 表字段
2. 函数
3. 子查询
4. 参数

这四类就是singleValue的四种类型，示例：
```java
class Demo {
   public WholeDesc query() {
      // 字段类型singleValue
      // select code from t_province where code = code
      return SQLQueryBuilder.query(ProvinceDO.class, ProvinceDO.class)
              .select(Mapping.of(ProvinceDO::setCode, ProvinceDO::getCode))
              .where(Condition.Equal.of(ProvinceDO::getCode, ProvinceDO::getCode))
              .build();
   }
}
```
```java
class Demo {
   public WholeDesc query() {
      // 函数类型singleValue
      // select code + code from t_province where code = code + code
      return SQLQueryBuilder.query(ProvinceDO.class, ProvinceDO.class)
              .select( 
                  Mapping.of(ProvinceDO::setCode, MySQLFunction.Arithmetical.ADD.of(ProvinceDO::getCode, ProvinceDO::getCode))
              ).where(
                  Condition.Equal.of(ProvinceDO::getCode, MySQLFunction.Arithmetical.ADD.of(ProvinceDO::getCode, ProvinceDO::getCode))
              ).build();
   }
}
```
```java
class Demo {
   public WholeDesc query() {
      // 子查询类型singleValue
      // select code + code from t_province where code = code + code
      return SQLQueryBuilder.query(ProvinceDO.class, ProvinceDO.class) 
              .select(
                  Mapping.of(
                      ProvinceDO::setCode,
                      SQLQueryBuilder.query(ProvinceDO.class, Integer.class)
                          .selectOne(ProvinceDO::getCode)
                          .limit(Param.staticOf(1))
                          .build()
                      ),
                      Mapping.of(ProvinceDO::setName, ProvinceDO::getName)
              ).where(
                  Condition.Equal.of(
                      ProvinceDO::getCode,
                      SQLQueryBuilder.query(ProvinceDO.class, Integer.class)
                      .selectOne(ProvinceDO::getCode)
                      .limit(Param.staticOf(1))
                      .build()
                  )
              ).build();
   }
}
```
```java
class Demo {
   public WholeDesc query() {
      // 参数类型singleValue
      // select code + code from t_province where code = code + code
      return SQLQueryBuilder.query(ProvinceDO.class, ProvinceDO.class)
              .select(
                      Mapping.of(ProvinceDO::setCode, MySQLFunction.Arithmetical.ADD.of(ProvinceDO::getCode, ProvinceDO::getCode))
              ).where(
                      Condition.Equal.of(ProvinceDO::getCode, MySQLFunction.Arithmetical.ADD.of(ProvinceDO::getCode, ProvinceDO::getCode))
              ).build();
   }
   @RivuletDesc("queryProvince")
   public WholeDesc queryProvince() {
       // select 112, name from t_province where code = #{province.code}
       return SQLQueryBuilder.query(ProvinceDO.class, ProvinceDO.class) 
               .select(
                    Mapping.of(ProvinceDO::setCode, Param.staticOf(112)),
                    Mapping.of(ProvinceDO::setName, ProvinceDO::getName)
              ).where(
                    Condition.Equal.of(ProvinceDO::getCode, Param.of(Integer.class, "province.code"))
              ).build();
   }
}
```
