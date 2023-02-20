package zly.rivulet.sql.describer.function;

import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.base.describer.custom.CustomCollector;
import zly.rivulet.base.describer.custom.CustomSingleValueWrap;
import zly.rivulet.base.describer.field.FieldMapping;
import zly.rivulet.base.describer.field.JoinFieldMapping;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.sql.describer.query.SQLQueryMetaDesc;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;

public abstract class MultivariateOperation {

    public abstract BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCollect();

    @SafeVarargs
    public final <F, C> SQLFunction<F, C> of(FieldMapping<F, C> leftValue, FieldMapping<F, C> ... rightValues) {
        LinkedList<SingleValueElementDesc<F, C>> list = new LinkedList<>();
        list.add(leftValue);
        Collections.addAll(list, rightValues);
        return new SQLFunction<F, C>(list) {
            @Override
            public BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCustomCollect() {
                return getCollect();
            }
        };
    }

    @SafeVarargs
    public final <F, C> SQLFunction<F, C> of(FieldMapping<F, C> leftValue, SQLFunction<F, C> ... rightValues) {
        LinkedList<SingleValueElementDesc<F, C>> list = new LinkedList<>();
        list.add(leftValue);
        Collections.addAll(list, rightValues);
        return new SQLFunction<F, C>(list) {
            @Override
            public BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCustomCollect() {
                return getCollect();
            }
        };
    }

    @SafeVarargs
    public final <F, C> SQLFunction<F, C> of(FieldMapping<F, C> leftValue, Param<C> ... rightValues) {
        LinkedList<SingleValueElementDesc<F, C>> list = new LinkedList<>();
        list.add(leftValue);
        for (Param<C> param : rightValues) {
            list.add((SingleValueElementDesc) param);
        }
        return new SQLFunction<F, C>(list) {
            @Override
            public BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCustomCollect() {
                return getCollect();
            }
        };
    }

    @SafeVarargs
    public final <F, C> SQLFunction<F, C> of(FieldMapping<F, C> leftValue, SQLQueryMetaDesc<F, C>... rightValues) {
        LinkedList<SingleValueElementDesc<F, C>> list = new LinkedList<>();
        list.add(leftValue);
        for (SQLQueryMetaDesc<F, C> value : rightValues) {
            list.add((SingleValueElementDesc) value);
        }
        return new SQLFunction<F, C>(list) {
            @Override
            public BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCustomCollect() {
                return getCollect();
            }
        };
    }

    @SafeVarargs
    public final <F, C> SQLFunction<F, C> of(JoinFieldMapping<C> leftValue, JoinFieldMapping<C> ... rightValues) {
        LinkedList<SingleValueElementDesc<F, C>> list = new LinkedList<>();
        list.add((SingleValueElementDesc) leftValue);
        for (JoinFieldMapping<C> value : rightValues) {
            list.add((SingleValueElementDesc) value);
        }
        return new SQLFunction<F, C>(list) {
            @Override
            public BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCustomCollect() {
                return getCollect();
            }
        };
    }

    @SafeVarargs
    public final <F, C> SQLFunction<F, C> of(JoinFieldMapping<C> leftValue, SQLFunction<F, C> ... rightValues) {
        List<SingleValueElementDesc<F, C>> list = new LinkedList<>();
        list.add((SingleValueElementDesc) leftValue);
        for (SQLFunction<F, C> value : rightValues) {
            list.add((SingleValueElementDesc) value);
        }
        return new SQLFunction<F, C>(list) {
            @Override
            public BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCustomCollect() {
                return getCollect();
            }
        };
    }

    @SafeVarargs
    public final <F, C> SQLFunction<F, C> of(JoinFieldMapping<C> leftValue, Param<C> ... rightValues) {
        List<SingleValueElementDesc<F, C>> list = new LinkedList<>();
        list.add((SingleValueElementDesc) leftValue);
        for (Param<C> value : rightValues) {
            list.add((SingleValueElementDesc) value);
        }
        return new SQLFunction<F, C>(list) {
            @Override
            public BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCustomCollect() {
                return getCollect();
            }
        };
    }

