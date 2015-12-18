(ns exposure.handler
  (:require [environ.core :refer [env]]
            [compojure.core :refer [GET defroutes context]]
            [compojure.route :refer [not-found resources]]

            [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
            [prone.middleware :refer [wrap-exceptions]]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.json :refer [wrap-json-response]]

            [exposure.layout :as layout]
            [exposure.instagram-api :as instagram]))


(defroutes routes
  (GET "/"      [] layout/loading-page)
  (GET "/about" [] layout/loading-page)
  (GET "/auth"  [] layout/auht-page)

  (context "/api" []
    (GET "/ping" [] {:status 200 :body "pong"})
    (GET "/redirected" req (instagram/handle-redirect req)))

  (resources "/")
  (not-found "Not Found"))

(def app
  (let [handler (-> #'routes
                    (wrap-json-response)
                    (wrap-defaults site-defaults))]
    (if (env :dev)
      (-> handler
          wrap-exceptions
          wrap-reload)
      handler)))
