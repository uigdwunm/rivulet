# rivulet（半成品预览版）
## 特性预览
### 1、查询
#### 准备表对象（必须有getter方法，这里节省篇幅省略）
```java
@SqlTable("t_province")
public class ProvinceDO {

    @PrimaryKey
    @SqlColumn("code")
    @MySQLInt
    private Integer code;

    @SqlColumn
    @MySQLVarchar(length = 16)
    @Comment("省份名称")
    private String name;
}
```
``` java
@SqlTable("t_city")
public class CityDO {

    @PrimaryKey
    @SqlColumn("code")
    @MySQLInt
    private Integer code;

    @SqlColumn
    @MySQLVarchar(length = 16)
    @Comment("城市名称")
    private String name;

    @SqlColumn("province_code")
    @MySQLInt
    @Comment("所属省份code")
    private Integer provinceCode;
}
```
#### 查询演示
```java
public class QueryTest {
  /**
   * 预编写查询逻辑
   **/
  @RivuletDesc("queryProvince")
  public WholeDesc queryProvince() {
    // 从参数中动态取值
    Param<Integer> provinceCodeParam = Param.of(Integer.class, "provinceCode", ParamCheckType.NATURE);
    // 查询语句
    return QueryBuilder.query(ProvinceDO.class, ProvinceDO.class)
            .where(Condition.equalTo(CheckCondition.notNull(provinceCodeParam), ProvinceDO::getCode, provinceCodeParam))
            .build();
  }

  /**
   * 执行查询
   **/
  public void query() {
    Rivulet rivulet = createDefaultRivuletManager().getRivulet();
    HashMap<String, Object> paramMap = new HashMap<>();
    // 参数
    paramMap.put("provinceCode", 123);
    // 查询，通过预编写的查询语句方法上的注解@RivuletDesc的key找到语句。
    ProvinceDO provinceDO = rivulet.queryOneByDescKey("queryProvince", paramMap);
  }
  /**
   * 预先生成管理器
   **/
  public RivuletManager createDefaultRivuletManager() {
    DefaultWarehouseManager defaultWarehouseManager = new DefaultWarehouseManager("zly.rivulet.mysql");
    return new DefaultMySQLDataSourceRivuletManager(
      new MySQLRivuletProperties(),
      new ConvertorManager(),
      defaultWarehouseManager,
      createDataSource()
      );
  }
}
```

#### 查询映射原DO模型语句
```java
public class Desc {
    @RivuletDesc("queryProvince")
    public WholeDesc queryProvince() {
        // 从参数中动态取值
        Param<Integer> provinceCodeParam = Param.of(Integer.class, "provinceCode", ParamCheckType.NATURE);
        // 查询
        return QueryBuilder.query(ProvinceDO.class, ProvinceDO.class)
            .where(Condition.equalTo(CheckCondition.notNull(provinceCodeParam), ProvinceDO::getCode, provinceCodeParam))
            .build();
    }
}
```
+ 通过QueryBuilder开始编写sql语句
  + query的第一个对象是From的表对象，第二个对象是查询结果映射
  + 这里查询对象是ProvinceDO，映射对象也是ProvinceDO，可以设置成不同的，但是要手动映射select

#### 联表查询
准备一个联表模型，用于描述表之间的连接关系
```java
/**
 * 联表对象需要继承QueryComplexModel
 **/
public class CityProvinceJoin implements QueryComplexModel {

    private CityDO cityDO;

    private ProvinceDO provinceDO;

    @Override
    public ComplexDescriber register() {
        ComplexDescriber describer = ComplexDescriber.from(cityDO);

        // 联表条件
        describer.leftJoin(provinceDO).on(JoinCondition.equalTo(provinceDO::getCode, cityDO::getProvinceCode));

        return describer;
    }
}
```
联表查询语句
```java
public class Desc {
    @RivuletDesc("queryCityProvinceJoin")
    public WholeDesc queryCityProvinceJoin() {
        // 从参数中动态取值
        Param<Integer> cityCodeParam = Param.of(Integer.class, "cityCode", ParamCheckType.NATURE);
        // query的from对象就是上面的联表模型
        return QueryBuilder.query(CityProvinceJoin.class, CityProvinceJoin.class)
            .where(
                Condition.equalTo(x -> x.getCityDO().getCode(), cityCodeParam)
            ).build();
    }
}
```
#### 映射不同的查询结果对象
```java
public class Desc {
    @RivuletDesc("queryCityProvinceJoin2")
    public WholeDesc queryCityProvinceJoin2() {
        // 从参数中动态取值
        Param<Integer> cityCodeParam = Param.of(Integer.class, "cityCode", ParamCheckType.NATURE);
        // 使用不同的对象映射结果
        return QueryBuilder.query(CityProvinceJoin.class, CityInfo.class)
            .select(
                Mapping.of(CityInfo::setCityCode, x -> x.getCityDO().getCode()),
                Mapping.of(CityInfo::setCityName, x -> x.getCityDO().getName()),
                Mapping.of(CityInfo::setProvinceCode, x -> x.getProvinceDO().getCode()),
                Mapping.of(CityInfo::setProvinceName, x -> x.getProvinceDO().getName())
            )
            .where(Condition.equalTo(x -> x.getCityDO().getCode(), cityCodeParam))
            .build();
    }
}
```

> 就这些吧，目前还是个半成品，再多的东西没有了。  
> 先放出来给大家看看，觉得还行就点点star，希望为我下次跳槽加点分。

