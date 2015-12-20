(ns exposure.routes
  (:require [secretary.core :as secretary :refer-macros [defroute]]
            [taoensso.timbre :as log :include-macros true]
            [accountant.core :as accountant]
            [re-frame.core :refer [dispatch]]))

(log/debug "Registering routes")


(defn init-routes []
  (log/debug "Initializing routes")

  (defroute "/" []
    (dispatch [:set-active-page :home-page]))

  (defroute "/about" []
    (dispatch [:set-active-page :about-page]))

  (accountant/configure-navigation!)
  (accountant/dispatch-current!))
