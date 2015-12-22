(ns exposure.database
  (:require [taoensso.timbre :as log]
            [exposure.utils.localstorage :as storage]
            [exposure.constants :as constants]
            [exposure.utils.helpers :refer [read-json]]))

(defn init-user
  "Loads instagram user from localStorage if present"
  []
  (let [string (storage/get-item constants/localstorage-profile-key)
        result (read-json string)]
    result))

(defn sign-out
  "Removes current authorization and returns new DB instance"
  [db]
  (storage/remove-item! constants/localstorage-profile-key)
  (dissoc db :profile))

(def default-db
  {:name "re-frame"
   :active-page :home-page
   :profile (init-user)
   :search-pending false
   :search-query ""
   :posts []})

