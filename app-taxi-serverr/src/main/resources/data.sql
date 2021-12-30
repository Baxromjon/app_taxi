insert into role(role_name)
values ('ROLE_CLIENT'),
       ('ROLE_DRIVER'),
       ('ROLE_PARTNER'),
       ('ROLE_MODERATOR'),
       ('ROLE_ADMIN'),
       ('ROLE_SUPER_ADMIN'),
       ('ROLE_PAYNET'),
       ('ROLE_PAYME'),
       ('ROLE_CLICK'),
       ('ROLE_OSON'),
       ('ROLE_UPAY');

insert into brand(id, name)
values (1, 'CHEVROLET'),
       (2, 'RAVON'),
       (3, 'LADA'),
       (4, 'DAEWOO'),
       (5, 'BMW');

insert into model(name, brand_id)
values ('NEXIA3', 1),
       ('GENTRA', 1),
       ('MALIBU', 1),
       ('NEXIA3', 2),
       ('GENTRA', 2),
       ('SPARK', 2),
       ('GRAND', 3),
       ('X-RAYN', 3),
       ('MATIZ', 4),
       ('X6', 5),
       ('X7', 5);

insert into pay_type(name, pay_type_enum)
values ('PAYME', 'PAYME'),
       ('CLICK', 'CLICK'),
       ('PAYNET', 'PAYNET'),
       ('OSON', 'OSON'),
       ('UPAY', 'UPAY');

insert into color(name, code)
values ('White', '#fff'),
       ('Black', '#000'),
       ('Green', '#008000');

insert into made_year(value)values
(2012),
(2013),
(2014),
(2015),
(2016),
(2017),
(2018),
(2019),
(2020);

create function calculate_distance(a_lat double precision, a_lon double precision, b_lat double precision, b_lon double precision) returns double precision
    language plpgsql
as
$$
DECLARE
    a1   float;
    a2   float;
    b1   float;
    b2   float;
    pk   float;
    t1   float;
    t2   float;
    t3   float;
    tt   float;
BEGIN
    pk = (180 / pi());
    a1 = a_lat / pk;
    a2 = a_lon / pk;
    b1 = b_lat / pk;
    b2 = b_lon / pk;
    t1 = cos(a1) * cos(a2) * cos(b1) * cos(b2);
    t2 = cos(a1) * sin(a2) * cos(b1) * sin(b2);
    t3 = sin(a1) * sin(b1);
    tt = acos(t1 + t2 + t3);
    RETURN 6366000 * tt;
END;
$$;

alter function calculate_distance(double precision, double precision, double precision, double precision) owner to postgres;


