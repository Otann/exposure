(ns exposure.core
    (:require [reagent.core :as reagent]
              [re-frame.core :as re-frame]
              [taoensso.timbre :as log :include-macros true]
              [exposure.config :as config]
              [exposure.views :as views]
              [exposure.routes :as routes]
              [exposure.handlers]
              [exposure.subscriptions]))

(when config/debug?
  (log/debug "Running app in dev mode"))

(defn mount-root []
  (reagent/render [views/root-component]
                  (.getElementById js/document "app")))

(defn ^:export init []
  (re-frame/dispatch-sync [:initialize-db])
  (routes/init-routes)
  (mount-root))

(init)
