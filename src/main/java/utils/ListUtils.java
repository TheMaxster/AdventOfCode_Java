package utils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;

public class ListUtils {

    public static Integer sumUpInt(final List<Integer> list) {
        return list.stream().reduce(Integer::sum).orElse(0);
    }

    public static Long sumUpLong(final List<Long> list) {
        return list.stream().reduce(Long::sum).orElse(0L);
    }

    public static Integer multiplyUpInt(final List<Integer> list) {
        int product = 1;
        for (final int number : list) {
            product *= number;
        }
        return product;
    }

    public static BigDecimal sumUpBd(final List<BigDecimal> list) {
        return CollectionUtils.emptyIfNull(list).stream()
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
