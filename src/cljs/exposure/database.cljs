(ns exposure.database
  (:require [exposure.utils.localstorage :as storage]
            [exposure.constants :as constants]
            [taoensso.timbre :as log]))

(defn init-user
  "Loads instagram user from localStorage if present"
  []
  (let [string (storage/get-item constants/localstorage-profile-key)
        json   (.parse js/JSON string)
        result (js->clj json :keywordize-keys true)]
    result))

(defn sign-out
  "Removes current authorization and returns new DB instance"
  [db]
  (storage/remove-item! constants/localstorage-profile-key)
  (dissoc db :profile))

(def default-db
  {:name "re-frame"
   :active-page :home-page
   :profile (init-user)})

(def db-sample
  {:active-page :home-page
   :profile {:access_token "abacaba"
             :user {:username "otann"
                    :profile_picture "http://"}}
   :pages {:home-page {:search-form {:value ""}}}})