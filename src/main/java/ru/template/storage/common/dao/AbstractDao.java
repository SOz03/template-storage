package ru.template.storage.common.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import ru.template.storage.common.dto.GetListDto;
import ru.template.storage.common.dto.PagingDto;
import ru.template.storage.common.dto.SortingDirection;
import ru.template.storage.common.dto.filter.CommonFilter;
import ru.template.storage.common.dto.filter.ComplexFilter;
import ru.template.storage.common.dto.filter.FilterType;
import ru.template.storage.common.dto.filter.SimpleFilterDto;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.Field;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.BiFunction;

import static org.springframework.transaction.annotation.Propagation.REQUIRED;

@Slf4j
public class AbstractDao<E, R extends JpaSpecificationExecutor<E>> {

    private static final DateTimeFormatter DEFAULT_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    protected R repository;

    @Autowired
    public AbstractDao(R repository) {
        this.repository = repository;
    }

    @Transactional
    public Page<E> findAll(Specification<E> specificationFilters, Pageable page) {
        return repository.findAll(specificationFilters, page);
    }

    @Transactional(propagation = REQUIRED, readOnly = true)
    public Page<E> findAllByDto(@Nullable GetListDto dto) {
        return findAllBySpecAndDto(null, dto);
    }

    @Transactional(propagation = REQUIRED, readOnly = true)
    public E findOneBySpec(@Nullable Specification<E> specificationFilters) {
        try {
            Optional<E> opt = repository.findOne(specificationFilters);
            return opt.get();
        } catch (IncorrectResultSizeDataAccessException ex) {
            throw new RuntimeException("Найдено более одной записи");
        } catch (NoSuchElementException ex) {
            return null;
        }
    }

    @Transactional(propagation = REQUIRED, readOnly = true)
    public Page<E> findAllBySpec(@Nullable Specification<E> specificationFilters) {
        return findAllBySpecAndDto(specificationFilters, null);
    }

    @Transactional(propagation = REQUIRED, readOnly = true)
    public Page<E> findAllBySpecAndDto(@Nullable Specification<E> specificationFilters, @Nullable GetListDto dto) {

        specificationFilters = (specificationFilters == null ? Specification.where(null) : specificationFilters);

        if (dto == null) {
            return repository.findAll(specificationFilters, PageRequest.of(0, 10));
        }

        Pageable pagingAndSorting = null;
        Sort sorting = null;

        if (dto.getSorting() != null) {
            sorting = Sort.by(dto.getSorting().getColumnKey());
            if (SortingDirection.valueOf(SortingDirection.DESC.name()).equals(dto.getSorting().getDirection())) {
                sorting = sorting.descending();
            }
        }

        if (dto.getPaging() == null) {
            dto.setPaging(new PagingDto(0, 100));
        }
        if (sorting != null) {
            pagingAndSorting = PageRequest.of(dto.getPaging().getCurrentPage(), dto.getPaging().getPageSize(), sorting);
        } else {
            pagingAndSorting = PageRequest.of(dto.getPaging().getCurrentPage(), dto.getPaging().getPageSize());
        }

        if (dto.getFilters() != null) {
            specificationFilters = specificationFilters.and(processFilters(dto.getFilters()));
        }

        return repository.findAll(specificationFilters, pagingAndSorting);
    }

    private Specification<E> processFilters(CommonFilter filters) {

        FilterType fType = filters.getType();

        if (fType != null && fType.isSimpleType()) {
            return processSimpleFilter(filters);
        } else {
            return processComplexFilter((ComplexFilter) filters);
        }
    }

    private Specification<E> processComplexFilter(ComplexFilter filters) {
        List<CommonFilter> operandList = filters.getOperands();
        List<Specification<E>> specifications = new ArrayList<>();

        for (CommonFilter o : operandList) {
            specifications.add(processFilters(o));
        }

        switch (filters.getType()) {
            case OR:
                Specification<E> orSpecification = Specification.where(null);
                for (Specification<E> eSpecification : specifications) {
                    orSpecification = orSpecification.or(eSpecification);
                }
                return orSpecification;
            case AND:
            default:
                Specification<E> andSpecification = Specification.where(null);
                for (Specification<E> eSpecification : specifications) {
                    andSpecification = andSpecification.and(eSpecification);
                }
                return andSpecification;
        }
    }

    private Specification<E> processSimpleFilter(CommonFilter base) {
        Specification<E> specification = null;

        switch (base.getType()) {
            case CONTAINS:
                specification = containsFilter((SimpleFilterDto) base);
                break;
            case EQ:
                specification = equalsFilter((SimpleFilterDto) base);
                break;
            case GT:
                specification = gtFilter((SimpleFilterDto) base);
                break;
            case LT:
                specification = ltFilter((SimpleFilterDto) base);
                break;
            default:
                break;
        }
        return specification;
    }

    private Specification<E> equalsFilter(SimpleFilterDto dto) {
        return (Specification<E>) (root, query, criteriaBuilder) -> {
            Predicate condition = specificationFiltering(
                    dto,
                    criteriaBuilder::equal,
                    criteriaBuilder::equal,
                    criteriaBuilder::equal,
                    criteriaBuilder::equal,
                    criteriaBuilder::equal,
                    criteriaBuilder::equal,
                    criteriaBuilder::equal,
                    root);
            return criteriaBuilder.and(condition);
        };
    }

