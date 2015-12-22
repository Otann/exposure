(ns exposure.handlers
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [re-frame.core :refer [register-handler dispatch]]
            [taoensso.timbre :as log :include-macros true]
            [cljs.core.async :refer [<!]]
            [cljs-http.client :as http]

            [exposure.utils.helpers :refer [read-json]]
            [exposure.database :as db]))

(register-handler :initialize-db
  (fn [_ _] db/default-db))

(register-handler :set-active-page
  (fn [db [_ active-page]] (assoc db :active-page active-page)))

(register-handler :sign-out
  (fn [db _] (db/sign-out db)))

(register-handler :search-init
  (fn [db [_ data token]]
    ; initiate async handler, return updated db after
    (go (let [params   {:tag data :token token}
              channel  (http/get "/api/search" {:query-params params})
              {{posts :data} :body} (<! channel)]
          (log/debug "Received " (count posts) " posts from backend")
          (dispatch [:search-results posts])))
    (assoc db :search-pending true)))

(register-handler :search-results
  (fn [db [_ posts]]
    (-> db
        (assoc :search-pending false)
        (assoc :posts posts))))