## 愿景
+ 现在有三个包
  + rivulet-base
    + rivulet-sql
      + rivulet-mysql
+ 包分层从上到下，mysql层可以换成其他类sql的中间件，sql层可以换成mongo、es之类的用语句查询的
+ 从包分层上来看，很显然我希望统一持久层，所有存储类框架都用一种方式调用。

## 关于持久层
+ 我这里定义的持久层，可能不是广泛的定义
  + 是所有存储类的中间件，包括但不限于mysql、mongo、es之类的拥有存储能力的东西。
  + 通过拥有通过特定语法语句进行调用的能力。

## 主要能力
+ 基于上面对这类中间件功能的定义，使用流程一定可以归类成这些：
  + 拼语句
  + 发送到中间件，接受返回结果
  + 映射结果为模型、map、list之类的
+ 至于其他的一些能力，会保留很多扩展点进行支持，比如事物、连接管理等
+ 不同中间件调用时，最重要的差异性就是不同的语句语法，所以我的框架最主要也是最强的能力，就是拼语句。

## 没有缓存
+ mybatis是有缓存的，但是我这里没有。
+ 在单机的时代，缓存是会有非常大的优势，但是现在都是集群，感觉缓存已经有点鸡肋了。
+ 目前没考虑，但是我留下了足够的扩展点，相加也能加。

## 设计演进
+ 第一阶段
  + 一开始我只是在开发过程中使用了mybatis，觉得每次写语句都比较麻烦，还总是写错。
  + 觉得hibernate那种映射很方便，但是联表查询又很麻烦。
  + 于是想要自己设计一种方式把他们结合起来。
+ 第二阶段
  + 当我在设计我设想中的框架时，第一点想到的，也是最重要的定位就是方便拼语句。
  + 性能方面，语句必须预先编译好，易于拼接参数，拼好语句后，其余的由连接池操心。
  + mybatis-plus
    + 当我开始设计调研时，在github上随便查了一下，看到了mybatis-plus
    + 其实它有一部分可以满足我的需要了,但是我逐步发现mybatis-plus还是支持的不够，拼语句的能力局限较大。
    + 并且mybatis-plus只是mybatis的一个插件，有一定的局限性。
    + 当然，说了这么多，主要还是觉得我的设计更好。
+ 第三阶段
  + 当我把拼语句作为框架的主要定位时，有一天我突然想到，查sql是拼语句，查mongo也是拼语句，查es也是拼语句。
  + 那完全可以把他们统一起来，所以变成了现在的模块分包。
+ 第四阶段
  + sql、mongo、es这些存储类的中间件，有一些通用的查询能力，比如：
    + equal字段
    + in字段
    + \>
    + <
    + like
    + ……
  + 这种简单的单表查询，以及单表的增删改，是绝大部分的使用场景。
  + 那么完全可以在base层用统一的格式来屏蔽掉这种检查查询的底层语句。
    + 对使用者来说可以用同一种格式，查询sql、mongo、es。
  + 实际上随着现在微服务的流行，表越拆越细、简单的单表查询已经成为常态。

## 画饼
+ 最大的饼，就是有一天能统一持久层，用同一种语法调用所有的存储中间件。
+ 运行时
  + 如果你们公司有个类似db管理的平台。
  + 我通过扩展接口留了很多口子，理论上可以在运行过程中调整这些。
  + 所有的语句都是以definition为基础的，通过预定义的key映射
    + 理论上可以在程序运行时修改definition，重新映射到key，也就是修改语句。
    + 当然映射结果不能变。
    + 参数也是固定的没法变。
+ 编译时
  + 定义器中收集了表的ddl元数据，
    + 理论上可以在编译阶段，就对语句进行分析，判断有没有走索引，或者其他什么。
  + 我把sql语句解构的非常细，每个元素都能独立获取，所以解析语句很容易。

## 半成品
+ 这个框架经过几番改进、推倒重头再来，又因为我的拖延症，历经了一年多快两年，终于成为一个半成品。
+ mysql
  + 我是以mysql的语法支持来实现第一个版本的
  + 后来发现想要相对完整的支持sql，真的太难了。
    + sql的联表查询、子查询嵌套、别名、各种类型，还有函数都太变态了。
  + 好在现在已经解决了这些，目前的半成品已经初步可以展示出我的框架的强大之处。
+ 目前还是个半成品，不过暂时没时间完善了，先放出来给大家看看，帮忙支持下点点star，希望为我下次跳槽加点分。
+ 坦白来说，饼画的太大，我一个人有点搞不定了。
  + 虽然各个组件已经跑通了基本逻辑，但是很多细节没填充，操作符、函数之类的只实现了一两个。
  + 包括很多少见的类型，或者我一时没想起来的东西还没支持。
+ 我自己写了几个测试方法，勉强能运行，肯定还都是bug，先看着，我慢慢改。

## 寄语
+ 希望以后有公司接盘，让我全职维护最好了🤣
+ 不做梦了，先活下来再说。
+ 我闷头开发了快两年，写代码越来越提不起劲，也是因为没有反馈。
+ 虽然我一直觉得东西很不错，但都是自嗨，没人真正看到 
+ 有些细节我觉得我设计的非常nb，希望有一天有人能发现。
+ 现在还是个半成品放出来，也是因为自嗨太久没动力了。
+ 点个star是对我的鼓励，希望大家支持一下。
