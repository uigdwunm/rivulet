# JPAminus
一种简单的jpa实现

// TODO sql长度极限怎么处理


将语句中分成几种元素：
1，关键字部分，不用管
2，as后的别名
3,param部分，可作为参数的，包括 select的左边，where条件的左右两边，order,limit的条件等

as后的别名，要么是字符串常量，要么是结果模型字段映射
param情况较多，可为常量,查询模型字段映射,子查询

单值元素：
1，参数
2，返回单值的查询语句（比如sql中可以用来做为in查询条件的语句）
3，返回单值的函数


查询模型：就是描述哪些表的模型
结果字段模型：就是最终映射查询结果的模型


运行阶段：
1，前置阶段，启动时准备各种definition
2，执行前，
3，执行后，映射

每个查询语句对应一个definition
一个definition有可能对应多个查询方法（参数必须一致）
每次查询对应这个查询方法会生成 statement




明天要做的事

1，测试方法完善下，可以自己传参数的那种。
2，执行方法搞个出来，传入collection
3，convertor转换json类型，就是自带convertor的那种，最好定义到base中
4, 好好整下revuletManager，外层增删改就用这个。
5，转换statement时的缓存还没用，校验也没用，字数统计也没用

明天要做的事
~~1, 将statement缓存放到每个blueprint中。~~
2, 手动输入的看似是desc实际上直接会转换成statement。
3, 转换的statement必须直接代替某个大语句，比如 select、from、groupby、orderby
4, update语句动态的set值，就是那种用查出来的代理对象直接进行更新的，就是通过手动输入实现的，每次调用set方法，实际走了代理，得到了更新的字段。
5, 动态的select方法字段排序，通过手动输入实现
~~6, 针对DOModel的ParamManager~~

把代理模型全都收到一起管理，复用
