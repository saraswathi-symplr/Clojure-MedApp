(ns med-app.core
  (:gen-class)
  (:require [med-app.commonfunctions :refer :all]
  [med-app.useroperations :refer :all]
  [med-app.productoperations :refer :all]
  [med-app.orderoperations :refer :all]
  [monger.collection :as mc]
  [clojure.string :as str]
  [cheshire.core :refer :all])
  (:import org.bson.types.ObjectId))

;;-----------------------------------------------Login check function--------------------------------------------------------
(defn login_check [username_attempt]
  (let [matching_doc (mc/find-maps db coll {:loginID username_attempt})]
  [((select-keys (first matching_doc) [:loginID]) :loginID) 
   ((select-keys (first matching_doc) [:password]) :password) 
   ((select-keys (first matching_doc) [:user_type]) :user_type)
   ((select-keys (first matching_doc) [:active]) :active)
   ]))  
     
;;-----------------------------------------------User login-------------------------------------------------------------------------   
(defn user_login[]
(def isAuth (atom 0))
  (def privilege (atom 0))
  (println "-----------------------MedApp------------------------------")
  (def loginID (read_input_function "loginID"))
  (def passID (read_input_function "Password"))
  (def mongo_return (login_check loginID))
  (def user_name (nth mongo_return 0))
  (def password (nth mongo_return 1))
  (def isactive (nth mongo_return 3))
  (if (and (= loginID user_name) (= passID password))
      (do 
      (if (= isactive "Y")
      (do
      (reset! isAuth 1)
       (reset! privilege (nth mongo_return 2))
          (println "Hi"user_name", you are logged in successfully - Access Level"@privilege))
      (do (println "User is inactive"))
      )
      )
      (do (println "Invalid username or password")))

  (while (= @isAuth 1)
    (do
      (if (= @privilege 1)
      (do 
      (println "Enter numbers for selecting a menu..:")
      (println "1. Add User\n2. Upload Product\n3. Show Users\n4. Delete User") 
      (println "Enter logout to exit")) 
      (do 
      (println "Enter numbers for selecting a menu..:")
      (println "1. Show Products\n2. Order Medicine\n3. Export Order Details\n4. View Profile")
      (println "Enter logout to exit")) 
      )
      (def word (read_input_function "Enter your commands:"))
      (if (= word "logout")
        (do (println "[User: "user_name "][Access Level: "@privilege "]")
          (reset! isAuth 0) (println "logging out") (println "------------------------------------------------------------"))
        (do (println "[User:"user_name"][Access Level:"@privilege"]")
            (println "------------------------------------------------------------")
            (if (= @privilege 1)
            ;;Menu for Admins(1)
            (do 
            (case word 
                  "1" (add-new-user)
                  "2" (upload-product-data)
                  "3" (display-existing-users)   
                  "4" (delete-user-contact)
                  (println "Enter valid commands"))
            )
            ;;Menu for Normal users(0)
            (do
            (case word            
                  "1" (display-existing-products)
                  "2" (add-product-cart user_name)
                  "3" (export-order-details user_name)
                  "4" (display-current-user user_name)       
                  (println "Enter valid commands"))
            )
            )
              )))))
;;-----------------------------------------------Program main function--------------------------------------------------------
(defn -main
  "I don't do a whole lot ... yet."
  [& args] 
  (initialize)
  (def continue (atom "y"))
  (while (not= @continue "n")
    (do 
      (user_login)
      (def yn (read_input_function "Do you want to continue(y/n)?"))
      (reset! continue yn))
      )
  (println  "-------------------Exiting Med App------------------------"))