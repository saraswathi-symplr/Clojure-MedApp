(ns med-app.orderoperations
 (:require [med-app.commonfunctions :refer :all]
 [med-app.productoperations :refer :all]
 [monger.collection :as mc]
 [monger.operators :refer :all]
 [clojure.data.csv :as csv]
 [clojure.java.io :as io]
 [clojure.edn :as edn])
)

;;------------------------------------------------Adding product to cart---------------------------------------------------------------

(defn add-product-cart[loginName]
(display-existing-products)
  (def medName (read_input_function "Enter Medicine Name"))
  (def medBookQty (read_input_function "Enter Quantity"))
  (def one_product_return 
   (let [one_product_doc (mc/find-maps db product_coll {:medicine_name medName})]
  [
    ((select-keys (first one_product_doc) [:medicine_price]) :medicine_price) 
    ((select-keys (first one_product_doc) [:medicine_quantity]) :medicine_quantity) 
   ]))
  (def medPrice   (nth one_product_return 0))
  (def medAvailQty   (nth one_product_return 1))
  (if (< (Integer/parseInt medBookQty) (Integer/parseInt medAvailQty))
  (do
  (println "Please enter Y to confirm booking \n  The price "medName" is "(* (Integer/parseInt medPrice) (Integer/parseInt medBookQty)))
  (def canContinue (read_input_function "Continue?"))
  (if (= canContinue "Y")
  (do
  (mc/insert db order_coll {:loginID loginName :medicine_name medName :medicine_book_qty medBookQty :total_price (* (Integer/parseInt medPrice) (Integer/parseInt medBookQty)) 
  :order_date (.toString (java.util.Date.))}) 
  (mc/update db product_coll {:medicine_name medName} {$set {:medicine_quantity (- (Integer/parseInt medAvailQty) (Integer/parseInt medBookQty)) }})
  (println "Successfully ordered")
  )
  (do (add-product-cart))
  ) 
  )
  (do (println "Quantity not available"))
  )
)

;;-------------------------------------------------------------Exporting order---------------------------------------------------------------------
(defn export-order-details [loginName]
(def filename  (str "orderdata/order_" (.format (java.text.SimpleDateFormat. "MMddyyyyhhmmss") (new java.util.Date)) ".csv")
)
  (with-open [writer (io/writer filename)]
  (csv/write-csv writer
   (vec (for [m 
    (mc/find-maps db order_coll {:loginID loginName})
    ] ((juxt :medicine_name :medicine_book_qty :total_price :order_date) m))
    )
  ))
  (println "Data has been exported successfully\n")
)