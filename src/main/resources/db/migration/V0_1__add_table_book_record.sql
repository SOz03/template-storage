CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

create table template_storage.tr_book_record
(
    id                 uuid
        constraint tr_book_record_pk
            primary key default uuid_generate_v4(),
    created_at         float8,
    updated_at         float8,
    court_orders_at    text,
    conduct_inquiry_at text,
    name_body_inquiry  text,
    criminal_number    bigint,
    initiation_date    date,
    fullname           text,
    criminal_code      text
);

create unique index tr_book_record_id_uindex on template_storage.tr_book_record (id);


insert into template_storage.tr_book_record (created_at, updated_at, court_orders_at,
                                             conduct_inquiry_at, name_body_inquiry, criminal_number,
                                             initiation_date, fullname, criminal_code)
values (1658929663157, 1658929663157, '2022-08-26T06:47:51.531Z',
        '2022-08-26T06:47:51.531Z', 'name_body_inquiry', '1255125251512521',
        '2022-05-20', 'Ivanov Ivan Иванович', null),
       (1658929663157, 1658929663157, '2022-08-26T06:47:51.531Z',
        '2022-08-26T06:47:51.531Z', 'name_body_inquiry', '1255125251512521',
        '2022-05-20', 'Ivanov Ivan Иванович', '1.5.717'),
       (1658929663157, 1658929663157, '2022-08-26T06:47:51.531Z',
        '2022-08-26T06:47:51.531Z', 'name_body_inquiry', '1255125251512521',
        '2022-05-20', 'Ivanov Ivan Иванович', '1.5.717'),
       (1658929663157, 1658929663157, '2022-08-26T06:47:51.531Z',
        '2022-08-26T06:47:51.531Z', 'name_body_inquiry', '1255125251512521',
        '2022-05-20', 'Ivanov Ivan Иванович', '1.5.717'),
       (1658929663157, 1658929663157, '2022-08-26T06:47:51.531Z',
        null,'name_body_inquiry', '1255125251512521',
        '2022-05-20', 'Ivanov Ivan Иванович', '1.5.717'),
       (1658929663157, 1658929663157, '2022-08-26T06:47:51.531Z',
        '2022-08-26T06:47:51.531Z', 'name_body_inquiry', '1255125251512521',
        '2022-05-20', 'Ivanov Ivan Иванович', '1.5.717'),
       (1658929663157, 1658929663157, '2022-08-26T06:47:51.531Z',
        '2022-08-26T06:47:51.531Z', 'name_body_inquiry', '1255125251512521',
        null, 'Ivanov Ivan Иванович', '1.5.717'),
       (1658929663157, 1658929663157, '2022-08-26T06:47:51.531Z',
        '2022-08-26T06:47:51.531Z', 'name_body_inquiry', '1255125251512521',
        '2022-05-20', 'Ivanov Ivan Иванович', '1.5.717'),
       (1658929663157, 1658929663157, '2022-08-26T06:47:51.531Z',
        '2022-08-26T06:47:51.531Z', 'name_body_inquiry', '1255125251512521',
        '2022-05-20', 'Ivanov Ivan Иванович', '1.5.717'),
       (1658929663157, 1658929663157, '2022-08-26T06:47:51.531Z',
        '2022-08-26T06:47:51.531Z', 'name_body_inquiry', '1255125251512521',
        '2022-05-20', 'Ivanov Ivan Иванович', '1.5.717'),
       (1658929663157, 1658929663157, '2022-08-26T06:47:51.531Z',
        '2022-08-26T06:47:51.531Z', 'name_body_inquiry', '1255125251512521',
        '2022-05-20', 'Ivanov Ivan Иванович', '1.5.717');