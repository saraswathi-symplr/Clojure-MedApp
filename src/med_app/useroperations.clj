(ns med-app.useroperations
 (:require [med-app.commonfunctions :refer :all]
 [monger.collection :as mc]
  [monger.operators :refer :all])
)

;;-----------------------------------------------Add new user functionality--------------------------------------------------------
(defn add-new-user[]
  (def loginName (read_input_function "Login ID"))
  (def userPassword (read_input_function "Password"))
  (def firstName (read_input_function "First Name"))
  (def lastName (read_input_function "Last Name"))
  (def email (read_input_function "Email"))
  (def address (read_input_function "Address"))
  (def phone (read_input_function "Phone"))
  (def active "Y")
  (def usertype (read_input_function "UserType Admin/User"))
  (println "usertype" usertype)
  
  (if (= usertype "Admin")
  (do (def userCode 1))
  (do (def userCode 0)))
  (def date (.toString (java.util.Date.)))

  (mc/insert db coll {:loginID loginName :password userPassword :first_name firstName :last_name lastName 
  :email email :address address :phone phone :user_type userCode :created_date date :active active}) 

  (println "User details have been added successfully")
)

;;-------------------------------------------------Inactivating[DELETE] the user functionality----------------------------------------
(defn delete-user-contact[]
  (def loginName (read_input_function "Enter Login ID to make user Inactive"))
 (mc/update db coll {:loginID loginName} {$set {:active "N"}})
 (println "The user has been succesfully Inactive")
)

;;-----------------------------------------------------Display existing list of users-----------------------------------------------------------
(defn display-existing-users []
  (def user_dump (mc/find-maps db coll))
  (def length_of_user_list (count user_dump))
  (def userindex (atom 0))
  (println "loginid \t first name \t last  name \t phone \t email \t address")
  (while (< @userindex length_of_user_list)
    (do
       (let [userindex (nth user_dump @userindex)
            first_name ((select-keys userindex [:first_name]) :first_name)
            last_name  ((select-keys userindex [:last_name]) :last_name)
            loginName  ((select-keys userindex [:loginID]) :loginID)
            phone      ((select-keys userindex [:phone]) :phone)
            email      ((select-keys userindex [:email]) :email)
            address    ((select-keys userindex [:address]) :address)]
           (println "loginid:"loginName"first name:"first_name"last name:"last_name"phone:"phone"email:"email"address:"address))
       (swap! userindex inc))))
  ;;------------------------------------------------------Display current user-----------------------------------------------------------

(defn display-current-user[loginName]
(def profile_return 
   (let [profile_doc (mc/find-maps db coll {:loginID loginName})]
  [
    ((select-keys (first profile_doc) [:loginID]) :loginID) 
    ((select-keys (first profile_doc) [:first_name]) :first_name) 
    ((select-keys (first profile_doc) [:last_name]) :last_name) 
    ((select-keys (first profile_doc) [:email]) :email) 
    ((select-keys (first profile_doc) [:address]) :address) 
    ((select-keys (first profile_doc) [:phone]) :phone) 
    ((select-keys (first profile_doc) [:user_type]) :user_type)
    ((select-keys (first profile_doc) [:active]) :active)
   ]))
  (def uname    (nth profile_return 0))
  (def ufname   (nth profile_return 1))
  (def ulname   (nth profile_return 2))
  (def uemail   (nth profile_return 3))
  (def uaddress (nth profile_return 4))
  (def uphone   (nth profile_return 5))
  (def uactive  (nth profile_return 6))
  (println uname)
  (println ufname)
  (println ulname)
  (println uemail)
  (println uaddress)
  (println uphone)
  (println uactive)
   )
    