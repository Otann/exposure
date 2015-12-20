(ns exposure.handlers
  (:require [re-frame.core :as re-frame]
            [taoensso.timbre :as log :include-macros true]
            [exposure.database :as db]))

(re-frame/register-handler
  :initialize-db
  (fn [_ _]
    db/default-db))

(re-frame/register-handler
  :set-active-page
  (fn [db [_ active-page]]
    (log/debug "Setting active page: " active-page)
    (assoc db :active-page active-page)))

(re-frame/register-handler
  :btn-clicked
  (fn [db _]
    (update-in db [:clicked] inc)))
