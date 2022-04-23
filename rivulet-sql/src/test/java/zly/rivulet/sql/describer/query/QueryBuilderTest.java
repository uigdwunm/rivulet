package zly.rivulet.sql.describer.query;

import org.junit.Test;
import zly.rivulet.sql.describer.query.builder.SelectBuilder;
import zly.rivulet.sql.model.Student;
import zly.rivulet.sql.model.User;

public class QueryBuilderTest {

    @Test
    public void query() {
        SqlQueryMetaDesc<User, User> query = QueryBuilder.query(User.class, User.class).build();
        SelectBuilder<User, Student> query1 = QueryBuilder.query(query, Student.class);
    }

    @Test
    public void testQuery() {
    }

    @Test
    public void build() {
    }
}