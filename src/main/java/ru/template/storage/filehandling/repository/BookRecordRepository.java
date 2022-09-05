package ru.template.storage.filehandling.repository;

import org.springframework.stereotype.Repository;
import ru.template.storage.common.repository.AbstractRepository;
import ru.template.storage.filehandling.entity.BookRecord;


@Repository
public interface BookRecordRepository extends AbstractRepository<BookRecord> {

}