ALTER TABLE movimentacao
    ALTER COLUMN tipo TYPE VARCHAR USING tipo::text;