    @SafeVarargs
    public final <F, C> SQLFunction<F, C> of(JoinFieldMapping<C> leftValue, SQLQueryMetaDesc<F, C>... rightValues) {
        List<SingleValueElementDesc<F, C>> list = new LinkedList<>();
        list.add((SingleValueElementDesc) leftValue);
        for (SQLQueryMetaDesc<F, C> value : rightValues) {
            list.add((SingleValueElementDesc) value);
        }
        return new SQLFunction<F, C>(list) {
            @Override
            public BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCustomCollect() {
                return getCollect();
            }
        };
    }

    @SafeVarargs
    public final <F, C> SQLFunction<F, C> of(SQLFunction<F, C> leftValue, FieldMapping<F, C> ... rightValues) {
        List<SingleValueElementDesc<F, C>> list = new LinkedList<>();
        list.add((SingleValueElementDesc) leftValue);
        for (FieldMapping<F, C> value : rightValues) {
            list.add((SingleValueElementDesc) value);
        }
        return new SQLFunction<F, C>(list) {
            @Override
            public BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCustomCollect() {
                return getCollect();
            }
        };
    }

    @SafeVarargs
    public final <F, C> SQLFunction<F, C> of(SQLFunction<F, C> leftValue, JoinFieldMapping<C> ... rightValues) {
        List<SingleValueElementDesc<F, C>> list = new LinkedList<>();
        list.add((SingleValueElementDesc) leftValue);
        for (JoinFieldMapping<C> value : rightValues) {
            list.add((SingleValueElementDesc) value);
        }
        return new SQLFunction<F, C>(list) {
            @Override
            public BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCustomCollect() {
                return getCollect();
            }
        };
    }

    @SafeVarargs
    public final <F, C> SQLFunction<F, C> of(SQLFunction<F, C> leftValue, SQLFunction<F, C> ... rightValues) {
        List<SingleValueElementDesc<F, C>> list = new LinkedList<>();
        list.add((SingleValueElementDesc) leftValue);
        for (SQLFunction<F, C> value : rightValues) {
            list.add((SingleValueElementDesc) value);
        }
        return new SQLFunction<F, C>(list) {
            @Override
            public BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCustomCollect() {
                return getCollect();
            }
        };
    }

    @SafeVarargs
    public final <F, C> SQLFunction<F, C> of(SQLFunction<F, C> leftValue, Param<C> ... rightValues) {
        List<SingleValueElementDesc<F, C>> list = new LinkedList<>();
        list.add((SingleValueElementDesc) leftValue);
        for (Param<C> value : rightValues) {
            list.add((SingleValueElementDesc) value);
        }
        return new SQLFunction<F, C>(list) {
            @Override
            public BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCustomCollect() {
                return getCollect();
            }
        };
    }

    @SafeVarargs
    public final <F, C> SQLFunction<F, C> of(SQLFunction<F, C> leftValue, SQLQueryMetaDesc<F, C>... rightValues) {
        List<SingleValueElementDesc<F, C>> list = new LinkedList<>();
        list.add((SingleValueElementDesc) leftValue);
        for (SQLQueryMetaDesc<F, C> value : rightValues) {
            list.add((SingleValueElementDesc) value);
        }
        return new SQLFunction<F, C>(list) {
            @Override
            public BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCustomCollect() {
                return getCollect();
            }
        };
    }

    @SafeVarargs
    public final <F, C> SQLFunction<F, C> of(Param<C> leftValue, FieldMapping<F, C> ... rightValues) {
        List<SingleValueElementDesc<F, C>> list = new LinkedList<>();
        list.add((SingleValueElementDesc) leftValue);
        for (FieldMapping<F, C> value : rightValues) {
            list.add((SingleValueElementDesc) value);
        }
        return new SQLFunction<F, C>(list) {
            @Override
            public BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCustomCollect() {
                return getCollect();
            }
        };
    }

    @SafeVarargs
    public final <F, C> SQLFunction<F, C> of(Param<C> leftValue, JoinFieldMapping<C> ... rightValues) {
        List<SingleValueElementDesc<F, C>> list = new LinkedList<>();
        list.add((SingleValueElementDesc) leftValue);
        for (JoinFieldMapping<C> value : rightValues) {
            list.add((SingleValueElementDesc) value);
        }
        return new SQLFunction<F, C>(list) {
            @Override
            public BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCustomCollect() {
                return getCollect();
            }
        };
    }

