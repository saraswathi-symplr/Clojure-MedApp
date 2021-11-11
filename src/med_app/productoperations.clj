(ns med-app.productoperations
 (:require [med-app.commonfunctions :refer :all]
 [monger.collection :as mc]
 [clojure.edn :as edn])
)

;;-----------------------------------------------Upload product data---------------------------------------------------------------------
(defn upload-product-data[]
  (mc/remove db product_coll)
  (with-open [rdr (clojure.java.io/reader "productdata/productfile.txt")]
    (doseq [line (line-seq rdr)]
      (mc/insert db product_coll (edn/read-string line))))
  (println "Medicines have been loaded successfully")
)


;;-----------------------------------------------Diaplay existing list of products--------------------------------------------------------
(defn display-existing-products []
  (def product_dump (mc/find-maps db product_coll))
  (def length_of_product_list (count product_dump))
  (def productindex (atom 0))
  (println "medicine name \t medicine price \t  medicine quantity ")
  (while (< @productindex length_of_product_list)
    (do
       (let [productindex (nth product_dump @productindex)
            medicine_name     ((select-keys productindex [:medicine_name]) :medicine_name)
            medicine_price    ((select-keys productindex [:medicine_price]) :medicine_price)
            medicine_quantity ((select-keys productindex [:medicine_quantity]) :medicine_quantity)]
           (println "medicine name:"medicine_name"medicine price:"medicine_price"medicine quantity:"medicine_quantity))
       (swap! productindex inc))))