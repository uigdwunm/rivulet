package zly.rivulet.sql.definer.meta;

import zly.rivulet.sql.definition.query.operate.OperateDefinition;

import java.util.List;

/**
 * Description 连表查询对象
 *
 * @author zhaolaiyuan
 * Date 2022/4/3 11:21
 **/
public class JoinQueryMeta {

    private QueryFromMeta from;

    private JoinRelation leftJoinRelation;

    private JoinRelation rightJoinRelation;

    private JoinRelation innerJoinRelation;

    public class JoinRelation {

        private QueryFromMeta joinModel;

        private List<OperateDefinition> operateDefinitionList;

    }
}
