insert into orders (user_id, created_at, updated_at)
values (1, current_timestamp, current_timestamp);

insert into order_items (order_id, product_name, price, quantity)
values (1, 'apple', 1000, 2);

insert into order_items (order_id, product_name, price, quantity)
values (1, 'banana', 500, 3);
