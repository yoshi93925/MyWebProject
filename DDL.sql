-- Project Name : Mr's4G
-- Date/Time    : 2026/09/07 16:19:46
-- Author       : H30715
-- RDBMS Type   : PostgreSQL
-- Application  : A5:SQL Mk-2

/*
  << 注意！！ >>
  BackupToTempTable, RestoreFromTempTable疑似命令が付加されています。
  これにより、drop table, create table 後もデータが残ります。
  この機能は一時的に $$TableName のような一時テーブルを作成します。
  この機能は A5:SQL Mk-2でのみ有効であることに注意してください。
*/

-- 管理者

DROP TABLE if exists "admin" CASCADE;


CREATE TABLE "admin" (
  "admin_id" serial NOT NULL
  , "name" character varying(20) NOT NULL
  , "password" character varying(255) NOT NULL
  , CONSTRAINT "admin_PKC" PRIMARY KEY ("admin_id")
) ;

-- 注文詳細

DROP TABLE if exists "order_details" CASCADE;


CREATE TABLE "order_details" (
  "order_detail_id" serial NOT NULL
  , "item_id" integer NOT NULL
  , "order_id" integer NOT NULL
  , "quantity" integer DEFAULT 1 NOT NULL
  , CONSTRAINT "order_details_PKC" PRIMARY KEY ("order_detail_id")
) ;

-- 注文

DROP TABLE if exists "orders" CASCADE;


CREATE TABLE "orders" (
  "order_id" serial NOT NULL
  , "order_no" character varying(7) NOT NULL
  , "name" character varying(20) NOT NULL
  , "address" character varying(255) NOT NULL
  , "phone" character varying(11) NOT NULL
  , "note" TEXT
  , "ordered_at" timestamp with time zone DEFAULT NOW()
  , "delivery_staff" character varying(20)
  , "delivery_status" character varying(10) DEFAULT '未対応' NOT NULL
  , "notdelivery_note" text
  , CONSTRAINT "orders_PKC" PRIMARY KEY ("order_id")
) ;

ALTER TABLE "orders" ADD CONSTRAINT "注文番号"
  UNIQUE ("order_no") ;

-- 物資

DROP TABLE if exists "items" CASCADE;


CREATE TABLE "items" (
  "item_id" serial NOT NULL
  , "item_name" character varying(100) NOT NULL
  , "stock" integer DEFAULT 0 NOT NULL
  , "location" character varying(10) NOT NULL
  , "genre_id" integer NOT NULL
  , "updated_at" timestamp with time zone DEFAULT NOW() NOT NULL
  , CONSTRAINT "items_PKC" PRIMARY KEY ("item_id")
) ;

ALTER TABLE "order_details"
  ADD CONSTRAINT "order_details_FK1" FOREIGN KEY ("item_id") REFERENCES "items"("item_id");

ALTER TABLE "order_details"
  ADD CONSTRAINT "order_details_FK2" FOREIGN KEY ("order_id") REFERENCES "orders"("order_id")
  ON DELETE CASCADE;

COMMENT ON TABLE "admin" IS '管理者';
COMMENT ON COLUMN "admin"."admin_id" IS '管理ID:自動生成';
COMMENT ON COLUMN "admin"."name" IS '名前';
COMMENT ON COLUMN "admin"."password" IS 'パスワード';

COMMENT ON TABLE "order_details" IS '注文詳細';
COMMENT ON COLUMN "order_details"."order_detail_id" IS '注文詳細ID:自動生成される';
COMMENT ON COLUMN "order_details"."item_id" IS '品目ID　';
COMMENT ON COLUMN "order_details"."order_id" IS '注文ID';
COMMENT ON COLUMN "order_details"."quantity" IS '数量';

