(ns exposure.handlers
  (:require [re-frame.core :refer [register-handler]]
            [taoensso.timbre :as log :include-macros true]
            [exposure.database :as db]))

(register-handler :initialize-db
  (fn [_ _]
    (log/debug "Initializaing database")
    db/default-db))

(register-handler :set-active-page
  (fn [db [_ active-page]]
    (log/debug "Setting active page: " active-page)
    (assoc db :active-page active-page)))

(register-handler :sign-out
  (fn [db _]
    (db/sign-out db)))
