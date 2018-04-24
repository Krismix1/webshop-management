INSERT INTO `categories` (`id`, `name`) VALUES (1, 'Category A'), (2, 'Category B');
INSERT INTO `prod_types` (`id`, `name`, `descr`, `category_id`) values(1, 'Prod type 1', 'This is first type of products', 2),
(2, 'Prod type 2', 'This is the second type', 1);
INSERT INTO `products` (`id`, `name`, `price`, `short_desc`, `image`, `type_id`) VALUES (1, 'Prod 1', 147, 'This is a short description', 'http://via.placeholder.com/300', 2),
  (2, 'Prod Bag', 100, 'This is another short description', 'http://lexiconcss.wedeploy.io/images/thumbnail_placeholder.gif', 1);
