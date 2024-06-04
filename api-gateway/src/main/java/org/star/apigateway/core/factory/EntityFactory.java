package org.star.apigateway.core.factory;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.springframework.data.relational.core.mapping.Table;
import org.star.apigateway.core.entity.user.UserAuth;

import java.util.UUID;

public interface EntityFactory<T> {
    public T toNew();

    T toNew(@NotNull String newId);

    T toExists(@NotNull String existsEntityId);
}
