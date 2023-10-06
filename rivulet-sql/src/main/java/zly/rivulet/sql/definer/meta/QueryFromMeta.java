package zly.rivulet.sql.definer.meta;

import zly.rivulet.base.definition.Definition;
import zly.rivulet.sql.parser.SQLAliasManager;

/**
 * Description 可作为查询数据源的元数据信息，包括联表查询，单表meta，子查询
 *
 * @author zhaolaiyuan
 * Date 2022/4/4 14:34
 **/
public interface QueryFromMeta extends Definition {

    SQLAliasManager.AliasFlag getAliasFlag();
}
