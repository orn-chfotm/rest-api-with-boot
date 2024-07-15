package com.learn.restapiwithboot.core.query;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class QueryDslUtil {
    public OrderSpecifier<?>[] orderSpecifiers(Pageable pageable, Class<?> classType, String variable) {
        return pageable.getSort().stream().map(
                order -> {
                    String property = order.getProperty();
                    OrderSpecifier<?> orderSpecifier = new OrderSpecifier(
                            order.isAscending() ? Order.ASC : Order.DESC,
                            new PathBuilder<>(classType, variable).get(property)
                    );
                    return orderSpecifier;
                }
        ).toArray(OrderSpecifier[]::new);
    }
}
