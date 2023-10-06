package zly.rivulet.sql.parser.proxy_node;

import zly.rivulet.base.utils.ClassUtils;
import zly.rivulet.base.utils.StringUtil;
import zly.rivulet.sql.definer.meta.SQLFieldMeta;
import zly.rivulet.sql.definer.meta.SQLModelMeta;
import zly.rivulet.sql.definition.query_.mapping.MapDefinition;
import zly.rivulet.sql.parser.SQLAliasManager;

public class MetaModelProxyNode implements FromNode {

    /**
     * 当前节点的别名flag
     **/
    private final SQLAliasManager.AliasFlag aliasFlag;

    private final SQLModelMeta queryFromMeta;

    /**
     * 当前对象的代理对象，一定对应一个表对象
     *
     **/
    private final Object proxyModel;

    MetaModelProxyNode(SQLModelMeta queryFromMeta) {
        Class<?> modelClass = queryFromMeta.getModelClass();
        this.queryFromMeta = queryFromMeta;
        String firstChar = String.valueOf(queryFromMeta.getTableName().charAt(0));
        this.aliasFlag = SQLAliasManager.createAlias(firstChar);
        this.proxyModel = ClassUtils.dynamicProxy(
            modelClass,
            (method, args) -> {
                // 通过方法名解析出字段，获取fieldMeta
                String methodName = method.getName();
                if (!StringUtil.checkGetterMethodName(methodName)) {
                    // 只能解析get开头的方法
                    return;
                }

                String fieldName = StringUtil.parseGetterMethodNameToFieldName(methodName);
                SQLFieldMeta sqlFieldMeta = queryFromMeta.getFieldMetaByFieldName(fieldName);
                SQLAliasManager.AliasFlag fieldAliasFlag = SQLAliasManager.createAlias(sqlFieldMeta.getOriginName());

                THREAD_LOCAL.set(new MapDefinition(sqlFieldMeta, this.aliasFlag, fieldAliasFlag));
            }
        );
    }

    @Override
    public SQLModelMeta getQueryFromMeta() {
        return this.queryFromMeta;
    }

    @Override
    public Object getProxyModel() {
        return this.proxyModel;
    }

    @Override
    public SQLAliasManager.AliasFlag getAliasFlag() {
        return this.aliasFlag;
    }
}
