package zly.rivulet.sql.definition.query.main;

import zly.rivulet.base.definition.AbstractContainerDefinition;
import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.base.describer.field.SelectMapping;
import zly.rivulet.base.mapper.MapDefinition;
import zly.rivulet.sql.definition.query.mapping.MappingDefinition;
import zly.rivulet.sql.describer.query.desc.Mapping;
import zly.rivulet.sql.exception.SQLDescDefineException;
import zly.rivulet.sql.preparser.helper.SqlPreParseHelper;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Description
 * 有两种情况，
 * 1，传了mappedItemList，则按照传的走。
 * 2，没传，则手动补齐所有from对象的
 *
 * @author zhaolaiyuan
 * Date 2022/6/5 20:12
 **/
public class SelectDefinition extends AbstractContainerDefinition {

    private final Class<?> selectModel;

    private final List<MappingDefinition> mappingDefinitionList;

//    protected SelectDefinition() {
//        super(CheckCondition.IS_TRUE);
//    }

    public SelectDefinition(SqlPreParseHelper sqlPreParseHelper, FromDefinition fromDefinition, Class<?> selectModel, List<? extends Mapping.Item<?, ?, ?>> mappedItemList) {
        super(CheckCondition.IS_TRUE, sqlPreParseHelper.getSqlParamDefinitionManager());
        this.selectModel = selectModel;
        if (mappedItemList == null || mappedItemList.isEmpty()) {
            // 比较select对象和from对象必须是同一个才能进行补齐。
            if (!selectModel.equals(fromDefinition.getFromMode())) {
                throw SQLDescDefineException.selectAndFromNoMatch();
            }
            // 需要按名称映射，先把所有的set方法都找到
            Set<Method> setMethodSet = Arrays.stream(selectModel.getMethods())
                .filter(method -> method.getName().startsWith("set"))
                .collect(Collectors.toSet());

            // 找到所有对应的field


            // TODO 需要考虑联表查询结果的映射怎么搞

            SelectMapping<?, ?> selectMapping = new SelectMapping() {
                @Override
                public void setMapping(Object o, Object o2) {

                }
            };

        } else {
            this.mappingDefinitionList = mappedItemList.stream()
                .map(item -> new MappingDefinition(sqlPreParseHelper, item.getDesc(), item.getSelectField()))
                .collect(Collectors.toList());
        }
    }

    @Override
    public SelectDefinition forAnalyze() {
        return null;
    }

    @Override
    public ArrayList<?> getList() {
        return null;
    }
}