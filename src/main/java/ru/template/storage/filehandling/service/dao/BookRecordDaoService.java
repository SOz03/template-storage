package ru.template.storage.filehandling.service.dao;

import org.springframework.stereotype.Service;
import ru.template.storage.common.dao.AbstractServiceDao;
import ru.template.storage.filehandling.entity.BookRecord;
import ru.template.storage.filehandling.repository.BookRecordRepository;

@Service
public class BookRecordDaoService extends AbstractServiceDao<BookRecord, BookRecordRepository> {

    public BookRecordDaoService(BookRecordRepository repository) {
        super(repository);
    }
}
