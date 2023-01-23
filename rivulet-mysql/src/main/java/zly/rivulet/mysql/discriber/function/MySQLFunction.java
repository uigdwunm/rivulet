package zly.rivulet.mysql.discriber.function;

import zly.rivulet.base.describer.custom.CustomCollector;
import zly.rivulet.base.describer.custom.CustomSingleValueWrap;
import zly.rivulet.mysql.discriber.function.cast.CastOperation;
import zly.rivulet.sql.describer.function.BinaryOperation;
import zly.rivulet.sql.describer.function.MultivariateOperation;
import zly.rivulet.sql.describer.function.SQLFunction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.function.BiConsumer;

public interface MySQLFunction {

    interface Arithmetical {
        /**
         * 加法，+
         */
        MultivariateOperation ADD = new MultivariateOperation() {
            @Override
            public BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCollect() {
                return ((customCollector, customSingleValueWraps) -> customCollector.appendAllSeparator(customSingleValueWraps, "+"));
            }
        };

        /**
         * 减法，-
         */
        BinaryOperation SUBTRACT = new BinaryOperation() {
            @Override
            public BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCollect() {
                return ((customCollector, customSingleValueWraps) -> {
                    customCollector.append(customSingleValueWraps.get(0)).append("-").append(customSingleValueWraps.get(1));
                });
            }
        };

        /**
         * 乘法，*
         */
        MultivariateOperation MULTIPLY = new MultivariateOperation() {
            @Override
            public BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCollect() {
                return ((customCollector, customSingleValueWraps) -> customCollector.appendAllSeparator(customSingleValueWraps, "*"));
            }
        };

        /**
         * 除法，/
         */
        BinaryOperation DIVIDE = new BinaryOperation() {
            @Override
            public BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCollect() {
                return ((customCollector, customSingleValueWraps) -> {
                    customCollector.append(customSingleValueWraps.get(0)).append("/").append(customSingleValueWraps.get(1));
                });
            }
        };

        BinaryOperation powToInt = new BinaryOperation() {
            @Override
            public BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCollect() {
                return (customCollector, customSingleValueWraps) -> {
                    customCollector.append("POW(");
                    customCollector.appendAllSeparator(customSingleValueWraps, ",");
                    customCollector.append(")");
                };
            }
        };
    }

    interface Cast {
        static CastOperation<Object> toBinary() {
            return new CastOperation<>("BINARY");
        }

        static CastOperation<Object> toBinary(int length) {
            return new CastOperation<>("BINARY(" + length + ")");
        }

        static CastOperation<String> toChar(int length) {
            return new CastOperation<>("CHAR(" + length + ")");
        }

        static CastOperation<LocalDate> toDate1() {
            return new CastOperation<>("DATE");
        }

        static CastOperation<java.util.Date> toDate2() {
            return new CastOperation<>("DATE");
        }

        static CastOperation<java.util.Date> toDateTime1() {
            return new CastOperation<>("DATETIME");
        }

        static CastOperation<LocalDateTime> toDateTime2() {
            return new CastOperation<>("DATETIME");
        }

        static CastOperation<BigDecimal> toDecimal(int a, int b) {
            return new CastOperation<>("DECIMAL(" + a + "," + b + ")");
        }

        static CastOperation<String> toNchar(int length) {
            return new CastOperation<>("NCHAR(" + length + ")");
        }

        static CastOperation<Object> toSigned(int length) {
            return new CastOperation<>("SIGNED(" + length + ")");
        }

        static CastOperation<LocalTime> toTime() {
            return new CastOperation<>("TIME");
        }

        static CastOperation<Object> toUnSigned(int length) {
            return new CastOperation<>("UNSIGNED(" + length + ")");
        }
    }

    interface Date {
        static <F> SQLFunction<F, java.util.Date> curDateToDate() {
            return new SQLFunction<F, java.util.Date>(Collections.emptyList()) {
                @Override
                public BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCustomCollect() {
                    return (customCollector, customSingleValueWraps) -> {
                        customCollector.append("CURDATE()");
                    };
                }
            };
        }

        static <F> SQLFunction<F, LocalDate> curDateToLocalDate() {
            return new SQLFunction<F, LocalDate>(Collections.emptyList()) {
                @Override
                public BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCustomCollect() {
                    return (customCollector, customSingleValueWraps) -> {
                        customCollector.append("CURDATE()");
                    };
                }
            };
        }

        static <F> SQLFunction<F, LocalDateTime> curDateToLocalDateTime() {
            return new SQLFunction<F, LocalDateTime>(Collections.emptyList()) {
                @Override
                public BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCustomCollect() {
                    return (customCollector, customSingleValueWraps) -> {
                        customCollector.append("CURDATE()");
                    };
                }
            };
        }

    }



}
