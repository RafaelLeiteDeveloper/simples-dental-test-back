ALTER TABLE products
ADD CONSTRAINT uqk_products_code UNIQUE (code);

ALTER TABLE products
ALTER COLUMN category_id SET NOT NULL;

ALTER TABLE categories
ADD CONSTRAINT uqk_categories_name UNIQUE (name);

ALTER TABLE products
ADD CONSTRAINT uqk_products_name UNIQUE (name);

CREATE INDEX id_categories_name ON categories(name);
CREATE INDEX id_products_name ON products(name);
CREATE INDEX id_products_category_id ON products(category_id);