    @SafeVarargs
    public final <F, C> SQLFunction<F, C> of(Param<C> leftValue, SQLFunction<F, C> ... rightValues) {
        List<SingleValueElementDesc<F, C>> list = new LinkedList<>();
        list.add((SingleValueElementDesc) leftValue);
        for (SQLFunction<F, C> value : rightValues) {
            list.add((SingleValueElementDesc) value);
        }
        return new SQLFunction<F, C>(list) {
            @Override
            public BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCustomCollect() {
                return getCollect();
            }
        };
    }

    @SafeVarargs
    public final <F, C> SQLFunction<F, C> of(Param<C> leftValue, Param<C> ... rightValues) {
        List<SingleValueElementDesc<F, C>> list = new LinkedList<>();
        list.add((SingleValueElementDesc) leftValue);
        for (Param<C> value : rightValues) {
            list.add((SingleValueElementDesc) value);
        }
        return new SQLFunction<F, C>(list) {
            @Override
            public BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCustomCollect() {
                return getCollect();
            }
        };
    }

    @SafeVarargs
    public final <F, C> SQLFunction<F, C> of(Param<C> leftValue, SQLQueryMetaDesc<F, C>... rightValues) {
        List<SingleValueElementDesc<F, C>> list = new LinkedList<>();
        list.add((SingleValueElementDesc) leftValue);
        for (SQLQueryMetaDesc<F, C> value : rightValues) {
            list.add((SingleValueElementDesc) value);
        }
        return new SQLFunction<F, C>(list) {
            @Override
            public BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCustomCollect() {
                return getCollect();
            }
        };
    }

    @SafeVarargs
    public final <F, C> SQLFunction<F, C> of(SQLQueryMetaDesc<F, C> leftValue, FieldMapping<F, C> ... rightValues) {
        List<SingleValueElementDesc<F, C>> list = new LinkedList<>();
        list.add((SingleValueElementDesc) leftValue);
        for (FieldMapping<F, C> value : rightValues) {
            list.add((SingleValueElementDesc) value);
        }
        return new SQLFunction<F, C>(list) {
            @Override
            public BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCustomCollect() {
                return getCollect();
            }
        };
    }

    @SafeVarargs
    public final <F, C> SQLFunction<F, C> of(SQLQueryMetaDesc<F, C> leftValue, JoinFieldMapping<C> ... rightValues) {
        List<SingleValueElementDesc<F, C>> list = new LinkedList<>();
        list.add((SingleValueElementDesc) leftValue);
        for (JoinFieldMapping<C> value : rightValues) {
            list.add((SingleValueElementDesc) value);
        }
        return new SQLFunction<F, C>(list) {
            @Override
            public BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCustomCollect() {
                return getCollect();
            }
        };
    }

    @SafeVarargs
    public final <F, C> SQLFunction<F, C> of(SQLQueryMetaDesc<F, C> leftValue, SQLFunction<F, C> ... rightValues) {
        List<SingleValueElementDesc<F, C>> list = new LinkedList<>();
        list.add((SingleValueElementDesc) leftValue);
        for (SQLFunction<F, C> value : rightValues) {
            list.add((SingleValueElementDesc) value);
        }
        return new SQLFunction<F, C>(list) {
            @Override
            public BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCustomCollect() {
                return getCollect();
            }
        };
    }

    @SafeVarargs
    public final <F, C> SQLFunction<F, C> of(SQLQueryMetaDesc<F, C> leftValue, Param<C> ... rightValues) {
        List<SingleValueElementDesc<F, C>> list = new LinkedList<>();
        list.add((SingleValueElementDesc) leftValue);
        for (Param<C> value : rightValues) {
            list.add((SingleValueElementDesc) value);
        }
        return new SQLFunction<F, C>(list) {
            @Override
            public BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCustomCollect() {
                return getCollect();
            }
        };
    }

    @SafeVarargs
    public final <F, C> SQLFunction<F, C> of(SQLQueryMetaDesc<F, C> leftValue, SQLQueryMetaDesc<F, C>... rightValues) {
        List<SingleValueElementDesc<F, C>> list = new LinkedList<>();
        list.add((SingleValueElementDesc) leftValue);
        for (SQLQueryMetaDesc<F, C> value : rightValues) {
            list.add((SingleValueElementDesc) value);
        }
        return new SQLFunction<F, C>(list) {
            @Override
            public BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCustomCollect() {
                return getCollect();
            }
        };
    }
}
