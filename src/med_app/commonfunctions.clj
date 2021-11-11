(ns med-app.commonfunctions
(:require [monger.core :as mg]))

(defn read_input_function [inputArgs]
  (println inputArgs ": ")
(let [console_input (read-line)]console_input))

;;------------------------------------------------Initializing MongoDB------------------------------------------------
(defn initialize []
  (def conn (mg/connect))
  (def db (mg/get-db conn "medapp"))
  (def coll "userdata")
  (def product_coll "productdata")
  (def order_coll "orderdata")
)