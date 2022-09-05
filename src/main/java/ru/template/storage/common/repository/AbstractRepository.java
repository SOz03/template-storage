package ru.template.storage.common.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.template.storage.common.entity.BaseEntity;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.UUID;

@NoRepositoryBean
public interface AbstractRepository<E extends BaseEntity> extends PagingAndSortingRepository<E, UUID>, JpaSpecificationExecutor<E> {

    default Class<E> getEntityClass() {
        Type[] interfaces = getClass().getInterfaces();

        for (Type t : interfaces) {
            if (t instanceof Class<?>) {
                Class<?> clazz = (Class<?>) t;
                if (clazz.getPackage().getName().startsWith("ru.template.storage")) {
                    Type genericInterface = clazz.getGenericInterfaces()[0];
                    return (Class<E>) ((ParameterizedType) genericInterface).getActualTypeArguments()[0];
                }
            }
        }

        return null;
    }
}
