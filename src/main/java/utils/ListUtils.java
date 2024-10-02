package utils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;

public class ListUtils {

    public static Integer sumUpInt(final List<Integer> list) {
        return list.stream().reduce(Integer::sum).orElse(0);
    }

    public static BigDecimal sumUpBd(final List<BigDecimal> list) {
        return CollectionUtils.emptyIfNull(list).stream()
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