    private Specification<E> gtFilter(SimpleFilterDto dto) {
        return (Specification<E>) (root, query, criteriaBuilder) -> {
            Predicate condition = specificationFiltering(
                    dto,
                    criteriaBuilder::greaterThan,
                    criteriaBuilder::greaterThan,
                    criteriaBuilder::greaterThan,
                    criteriaBuilder::greaterThan,
                    criteriaBuilder::greaterThan,
                    criteriaBuilder::greaterThan,
                    criteriaBuilder::greaterThan,
                    root);
            return criteriaBuilder.and(condition);
        };
    }

    private Specification<E> ltFilter(SimpleFilterDto dto) {
        return (Specification<E>) (root, query, criteriaBuilder) -> {
            Predicate condition = specificationFiltering(
                    dto,
                    criteriaBuilder::lessThan,
                    criteriaBuilder::lessThan,
                    criteriaBuilder::lessThan,
                    criteriaBuilder::lessThan,
                    criteriaBuilder::lessThan,
                    criteriaBuilder::lessThan,
                    criteriaBuilder::lessThan,
                    root);
            return criteriaBuilder.and(condition);
        };
    }

    @SuppressWarnings("unchecked")
    private Predicate specificationFiltering(
            SimpleFilterDto dto,
            BiFunction<Expression<? extends Date>, Date, Predicate> functionDate,
            BiFunction<Expression<? extends LocalDate>, LocalDate, Predicate> functionLocalDate,
            BiFunction<Expression<? extends UUID>, UUID, Predicate> functionUUID,
            BiFunction<Expression<? extends Long>, Long, Predicate> functionLong,
            BiFunction<Expression<? extends Integer>, Integer, Predicate> functionInteger,
            BiFunction<Expression<? extends Double>, Double, Predicate> functionDouble,
            BiFunction<Expression<? extends String>, String, Predicate> functionString,
            Root<E> root) {
        Predicate condition = null;
        String fieldType = getValueType(root, dto.getColumnKey());
        try {
            Class<?> aClass = Class.forName(fieldType);
            String[] parts = dto.getColumnKey().split("\\.");
            Path<?> path = root.get(parts[0]);
            for (int i = 1; i < parts.length; i++) {
                path = path.get(parts[i]);
            }
            if (aClass.isEnum()) {
                // не поддерживается
            } else if (aClass.isAssignableFrom(LocalDate.class)) {
                LocalDate date = LocalDate.parse(dto.getValue(), DEFAULT_DATE_FORMAT);
                condition = functionLocalDate.apply((Expression<? extends LocalDate>) path, date);
            } else if (aClass.isAssignableFrom(Date.class)) {
                Date date = Date.valueOf(ZonedDateTime.parse(dto.getValue(), DEFAULT_DATE_FORMAT).toLocalDate());
                condition = functionDate.apply((Expression<? extends Date>) path, date);
            } else if (aClass.isAssignableFrom(UUID.class)) {
                condition = functionUUID.apply((Expression<? extends UUID>) path, UUID.fromString(dto.getValue()));
            } else if (aClass.isAssignableFrom(Integer.class)) {
                condition = functionInteger.apply((Expression<? extends Integer>) path, Integer.valueOf(dto.getValue()));
            } else if (aClass.isAssignableFrom(Double.class)) {
                condition = functionDouble.apply((Expression<? extends Double>) path, Double.valueOf(dto.getValue()));
            } else if (aClass.isAssignableFrom(Long.class)) {
                condition = functionLong.apply((Expression<? extends Long>) path, Long.valueOf(dto.getValue()));
            } else {
                condition = functionString.apply((Expression<? extends String>) path, dto.getValue());
            }

        } catch (ClassNotFoundException e) {
            log.error("An error while using reflection for getting attribute type", e);
        }
        return condition;
    }

    private Specification<E> containsFilter(SimpleFilterDto dto) {
        Hashtable<String, String> filterFieldNameAndValueHTable = new Hashtable<String, String>();
        filterFieldNameAndValueHTable.put(dto.getColumnKey(), dto.getValue());
        return specificationLikeFiltering(filterFieldNameAndValueHTable, dto.getType());

    }

    private Specification<E> specificationLikeFiltering(Hashtable<String, String> filterFieldNameAndValueHTable, FilterType filterType) {
        return (Specification<E>) (root, query, criteriaBuilder) -> {

            List<Predicate> conditions = new ArrayList<>();

            filterFieldNameAndValueHTable.forEach((field, value) -> {

                Predicate condition = criteriaBuilder.like(criteriaBuilder.lower(root.get(field)), "%" + String.valueOf(value).toLowerCase() + "%");
                conditions.add(condition);

            });
            switch (filterType) {
                case OR:
                    return criteriaBuilder.or(conditions.toArray(new Predicate[conditions.size()]));
                default:
                    return criteriaBuilder.and(conditions.toArray(new Predicate[conditions.size()]));
            }
        };
    }

    private String getValueType(Root<E> root, String field) {
        String fieldType = String.class.getName();
        String[] parts = field.split("\\.");
        Class<?> current = root.getJavaType();
        for (int i = 0; i < parts.length; i++) {
            for (Field declaredField : getAllFields(new ArrayList<>(), current)) {
                if (declaredField.getName().equals(parts[i])) {
                    fieldType = declaredField.getType().getName();
                    current = declaredField.getType();
                    break;
                }
            }
        }
        return fieldType;
    }

    public List<Field> getAllFields(List<Field> fields, Class<?> type) {
        fields.addAll(Arrays.asList(type.getDeclaredFields()));

        if (type.getSuperclass() != null) {
            getAllFields(fields, type.getSuperclass());
        }

        return fields;
    }
}
