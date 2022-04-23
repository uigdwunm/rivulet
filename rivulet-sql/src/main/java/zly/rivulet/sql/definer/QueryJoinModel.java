package zly.rivulet.sql.definer;

import zly.rivulet.base.exception.ModelDefineException;
import zly.rivulet.sql.describer.join.FromModelJoinDescriber;
import zly.rivulet.sql.describer.join.FromSubQueryJoinDescriber;
import zly.rivulet.sql.describer.join.JoinDescriber;
import zly.rivulet.sql.describer.query.SqlQueryMetaDesc;

public interface QueryJoinModel {

    <F> void register(JoinGuide joinGuide);

    class JoinGuide {
        private JoinDescriber<?> joinDescriber;

        public <F> JoinDescriber<F> fromMode(Class<F> modelFrom) {
            if (joinDescriber != null) {
                throw ModelDefineException.multiMainFromMode();
            }
            FromModelJoinDescriber<F> fromModelJoinDescriber = JoinDescriber.mainFrom(modelFrom);
            this.joinDescriber = fromModelJoinDescriber;
            return fromModelJoinDescriber;
        }

        public <F> JoinDescriber<F> fromSubQuery(SqlQueryMetaDesc<?, F> subQueryFrom) {
            if (joinDescriber != null) {
                throw ModelDefineException.multiMainFromMode();
            }
            FromSubQueryJoinDescriber<F> fromSubQueryJoinDescriber = JoinDescriber.subQueryFrom(subQueryFrom);
            this.joinDescriber = fromSubQueryJoinDescriber;
            return fromSubQueryJoinDescriber;
        }

    }
}
