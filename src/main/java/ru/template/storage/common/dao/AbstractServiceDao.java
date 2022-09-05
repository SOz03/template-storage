package ru.template.storage.common.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import ru.template.storage.common.entity.BaseEntity;
import ru.template.storage.common.repository.AbstractRepository;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.transaction.annotation.Propagation.REQUIRED;

@Slf4j
public abstract class AbstractServiceDao<E extends BaseEntity, R extends AbstractRepository<E>> extends AbstractDao<E, R> {

    protected final R repository;

    @Autowired
    public AbstractServiceDao(R repository) {
        super(repository);
        this.repository = repository;
    }

    @Transactional(propagation = REQUIRED, readOnly = true)
    public Iterable<E> findAll() {
        return repository.findAll();
    }

    @Transactional(propagation = REQUIRED, readOnly = true)
    public E findById(UUID id) {
        Optional<E> op = repository.findById(id);

        if (!op.isPresent()) {
            String canonicalName = repository.getEntityClass() != null ? repository.getEntityClass().getCanonicalName() : "undefined";
            log.error("Row with id: {} is not found in database. Class {}", id, canonicalName);
            throw new RuntimeException("Значение не найдено");
        }

        return op.get();
    }

    @Transactional(propagation = REQUIRED, readOnly = true)
    public E findByIdNullable(UUID id) {
        Optional<E> op = repository.findById(id);
        if (op.isPresent()) {
            return op.get();
        }
        return null;
    }

    @Transactional(propagation = REQUIRED)
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

    @Transactional(propagation = REQUIRED)
    public void delete(E entity) {
        repository.delete(entity);
    }

    @Transactional(propagation = REQUIRED)
    public void deleteAll(Iterable<? extends E> entities) {
        repository.deleteAll(entities);
    }

    @Transactional(propagation = REQUIRED)
    public E save(E entity) {
        return repository.save(entity);
    }

    @Transactional(propagation = REQUIRED)
    public void deleteAll() {
        repository.deleteAll();
    }

    @Transactional(propagation = REQUIRED)
    public Iterable<E> saveAll(Iterable<E> entities) {
        return repository.saveAll(entities);
    }

}