COMMENT ON TABLE "orders" IS '注文';
COMMENT ON COLUMN "orders"."order_id" IS '注文ID:自動生成';
COMMENT ON COLUMN "orders"."order_no" IS '注文番号:Java側でランダム自動生成';
COMMENT ON COLUMN "orders"."name" IS '名前';
COMMENT ON COLUMN "orders"."address" IS '住所';
COMMENT ON COLUMN "orders"."phone" IS '電話番号';
COMMENT ON COLUMN "orders"."note" IS '備考';
COMMENT ON COLUMN "orders"."ordered_at" IS '注文日時';
COMMENT ON COLUMN "orders"."delivery_staff" IS '配送担当者';
COMMENT ON COLUMN "orders"."delivery_status" IS '配送ステータス:登録時''未対応''で登録される';
COMMENT ON COLUMN "orders"."notdelivery_note" IS '配送不可理由';

COMMENT ON TABLE "items" IS '物資';
COMMENT ON COLUMN "items"."item_id" IS '品目ID　';
COMMENT ON COLUMN "items"."item_name" IS '品目名';
COMMENT ON COLUMN "items"."stock" IS '在庫数';
COMMENT ON COLUMN "items"."location" IS '場所';
COMMENT ON COLUMN "items"."genre_id" IS 'ジャンルID:01:食料・飲料 02:衛生・衣料品 03:生活・日用品 04:防寒・睡眠・衣類 05:インフラ・環境整備';
COMMENT ON COLUMN "items"."updated_at" IS '更新日時';


-- ============================================================
-- 1. 管理者 (admin) テーブルへのデータ追加
-- ============================================================
INSERT INTO admin(name,password) VALUES
  ('管理者A', '1234')
, ('支援本部スタッフ', 'staffPass456')
, ('管理者V', 'pass1234')
, ('管理者T', 'sum1234')
;

-- ============================================================
-- 2. 物資 (items) テーブルへのデータ追加
-- ============================================================
-- genre_id : 01:食料・飲料 / 02:衛生・衣料品 / 03:生活・日用品 / 04:防寒・睡眠・衣類 / 05:インフラ・環境整備
INSERT INTO items(item_name,stock,location,genre_id,updated_at) VALUES
  ('保存水 2L', 500, 'A-01', 1,'2026/09/04')
, ('非常用おにぎり', 300, 'A-02', 1,'2026/09/06')
, ('不織布マスク (50枚入)', 150, 'B-01', 2,'2026/09/10')
, ('アルコール消毒液', 80, 'B-02', 2,'2026/09/15')
, ('トイレットペーパー (12ロール)', 100, 'C-01', 3,'2026/09/18')
, ('毛布', 200, 'D-01', 4,'2026/09/20')
, ('ポータブル発電機', 10, 'E-01', 5,'2026/09/25')
;


-- ============================================================
-- 3. 注文 (orders) テーブルへのデータ追加
-- ============================================================
-- ※ delivery_status の初期値は '未対応' (デフォルト値)
INSERT INTO orders (order_no, name, address, phone, note, delivery_staff, delivery_status, notdelivery_note) VALUES
  ( 1, '山田 太郎', '東京都千代田区1-1-1 避難所A', '09012345678', 'なるべく早めの配送を希望します。', NULL, '未対応', NULL)
, ( 3, '佐藤 花子', '東京都千代田区1-1-2 避難所B', '08098765432', 'アレルギー対応食品があれば助かります。', '鈴木 配送員', '対応中', NULL)
, ( 4, '高橋 健一', '東京都千代田区1-1-3 避難所C', '07011112222', '高齢者が多いため毛布を多めに希望。', '佐藤 配送員', '完了', NULL)
, (5 ,'田中 誠', '東京都千代田区1-1-4 避難所D', '09033334444', '路面崩落のためアクセス注意', NULL, '配送不可', '道路障害のため車両通行不可')
;


-- ============================================================
-- 4. 注文詳細 (order_details) テーブルへのデータ追加
-- ========INSERT INTO order_details (order_id, item_id, quantity) VALUES
INSERT INTO order_details (order_id, item_id, quantity) VALUES
  (1,2, 2),
  (2,1, 10),
  (3,5, 3),
  (3,6, 2)
;
