(ns exposure.handler
  (:require [compojure.core :refer [GET defroutes]]
            [compojure.route :refer [not-found resources]]
            [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
            [prone.middleware :refer [wrap-exceptions]]
            [ring.middleware.reload :refer [wrap-reload]]
            [environ.core :refer [env]]

            [exposure.layout :as layout]))




(defroutes routes
  (GET "/" [] layout/loading-page)
  (GET "/about" [] layout/loading-page)

  (resources "/")
  (not-found "Not Found"))

(def app
  (let [handler (wrap-defaults #'routes site-defaults)]
    (if (env :dev)
      (-> handler
          wrap-exceptions
          wrap-reload)
      handler)))
