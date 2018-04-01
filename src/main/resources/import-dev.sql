INSERT INTO `products` (`id`, `name`, `price`, `short_desc`) VALUES (1, 'Prod 1', 147, 'This is a short description'),
  (2, 'Prod Bag', 100, 'This is another short description');
INSERT INTO `categories` (`id`, `name`) VALUES (1, 'Category A'), (2, 'Category B');
INSERT INTO `categories_products` (`category_id`, `products_id`) VALUES (2, 1), (2, 2);